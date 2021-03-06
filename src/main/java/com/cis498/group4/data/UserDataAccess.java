package com.cis498.group4.data;

import com.cis498.group4.models.User;
import com.cis498.group4.util.DbConn;
import com.cis498.group4.util.UserHelpers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * The UserDataAccess class facilitates operations on User data in the database.
 */
public class UserDataAccess {

    private Connection connection;

    // Default SQL to select all attributes from User table
    private final String SELECT_ALL_ATTRIBUTES = "SELECT u.`user_id`, ut.`user_type`, u.`first_name`, u.`last_name`, " +
            "u.`email` FROM `user` u INNER JOIN `user_type` ut ON u.`user_type_id` = ut.`user_type_id`";

    // Get new database connection
    public UserDataAccess() {
        this.connection = DbConn.getConnection();
    }

    /**
     * Retrieves a single user from the database
     * @param id The ID of the row to retrieve
     * @return User object with the data from the row
     */
    public User getUser(int id) {
        User user = new User();

        try {
            // Set id parameter and execute SQL statement
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE u.`user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in User object
            if (results.next()) {
                setAttributes(user, results);
            } // TODO return null if none found

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Retrieves a single user by their email address.
     * This assumes that a unique constraint has been placed on the email column.
     * @param email The email address of the user to retrieve
     * @return User object
     */
    public User getUserByEmail(String email) {
        User user = new User();

        try {
            // Set id parameter and execute SQL statement
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE u.`email` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet results = preparedStatement.executeQuery();

            // Store results in User object
            if (results.next()) {
                setAttributes(user, results);
            } // TODO return null if none found - used in AddRegistration and AddRegistrationCSV

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Retrieves all users in the database
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = SELECT_ALL_ATTRIBUTES;
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in list of Users
            while (results.next()) {
                User user = new User();
                setAttributes(user, results);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Retrieves all users with type "ORGANIZER"
     * @return List of User objects
     */
    public List<User> getOrganizers() {
        List<User> organizers = new ArrayList<User>();

        try {
            // Execute SQL statement - no parameters, so no need to prepare
            String sql = SELECT_ALL_ATTRIBUTES + " WHERE u.`user_type_id` = 0";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);

            // Store results in list of Users
            while (results.next()) {
                User user = new User();
                setAttributes(user, results);
                organizers.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return organizers;
    }

    /**
     * Gets the specified user's password hash from the database
     * @param user The user whose password to retrieve
     * @return Hex string value of the password hash or empty String for failure
     */
    public String getUserPasswordHash(User user) {
        String hash = "";

        try {
            // Set parameters and execute SQL
            String sql = "SELECT `password` FROM `user` WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            ResultSet results = preparedStatement.executeQuery();

            if(results.next()) {
                hash = results.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hash;
    }

    /**
     * Checks a user password by comparing its SHA hash to a stored hash
     * @param password The password to compare
     * @param user The user whose password is being checked
     * @return true if the hashes match, otherwise false
     */
    public boolean checkPassword(String password, User user) {
        try {
            String storedHash = getUserPasswordHash(user);
            String passHash = UserHelpers.shaHash(password);

            if (passHash.equals(storedHash)) {
                return true;
            } else {
                return false;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * Inserts a new user into the `user` table in the database
     * @param user The User object to insert
     * @return 0 for success, -1 for invalid data, -2 or -3 for encryption error, SQL error code for database failure
     */
    public int insertUser(User user) {
        if (!UserHelpers.validateFields(user)) {
            return -1;
        }

        try {
            // Set parameters and execute SQL
            String sql = "INSERT INTO `user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) " +
                         "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getType().ordinal());
            preparedStatement.setString(2, user.getFirstName().trim());
            preparedStatement.setString(3, user.getLastName().trim());
            preparedStatement.setString(4, user.getEmail().trim());
            // Only set password for new users
            preparedStatement.setString(5, UserHelpers.shaHash(user.getPassword().trim()));
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        } catch (NoSuchAlgorithmException e) {
            return -2;
        } catch (UnsupportedEncodingException e) {
            return -3;
        }
    }

    /**
     * Updates the data of the user with the specified ID in the `user` table in the database
     * @param user The User object to update
     * @return 0 for success, -1 for invalid data, SQL error code for database failure
     */
    public int updateUser(User user) {
        if (!UserHelpers.validateRecord(user)) {
            return -1;
        }

        try {
            // Set parameters and execute SQL
            String sql = "UPDATE `user` SET `user_type_id` = ?, `first_name` = ?, `last_name` = ?, `email` = ? " +
                         "WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getType().ordinal());
            preparedStatement.setString(2, user.getFirstName().trim());
            preparedStatement.setString(3, user.getLastName().trim());
            preparedStatement.setString(4, user.getEmail().trim());
            // Do not set password on update. Use updateUserPassword instead
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Updates user password by writing a new password hash to the DB
     * @param user The user whose password is to be updated
     * @param password The new password
     * @return 0 for success, -1 for invalid data, -2 or -3 for encryption error, SQL error code for database failure
     */
    public int updateUserPassword(User user, String password) {
        if (!UserHelpers.validatePassword(password)) {
            return -1;
        }

        try {
            String sql = "UPDATE `user` SET `password` = ? WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, UserHelpers.shaHash(password.trim()));
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        } catch (NoSuchAlgorithmException e) {
            return -1;
        } catch (UnsupportedEncodingException e) {
            return -2;
        }
    }

    /**
     * Deletes the user with the specified ID from the database
     * @param user The user to delete
     * @return 0 for success, SQL error code for failure
     */
    public int deleteUser(User user) {
        try {
            // Set id parameter and execute SQL
            String sql = "DELETE FROM `user` WHERE `user_id` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            return e.getErrorCode();
        }
    }

    /**
     * Sets the attributes of a User object based on the result set from a SQL query
     * @param user The user whose attributes to set
     * @param results The results set containing the data
     */
    private void setAttributes(User user, ResultSet results) throws SQLException, IllegalArgumentException {
        user.setId(results.getInt("user_id"));
        user.setType(User.UserType.valueOf(escapeHtml4(results.getString("user_type").trim().toUpperCase())));
        user.setFirstName(escapeHtml4(results.getString("first_name")));
        user.setLastName(escapeHtml4(results.getString("last_name")));
        user.setEmail(escapeHtml4(results.getString("email")));
        // NOTE: Do not get passwords from DB with normal read operations, use getUserPassword
    }

}
