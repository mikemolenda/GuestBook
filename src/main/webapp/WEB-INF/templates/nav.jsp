<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">

            <div id="navbar-logo"><a href="home">Guestbook</a></div>

            <c:if test="${sessionUser != null}">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#nav-menu">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </c:if>

        </div>
        <div class="collapse navbar-collapse" id="nav-menu">

            <c:if test="${sessionUser.type == 'ORGANIZER'}">
                <ul class="nav navbar-nav">
                    <li><a href="list-users">Users</a></li>
                    <li><a href="list-events">Events</a></li>
                    <li><a href="list-surveys">Surveys</a></li>
                    <li><a href="list-event-registrations">Registration</a></li>
                    <li><a href="start-kiosk">Sign-In Kiosk</a></li>
                    <li><a href="logout">Logout ${sessionUser.firstName} ${sessionUser.lastName}</a></li>
                </ul>
            </c:if>

            <c:if test="${sessionUser.type == 'GUEST'}">
                <ul class="nav navbar-nav">
                    <li><a href="list-events-guest">My Events</a></li>
                    <li><a href="list-surveys-guest">My Surveys</a></li>
                    <li><a href="list-registrations-guest">Event Registration</a></li>
                    <li><a href="show-user-info-guest">My Account</a></li>
                    <li><a href="logout">Logout ${sessionUser.firstName} ${sessionUser.lastName}</a></li>
                </ul>
            </c:if>

        </div>
    </div>
</nav>
