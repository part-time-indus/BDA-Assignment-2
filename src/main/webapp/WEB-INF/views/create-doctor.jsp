<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Add doctor" scope="request"/>
<c:set var="activeNav" value="doctors" scope="request"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<div class="page-head"><h1>Add doctor</h1></div>

<form class="card form js-form" action="${ctx}/doctors" method="post" data-next="/doctors">
  <%@ include file="/WEB-INF/views/_doctor-fields.jspf" %>
  <div class="form-actions">
    <button type="submit" class="btn primary">Create doctor</button>
    <a class="btn" href="${ctx}/doctors">Cancel</a>
  </div>
</form>

<%@ include file="/WEB-INF/views/_footer.jspf" %>
</body>
</html>
