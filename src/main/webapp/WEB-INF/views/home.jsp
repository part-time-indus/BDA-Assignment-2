<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home" scope="request"/>
<c:set var="activeNav" value="home" scope="request"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<div class="page-head"><h1>Hospital Management</h1></div>

<div class="tiles">
  <section class="card tile">
    <h2>Doctors</h2>
    <p class="count"><span id="doctorCount">-</span> on record</p>
    <div>
      <a class="btn primary" href="${ctx}/doctors">View doctors</a>
      <a class="btn" href="${ctx}/doctors/new">Add doctor</a>
    </div>
  </section>

  <section class="card tile">
    <h2>Departments</h2>
    <p class="count"><span id="deptCount">-</span> on record</p>
    <div>
      <a class="btn primary" href="${ctx}/departments">View departments</a>
      <a class="btn" href="${ctx}/departments/new">Add department</a>
    </div>
  </section>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>
<script>
// Count rows from the existing list pages (no extra endpoint needed)
function countRows(url, tableSel, $target) {
  $.get(url).done(function (html) {
    var $d = $('<div>').append($.parseHTML(html));
    $target.text($d.find(tableSel + ' tbody tr[data-id]').length);
  }).fail(function () { $target.text('?'); });
}
countRows(APP_CTX + '/doctors', '#doctorTable', $('#doctorCount'));
countRows(APP_CTX + '/departments', '#deptTable', $('#deptCount'));
</script>
</body>
</html>
