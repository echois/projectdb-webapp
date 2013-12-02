<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <meta charset="utf-8">
  <script src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/common.css" type="text/css"/>  
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/jquery-ui.css" type="text/css"/>
  <script>
    $(function() {
        $("#datepicker").datepicker({ dateFormat: "yy-mm-dd" });
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <c:choose>
    <c:when test="${empty researchOutput.id}">
      <h3>Create Research Output</h3>
    </c:when>
    <c:otherwise>
      <h3>Edit Research Output</h3>
    </c:otherwise>
  </c:choose>
  <br>
  
  <form:form method="post" commandName="researchOutput">
	<spring:bind path="researchOutput.projectId">
		<input type="hidden" name="adviserId" value="${adviserId}"/>
	</spring:bind>
  <table border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td>Date</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker" path="date" size="20"/></td>
    </tr>
    <tr>
      <td>Type</td>
      <td>&nbsp;</td>
      <td><form:select path="typeId" items="${researchOutputTypes}"/></td>
    </tr>
    <tr>
      <td valign="top">Description/Citation</td>
      <td valign="top">&nbsp;</td>
      <td><form:textarea path="description" rows="5" cols="104"/></td>
    </tr>
    <tr>
      <td>Link</td>
      <td>&nbsp;</td>
      <td><form:input path="link" size="120"/></td>
    </tr>
  </table>
  <br>
  <input type="submit" align="center" value="Submit">
  </form:form>
      
  </div>
</body>
</html>
