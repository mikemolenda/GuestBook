package com.mikemolenda.guestbook.controller;

import com.mikemolenda.guestbook.data.UserDataAccess;
import com.mikemolenda.guestbook.models.User;
import com.mikemolenda.guestbook.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The _ListUsers servlet responds to requests to view a list of users.
 */
@WebServlet(name = "_ListUsers", urlPatterns = "/manager/list-users")
public class _ListUsers extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public _ListUsers() {
        super();
        userData = new UserDataAccess();
    }

    /**
     * Render a list of users, with buttons to view, edit, or delete each
     * @param request The HTTP request received from the client
     * @param response The HTTP response returned by the servlet
     * @throws ServletException The request could not be handled
     * @throws IOException An input or output error has occurred
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            request.setAttribute("statusMessage", "You must be logged in as an organizer to view the requested page.");
            request.setAttribute("statusType", "warning");
            RequestDispatcher view = request.getRequestDispatcher("/manager/login");
            view.forward(request, response);
            return;
        }

        String url = "/WEB-INF/views/list-users.jsp";
        String pageTitle = "Users";

        // Get list of users from DB
        List<User> users = userData.getAllUsers();
        request.setAttribute("users", users);

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
