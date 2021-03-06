package com.cis498.group4.util;

import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.Survey;
import com.cis498.group4.models.User;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The SurveyHelpers class contains methods to assist with Survey data (verification, etc)
 */
public class SurveyHelpers {

    public static final String[] QUESTIONS = {
            "Overall, how would you rate your satisfaction with the event?",
            "How satisfied were you with the quality of the information presented?",
            "How relevant did you find the information in the presentation?",
            "How satisfied are you with the presenter's knowledge of the subject?",
            "How satisfied are you with the quality of the presentation?",
            "How likely are you to attend another event by the same presenter?",
            "How satisfied were you with the organization of the event?",
            "How satisfied were you with the staff at the event?",
            "How well did we respond to your questions and concerns?",
            "How likely are you to recommend the event to a friend or colleague?"
    };

    public static final String[] RESPONSE_TYPES = {
            "satisfied",
            "satisfied",
            "relevant",
            "satisfied",
            "satisfied",
            "likely",
            "satisfied",
            "satisfied",
            "well",
            "likely"
    };

    // Status codes
    public static final int SUCCESSFUL_SUBMISSION = 0;
    public static final int INVALID_USER = 1;
    public static final int INVALID_USER_TYPE = 2;
    public static final int INVALID_EVENT = 3;
    public static final int INVALID_EVENT_DATE = 4;
    public static final int INVALID_ATTENDANCE = 5;
    public static final int INVALID_RESPONSE = 6;
    public static final int INVALID_DATA = 7;

    /**
     * Validates a survey record (e.g. survey does not already exist).
     * Use before writing to database.
     * @param survey The survey to validate
     * @return true iof the survey is not null and its data is valid
     */
    public static boolean validate(Survey survey) {
        // TODO User attended event
        // TODO Survey does not already exist
        return true;
    }

    /**
     * Ensures that the user submitting the survey has signed in to the event
     * @param attendance The attendance data for the user submitting the survey
     * @return true if attendance is SIGNED_IN or ATTENDED
     */
    public static boolean validateAttendance(Attendance attendance) {
        if (attendance == null) {
            return false;
        }

        if (attendance.getStatus() == null) {
            return false;
        }

        return (attendance.getStatus() != Attendance.AttendanceStatus.NOT_ATTENDED);
    }

    /**
     * Validates that event exists and is valid
     * @param event The event to check
     * @return true if event is not null and has a valid ID
     */
    public static boolean validateEvent(Event event) {
        if (event == null) {
            return false;
        }

        if (event.getId() < 1) {
            return false;
        }

        return true;
    }

    /**
     * Validates that a response is an integer between 1 and 10
     * @param response The rating from a survey question
     * @return true if response is an integer between 1 and 10
     */
    public static boolean validateResponse(int response) {
        return (response >= 1 && response <= 10);
    }

    /**
     * Calculates and returns the arithmetic mean for this Survey
     * @return BigDecimal average with scale of 2
     */
    public static BigDecimal responseAverage(Survey survey) {
        BigDecimal sum = new BigDecimal(0);

        Iterator<Integer> it = survey.getResponses().values().iterator();
        while (it.hasNext()) {
            sum = sum.add(new BigDecimal(it.next()));
        }

        return sum.divide(BigDecimal.TEN, 1, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Generates a sentiment string from the average of the survey responses
     * @param value The value of the survey response average
     * @return String representing the sentiment of the survey
     */
    public static String responseSentiment(double value) {
        if (value < 2.5) {
            return "very negative";
        }

        if (value >= 2.5 && value < 5.0) {
            return "somewhat negative";
        }

        if (value >= 5.0 && value < 7.5) {
            return "somewhat positive";
        }

        if (value >= 7.5) {
            return "very positive";
        }

        return "ambiguous";
    }

    /**
     * Remaps survey responses to question strings
     * @param survey The survey to remap
     * @return Map of question text to survey response
     */
    public static Map<String, Integer> getQuestionsResponses(Survey survey) {
        Map<String, Integer> questionsResponses = new HashMap<String, Integer>();
        Map<String, Integer> responses = survey.getResponses();

        for(int i = 0; i < 10; i++) {
            String responseLabel = String.format("response_%02d", i + 1);
            questionsResponses.put(QUESTIONS[i], responses.get(responseLabel));
        }

        return questionsResponses;
    }

    /**
     * Sets a survey object's attributes based on parameters passed in request
     * @param survey The survey to set
     * @param user The user of the survey
     * @param attendance The attendance data for the survey
     * @return Status code of the operation, determined by submissionStatus
     */
    public static int setAttributesFromRequest(Survey survey, User user, Attendance attendance, HttpServletRequest request) {
        try {
            survey.setUser(user);
            survey.setEvent(attendance.getEvent());
            LocalDateTime submissionDateTime = LocalDateTime.now();
            survey.setSubmissionDateTime(submissionDateTime);

            Map<String, Integer> responses = new HashMap<String, Integer>();    //TODO Make this a TreeMap, so it is sorted for iteration

            for(int i = 1; i <= 10; i++) {
                String responseLabel = String.format("response_%02d", i);
                responses.put(responseLabel, Integer.valueOf(request.getParameter(responseLabel)));
            }

            survey.setResponses(responses);

            // Get status code
            return submissionStatus(survey, user, attendance);

        } catch (Exception e) {
            return INVALID_DATA;
        }
    }

    /**
     * Get status code for verifying the success or failure of submission
     * @param survey The survey to verify
     * @param user The survey user data
     * @param attendance The survey attendance data
     * @return Status code indicating the success of the operation
     */
    @SuppressWarnings("unchecked")
    public static int submissionStatus(Survey survey, User user, Attendance attendance) {
        if (!UserHelpers.validateRecord(user)) {
            return INVALID_USER;
        }

        if (user.getType() != User.UserType.GUEST) {
            return INVALID_USER_TYPE;
        }

        if (!validateEvent(attendance.getEvent())) {
            return INVALID_EVENT;
        }

        if (EventHelpers.endsInFuture(attendance.getEvent())) {
            return INVALID_EVENT_DATE;
        }

        if (!validateAttendance(attendance)) {
            return INVALID_ATTENDANCE;
        }

        Iterator responses = survey.getResponses().entrySet().iterator();
        while (responses.hasNext()) {
            Map.Entry<String, Integer> response = (Map.Entry<String, Integer>) responses.next();
            if (!validateResponse(response.getValue())) {
                return INVALID_RESPONSE;
            }
        }

        return SUCCESSFUL_SUBMISSION;
    }

}
