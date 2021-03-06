<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<table class="table table-responsive" id="event-registrations-list">
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

<jsp:include page="/WEB-INF/templates/footer.jsp"></jsp:include>

<script>
    // jQuery DataTables https://datatables.net/
    $(document).ready(function() {
        var table = $('#event-registrations-list').DataTable( {
            dom: '<"row"<"col-sm-12"i>>' +
            '<"row"<"col-sm-6"l><"col-sm-6"f>>' +
            '<"row"<"col-sm-12"rt>>' +
            '<"spacer_20">' +
            '<"row"<"col-sm-6"B><"col-sm-6"p>>',
            columnDefs: [ { orderable: false, targets: [6, 7] },
                { render: $.fn.dataTable.render.ellipsis(25), targets: [0, 2] } ],
            buttons: [
                { extend: 'csv', text: 'Download CSV', className: 'btn-primary' },
                { extend: 'print', className: 'btn-primary'},
            ]
        });
    });

    // render ellipses for data longer than maxLen
    $.fn.dataTable.render.ellipsis = function (maxLen) {
        return function (data, type, row) {
            return type === 'display' && data.length > maxLen ?
                data.substr(0, maxLen) +'&hellip;' :
                data;
        }
    };

</script>

</body>
</html>