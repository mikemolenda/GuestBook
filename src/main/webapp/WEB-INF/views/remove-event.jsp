<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<p>The following event will be <strong>permanently</strong> deleted from the database:</p>

<table>
    <tr>
        <td><strong>Event Name:</strong></td>
        <td>${event.name}</td>
    </tr>
    <tr>
        <td><strong>Event Start:</strong></td>
        <td>
            ${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}
            ${event.startDateTime.getHour()}:${event.startDateTime.getMinute() < 10 ? "0" : "&nbsp;"}${event.startDateTime.getMinute()}
        </td>
    </tr>
    <tr>
        <td><strong>Event End:</strong></td>
        <td>
            ${event.endDateTime.getMonthValue()}/${event.endDateTime.getDayOfMonth()}/${event.endDateTime.getYear()}
            ${event.endDateTime.getHour()}:${event.endDateTime.getMinute() < 10 ? "0" : "&nbsp;"}${event.endDateTime.getMinute()}
        </td>
    </tr>
    <tr>
        <td><strong>Presenter:</strong></td>
        <td>${event.presenter.firstName} ${event.presenter.lastName}</td>
    </tr>
    <tr>
        <td><strong>Registration Type:</strong></td>
        <td>${event.openRegistration ? "Open" : "Closed"}</td>
    </tr>
    <tr>
        <td><strong>Registration Code:</strong></td>
        <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
    </tr>
    <tr>
        <td><strong>Survey Required:</strong></td>
        <td>${event.mandatorySurvey ? "Yes" : "No"}</td>
    </tr>
    <tr>
        <td><strong>Max Capacity:</strong></td>
        <td>${event.capacity > 0 ? event.capacity : "<em>none</em>"}</td>
    </tr>
</table>

<p>This effectively cancels the event. Are you sure?</p>

<form action="remove-event" method="post">
    <input type="hidden" name="id" value="${event.id}"><br>
    <table>
        <tr>
            <td><input type="submit" value="confirm delete"></td>
            <td><button onclick="history.go(-1)">cancel</button></td>
        </tr>
    </table>
</form>

<hr>

<button onclick="history.go(-1)">back</button>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>


