<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Doctor" scope="request"/>
<c:set var="activeNav" value="doctors" scope="request"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<c:set var="d" value="${doctor}"/>

<div class="page-head">
  <h1>Dr. <c:out value="${d.firstName} ${d.lastName}"/></h1>
  <div>
    <a class="btn" href="${ctx}/doctors">Back to list</a>
    <a class="btn primary" href="${ctx}/doctors/edit/${d.doctorId}">Edit</a>
  </div>
</div>

<section class="card">
  <dl class="details">
    <dt>Specialisation</dt><dd><c:out value="${d.specialisation}"/></dd>
    <dt>Department</dt>
    <dd><span id="deptView" class="dept-cell" data-dept="${d.deptId}">${d.deptId}</span>
        &nbsp;<a href="${ctx}/doctors/department/id?deptId=${d.deptId}">Colleagues</a></dd>
    <dt>Gender</dt><dd><c:out value="${d.gender}"/></dd>
    <dt>Phone</dt><dd><c:out value="${d.phone}"/></dd>
    <dt>Email</dt><dd><c:out value="${d.email}" default="-"/></dd>
    <dt>Join date</dt><dd><c:out value="${d.joinDate}"/></dd>
    <dt>Consultation fee</dt><dd id="feeView"><fmt:formatNumber value="${d.consultationFee}" minFractionDigits="2" maxFractionDigits="2"/></dd>
    <dt>Status</dt><dd id="statusView"><c:out value="${d.status}"/></dd>
  </dl>
</section>

<section class="card">
  <h2>Quick updates</h2>
  <form id="statusForm" class="inline-form js-form" action="${ctx}/doctors/${d.doctorId}/status" method="post">
    <label>Status
      <select name="status" data-options="status" data-selected="${d.status}"></select>
    </label>
    <button type="submit" class="btn">Update status</button>
  </form>
  <form id="feeForm" class="inline-form js-form" action="${ctx}/doctors/${d.doctorId}/consultationFee" method="post">
    <label>Consultation fee
      <input type="number" name="consultationFee" min="0" step="0.01" required value="${d.consultationFee}">
    </label>
    <button type="submit" class="btn">Update fee</button>
  </form>
  <form id="deptForm" class="inline-form js-form" action="${ctx}/doctors/${d.doctorId}/department" method="post">
    <label>Department
      <select name="deptId" required data-options="department" data-selected="${d.deptId}"></select>
    </label>
    <button type="submit" class="btn">Change department</button>
  </form>
</section>

<%@ include file="/WEB-INF/views/_footer.jspf" %>
<script>
$('#statusForm').on('saved', function () { $('#statusView').text($(this).find('select').val()); });
$('#feeForm').on('saved', function () {
  $('#feeView').text(parseFloat($(this).find('input').val()).toFixed(2));
});
$('#deptForm').on('saved', function () {
  $('#deptView').text($(this).find('option:selected').text());
});
</script>
</body>
</html>
