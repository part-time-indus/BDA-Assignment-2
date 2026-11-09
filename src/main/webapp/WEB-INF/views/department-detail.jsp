<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Department" scope="request"/>
<c:set var="activeNav" value="departments" scope="request"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<c:set var="d" value="${department}"/>

<div class="page-head">
  <h1><c:out value="${d.deptName}"/></h1>
  <div>
    <a class="btn" href="${ctx}/departments">Back to list</a>
    <a class="btn primary" href="${ctx}/departments/edit/${d.deptId}">Edit</a>
  </div>
</div>

<section class="card">
  <dl class="details">
    <dt>Name</dt><dd><c:out value="${d.deptName}"/></dd>
    <dt>Location</dt><dd id="locView"><c:out value="${d.location}"/></dd>
    <dt>Phone</dt><dd id="phoneView"><c:out value="${d.phone}" default="-"/></dd>
    <dt>Doctors</dt><dd><a href="${ctx}/doctors/department/id?deptId=${d.deptId}">View doctors in this department</a></dd>
  </dl>
</section>

<section class="card">
  <h2>Quick updates</h2>
  <form id="locForm" class="inline-form js-form" action="${ctx}/departments/${d.deptId}/location" method="post">
    <label>Location
      <input type="text" name="location" maxlength="60" required value="<c:out value='${d.location}'/>">
    </label>
    <button type="submit" class="btn">Update location</button>
  </form>
  <form id="phoneForm" class="inline-form js-form" action="${ctx}/departments/${d.deptId}/phone" method="post">
    <label>Phone
      <input type="tel" name="phone" maxlength="15" required value="<c:out value='${d.phone}'/>">
    </label>
    <button type="submit" class="btn">Update phone</button>
  </form>
</section>

<%@ include file="/WEB-INF/views/_footer.jspf" %>
<script>
$('#locForm').on('saved',   function () { $('#locView').text($(this).find('input').val()); });
$('#phoneForm').on('saved', function () { $('#phoneView').text($(this).find('input').val()); });
</script>
</body>
</html>
