<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<form class="form-horizontal" action="show-user-info-guest" method="post">
    <div class="row padding-horiz-10px">
        <div class="col-sm-offset-1">
            <div class="form-group">
                <label class="control-label col-sm-3" for="first-name">First Name:</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="first-name" id="first-name" value="${sessionUser.firstName}" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="last-name">Last Name:</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="last-name" id="last-name" value="${sessionUser.lastName}" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3" for="email">Email Address:</label>
                <div class="col-sm-5">
                    <input type="email" class="form-control" name="email" id="email" value="${sessionUser.email}" required>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-3">Password:</label>
                <div class="col-sm-5">
                    <a class="btn btn-default btn-block" href="update-password-guest">Click here to update</a>
                </div>
            </div>

        </div>
    </div>

    <div class="spacer_1em"></div>

    <div class="form-group text-center">
        <a class="btn btn-primary" href="home">Cancel</a>
        <input type="hidden" name="type" value="GUEST">
        <input type="submit" class="btn btn-success" value="Update Information">
    </div>

</form>

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

</body>
</html>
