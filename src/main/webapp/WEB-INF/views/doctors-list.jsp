<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Doctors" scope="request"/>
<c:set var="activeNav" value="doctors" scope="request"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<div class="page-head">
  <h1>Doctors</h1>
  <a class="btn primary" href="${ctx}/doctors/new">Add doctor</a>
</div>

<form id="filterForm" class="filters">
  <input type="text" id="fSpec" placeholder="Specialisation" aria-label="Specialisation">
  <select id="fDept" data-options="department" data-placeholder="Any department" aria-label="Department"></select>
  <select id="fStatus" data-options="status" data-placeholder="Any status" aria-label="Status"></select>
  <button type="submit" class="btn">Apply</button>
  <button type="button" id="clearBtn" class="btn">Clear</button>
  <span class="spacer"></span>
</form>
<form id="lookupForm" class="filters">
  <input type="text" id="lookup" placeholder="Find by phone or email" aria-label="Phone or email" size="28">
  <button type="submit" class="btn">Find</button>
</form>

<div class="table-wrap">
  <table id="doctorTable">
    <thead>
      <tr><th>Name</th><th>Specialisation</th><th>Department</th><th>Phone</th><th>Fee</th><th>Status</th><th></th></tr>
    </thead>
    <tbody>
      <c:forEach var="d" items="${doctorList}">
        <tr data-id="${d.doctorId}">
          <td><a href="${ctx}/doctors/${d.doctorId}"><c:out value="${d.firstName} ${d.lastName}"/></a></td>
          <td><c:out value="${d.specialisation}"/></td>
          <td class="dept-cell" data-dept="${d.deptId}">${d.deptId}</td>
          <td><c:out value="${d.phone}"/></td>
          <td class="num"><fmt:formatNumber value="${d.consultationFee}" minFractionDigits="2" maxFractionDigits="2"/></td>
          <td>
            <select class="status-select" data-options="status" data-selected="${d.status}" data-id="${d.doctorId}" aria-label="Status"></select>
          </td>
          <td class="actions">
            <a class="btn small" href="${ctx}/doctors/edit/${d.doctorId}">Edit</a>
            <button type="button" class="btn small danger js-delete"
                    data-url="/doctors/delete/${d.doctorId}"
                    data-name="Dr. <c:out value='${d.firstName} ${d.lastName}'/>">Delete</button>
          </td>
        </tr>
      </c:forEach>
      <c:if test="${empty doctorList}">
        <tr class="empty"><td colspan="7">No doctors found.</td></tr>
      </c:if>
    </tbody>
  </table>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>
<script>
var base = APP_CTX + '/doctors';

// Filters: replace only the table body using AJAX
$('#filterForm').on('submit', function (e) {
  e.preventDefault();
  var spec = $.trim($('#fSpec').val()), dept = $('#fDept').val(), st = $('#fStatus').val(), url = base;
  if (spec)           { url = base + '/specialisation?specialisation=' + encodeURIComponent(spec); }
  else if (dept && st) { url = base + '/department/status?deptId=' + dept + '&status=' + encodeURIComponent(st); }
  else if (dept)       { url = base + '/department/id?deptId=' + dept; }
  else if (st)         { toast('Pick a department to filter by status', 'err'); return; }
  loadTable(url, '#doctorTable');
});

$('#clearBtn').on('click', function () {
  $('#filterForm')[0].reset();
  loadTable(base, '#doctorTable');
});

// Phone / email lookup opens the detail page
$('#lookupForm').on('submit', function (e) {
  e.preventDefault();
  var v = $.trim($('#lookup').val());
  if (!v) { return; }
  var param = v.indexOf('@') > -1 ? 'email' : 'phone';
  window.location.href = base + '/' + param + '?' + param + '=' + encodeURIComponent(v);
});

// Change status straight from the table
$(document).on('change', '.status-select', function () {
  var $s = $(this), prev = $s.attr('data-selected');
  $.post(base + '/' + $s.attr('data-id') + '/status', { status: $s.val() })
    .done(function () { $s.attr('data-selected', $s.val()); toast('Status updated'); })
    .fail(function () { $s.val(prev); toast('Could not update status', 'err'); });
});
</script>
</body>
</html>
