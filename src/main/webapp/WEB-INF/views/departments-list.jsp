<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Departments" scope="request"/>
<c:set var="activeNav" value="departments" scope="request"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<div class="page-head">
  <h1>Departments</h1>
  <a class="btn primary" href="${ctx}/departments/new">Add department</a>
</div>

<form id="filterForm" class="filters">
  <select id="filterType" aria-label="Filter type">
    <option value="all">All departments</option>
    <option value="name">By name</option>
    <option value="location">By location</option>
    <option value="specialisation">By doctor specialisation</option>
    <option value="status">By doctor status</option>
    <option value="empty">Without doctors</option>
  </select>
  <input type="text" id="filterText" placeholder="Search text" aria-label="Search text">
  <select id="filterStatus" data-options="status" aria-label="Doctor status"></select>
  <button type="submit" class="btn">Apply</button>
</form>

<div class="table-wrap">
  <table id="deptTable">
    <thead>
      <tr><th>Name</th><th>Location</th><th>Phone</th><th></th></tr>
    </thead>
    <tbody>
      <c:forEach var="d" items="${departmentList}">
        <tr data-id="${d.deptId}" data-name="<c:out value='${d.deptName}'/>">
          <td><a href="${ctx}/departments/${d.deptId}"><c:out value="${d.deptName}"/></a></td>
          <td><c:out value="${d.location}"/></td>
          <td><c:out value="${d.phone}" default="-"/></td>
          <td class="actions">
            <a class="btn small" href="${ctx}/departments/edit/${d.deptId}">Edit</a>
            <button type="button" class="btn small danger js-delete"
                    data-url="/departments/delete/${d.deptId}"
                    data-name="<c:out value='${d.deptName}'/>"
                    data-warn="Doctors in this department will be deleted too.">Delete</button>
          </td>
        </tr>
      </c:forEach>
      <c:if test="${empty departmentList}">
        <tr class="empty"><td colspan="4">No departments found.</td></tr>
      </c:if>
    </tbody>
  </table>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>
<script>
function syncFilter() {
  var t = $('#filterType').val();
  $('#filterText').toggle(t === 'name' || t === 'location' || t === 'specialisation');
  $('#filterStatus').toggle(t === 'status');
}
$('#filterType').on('change', syncFilter);
syncFilter();

$('#filterForm').on('submit', function (e) {
  e.preventDefault();
  var t = $('#filterType').val(), q = $.trim($('#filterText').val()), base = APP_CTX + '/departments';
  if (t === 'all')   { loadTable(base, '#deptTable'); return; }
  if (t === 'empty') { loadTable(base + '/doctors/empty', '#deptTable'); return; }
  if (t === 'status') {
    loadTable(base + '/doctors/status?status=' + encodeURIComponent($('#filterStatus').val()), '#deptTable');
    return;
  }
  if (!q) { toast('Enter a search value', 'err'); return; }
  q = encodeURIComponent(q);
  if (t === 'name') { window.location.href = base + '/name?deptName=' + q; return; }   // returns a single department page
  if (t === 'location') { loadTable(base + '/location?location=' + q, '#deptTable'); }
  if (t === 'specialisation') { loadTable(base + '/doctors/specialisation?specialisation=' + q, '#deptTable'); }
});
</script>
</body>
</html>
