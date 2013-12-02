<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <meta charset="utf8">
  <script src="<%=request.getContextPath()%>/js/jquery1.8.3.js"></script>
  <script src="<%=request.getContextPath()%>/js/jqueryui.js"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/common.css" type="text/css"/>  
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/jqueryui.css" type="text/css"/>
  <script>
    $(function() {
        $("#datepicker").datepicker({ dateFormat: "yymmdd" });
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <c:choose>
    <c:when test="${empty attachment.id}">
      <h3>Create Attachment</h3>
    </c:when>
    <c:otherwise>
      <h3>Edit Attachment</h3>
    </c:otherwise>
  </c:choose>
  <br>
  
  <form:form method="post" commandName="attachment">
  <table border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td>Date</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker" path="date" size="20"/></td>
    </tr>
    <tr>
      <td valign="top">Description</td>
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
  <input type="submit" align="center" value="Add attachment">
  </form:form>
      
  </div>
</body>
</html>
