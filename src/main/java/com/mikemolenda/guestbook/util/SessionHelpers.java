package com.mikemolenda.guestbook.util;

import com.mikemolenda.guestbook.models.User;

import javax.servlet.http.HttpSession;

/**
 * The SessionHelpers class contains methods to help deal with session data
 */
public class SessionHelpers {

    /**
     * Checks that user is logged in
     * @param session The current session
     * @return True if session user logged in, false otherwise
     */
    public static boolean checkLogin(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser != null) {
            return true;
        }

        return false;
    }

    /**
     * Checks that user is logged in, and is of type Organizer
     * @param session The current session
     * @return True if session user logged in and Organizer, false otherwise
     */
    public static boolean checkOrganizer(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser != null) {
            if (sessionUser.getType() == User.UserType.ORGANIZER) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks that user is logged in, and is of type Guest
     * @param session The current session
     * @return True if session user logged in and Guest, false otherwise
     */
    public static boolean checkGuest(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser != null) {
            if (sessionUser.getType() == User.UserType.GUEST) {
                return true;
            }
        }

        return false;
    }

}
