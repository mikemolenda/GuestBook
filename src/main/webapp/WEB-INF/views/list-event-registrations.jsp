<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive">
    <thead>
        <tr>
            <th>Event Name</th>
            <th>Date</th>
            <th>Presenter</th>
            <th>Reg. Type</th>
            <th>Registration Code</th>
            <th>Registered</th>
            <th></th>
            <th></th>
        </tr>
    </thead>

    <tbody>
        <c:if test="${events.isEmpty()}">
            <tr>
                <td colspan="8" align="center">No events found</td>
            </tr>
        </c:if>

        <c:forEach items="${events}" var="event">
            <tr>
                <td>${event.name}</td>
                <td>${event.startDateTime.getMonthValue()}/${event.startDateTime.getDayOfMonth()}/${event.startDateTime.getYear()}</td>
                <td>${event.presenter.lastName}, ${event.presenter.firstName}</td>
                <td>${event.openRegistration ? "Open" : "Closed"}</td>
                <td>${event.registrationCode != null ? event.registrationCode : "<em>none</em>"}</td>
                <td>
                        ${event.numRegistered} / ${event.capacity > 0 ? event.capacity : "&#8734;"}
                </td>
                <td>
                    <form action="list-user-regs-for-event">
                        <input type="hidden" name="id" value="${event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="view registrations">
                    </form>
                </td>
                <td>
                    <form action="add-registration">
                        <input type="hidden" name="id" value="${event.id}">
                        <input type="submit" class="btn btn-link btn-block" value="register users">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<hr>

<form action="filter-reg">
    <label for="field">Filter by: </label>
    <select name="field" id="field">
        <option>Event Name</option>
        <option>Presenter Last Name</option>
        <option>Event Date</option>
    </select>
    <input type="text" name="value">
    <input type="checkbox" name="exact" checked>Exact matches only
    <input type="submit" class="btn btn-default btn-sm" value="submit">
</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>
</html>