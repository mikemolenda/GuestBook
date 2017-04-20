package com.cis498.group4.controllers;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The ShowEventInfo servlet responds with the information for the specified event.
 */
@WebServlet(name = "ShowEventInfo", urlPatterns = "/manager/view-event")
public class ShowEventInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;

    public ShowEventInfo() {
        super();
        eventData = new EventDataAccess();
    }

    /**
     * Render page to display all data about an event, with buttons to edit and delete
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access if not logged in
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You must login to access this resource");
            return;
        }

        String url = "/WEB-INF/views/view-event.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("event", event);

        String pageTitle = String.format("Info for event \"%s\"", event.getName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}