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
  		$("#propname_select").change(function() {
  			$("#propname_text").val($(this).val());
  		});
  		$("#propname_select").val($("#propname_text").val());
  	});
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <c:choose>
    <c:when test="${empty property.id}">
      <h3>Create Project Property</h3>
    </c:when>
    <c:otherwise>
      <h3>Edit Project Property</h3>
    </c:otherwise>
  </c:choose>
  <br>
  
  <form:form method="post" commandName="property">
  <input type="hidden" name="projectId" value="${projectId}"/>
  <table border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td>Facility</td>
      <td>&nbsp;</td>
      <td><form:select id="facility" path="facilityId" items="${facilities}"/></td>
    </tr>
    <tr>
      <td>Property Name<br>(select existing or enter new)</td>
      <td>&nbsp;</td>
      <td><form:select id="propname_select" path="" items="${propnames}"/><form:input id="propname_text" path="propname" size="40"/></td>
    </tr>
    <tr>
      <td valign="top">Property Value</td>
      <td>&nbsp;</td>
      <td><form:textarea path="propvalue" rows="5" cols="104"/></td>
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
