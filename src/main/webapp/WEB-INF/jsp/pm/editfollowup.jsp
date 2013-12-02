<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <meta charset="utf-8">
  <script src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery.watermark.min.js"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/common.css" type="text/css"/>  
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/jquery-ui.css" type="text/css"/>
  <script>
    $(function() {
        $("#datepicker").datepicker({ dateFormat: "yy-mm-dd" });

        // form field watermarks
        $("#attachment_description").watermark("If you add an attachment, describe what it is");
        $("#attachment_link").watermark("If you add an attachment, store it on a publicly available webserver, and add a link here");
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <c:choose>
    <c:when test="${empty followUp.id}">
      <h3>Create Follow-up</h3>
    </c:when>
    <c:otherwise>
      <h3>Edit Follow-up</h3>
    </c:otherwise>
  </c:choose>
  <br>
  
  <form:form method="post" commandName="followUp">
  	<spring:bind path="followUp.adviserId">
		<input type="hidden" name="adviserId" value="${adviserId}"/>
	</spring:bind>
  <table border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td>Date</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker" path="date" size="20"/></td>
    </tr>
    <tr>
      <td valign="top">Notes</td>
      <td>&nbsp;</td>
      <td><form:textarea path="notes" rows="5" cols="104"/></td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
  </table>
  <br>
  
  <input type="submit" align="center" value="Submit">
  </form:form>
      
  </div>
</body>
</html>
