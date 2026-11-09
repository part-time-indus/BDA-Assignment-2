<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Add department" scope="request"/>
<c:set var="activeNav" value="departments" scope="request"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<div class="page-head"><h1>Add department</h1></div>

<form class="card form js-form" style="max-width:520px" action="${ctx}/departments" method="post" data-next="/departments">
  <%@ include file="/WEB-INF/views/_department-fields.jspf" %>
  <div class="form-actions">
    <button type="submit" class="btn primary">Create department</button>
    <a class="btn" href="${ctx}/departments">Cancel</a>
  </div>
</form>

<%@ include file="/WEB-INF/views/_footer.jspf" %>
</body>
</html>
