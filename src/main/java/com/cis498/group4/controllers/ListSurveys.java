package com.cis498.group4.controllers;

import com.cis498.group4.data.SurveyDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Survey;
import com.cis498.group4.models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The ListSurveys servlet responds to requests to view a list of surveys.
 */
@WebServlet(name = "ListSurveys", urlPatterns = "/manager/list-surveys")
public class ListSurveys extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SurveyDataAccess surveyData;

    public ListSurveys() {
        super();
        surveyData = new SurveyDataAccess();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/views/list-surveys.jsp";

        List<Survey> surveys = surveyData.getAllSurveys();
        request.setAttribute("surveys", surveys);

        String pageTitle = "Surveys";
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}