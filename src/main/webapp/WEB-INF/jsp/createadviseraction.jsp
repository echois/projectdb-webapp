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
  
  <h3>Add adviser action</h3>
  <br>
  
  <form:form method="post" commandName="adviserAction">
  <spring:bind path="adviserAction.projectId">
    <input type="hidden" name="projectId" value="${pid}"/>
    <input type="hidden" name="adviserId" value="${adviserId}"/>
  </spring:bind>
  <table border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td>Date</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker" path="date" size="20"/></td>
    </tr>
    <tr>
      <td valign="top">Action</td>
      <td valign="top">&nbsp;</td>
      <td><form:textarea path="action" rows="5" cols="104"/></td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2"><b>Attachment:</b></td>
    </tr>
    <tr>
      <td valign="top">Description</td>
      <td>&nbsp;</td>
      <td><form:textarea id="attachment_description" path="attachmentDescription" rows="5" cols="104"/></td>
    </tr>
    <tr>
      <td valign="top">Link</td>
      <td>&nbsp;</td>
      <td><form:input id="attachment_link" path="attachmentLink" size="100"/></td>
    </tr>
  </table>
  <br>
  <input type="submit" align="center" value="Add adviser action to project">
  </form:form>
      
  </div>
</body>
</html>
