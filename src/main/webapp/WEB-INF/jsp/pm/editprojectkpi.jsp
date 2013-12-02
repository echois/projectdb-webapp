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
        $("#value").watermark("!!! base 10 scaling factor from now on !!!");
        $("#notes").watermark("Describe the KPI");
        if ($("#kpiId").val()!="9") {
        	$("#code-row").hide(); // Hide by default
        }
        $("#kpiId").change(function() {
        	if ($(this).val()=="9") {
        		$("#code-row").show();
        	} else {
        		$("#code-row").hide();
        	}
        });
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <c:choose>
    <c:when test="${empty projectkpi.id}">
      <h3>Create Project KPI</h3>
    </c:when>
    <c:otherwise>
      <h3>Edit Project KPI</h3>
    </c:otherwise>
  </c:choose>
  <br>
  
  <form:form method="post" commandName="projectkpi">
  <spring:bind path="projectkpi.adviserId">
		<input type="hidden" name="adviserId" value="${adviserId}"/>
  </spring:bind>
  <table border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td>Date</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker" path="date" size="20"/></td>
    </tr>
    <tr>
      <td>KPI</td>
      <td>&nbsp;</td>
      <td><form:select path="kpiId" items="${kpis}"/></td>
    </tr>
    <tr id="code-row">
      <td>Code</td>
      <td>&nbsp;</td>
      <td><form:select path="code" items="${codes}"/></td>
    </tr>
    <tr>
      <td>Value</td>
      <td>&nbsp;</td>
      <td><form:input id="value" path="value" size="80"/></td>
    </tr>
    <tr>
      <td valign="top">Notes</td>
      <td>&nbsp;</td>
      <td><form:textarea path="notes" rows="5" cols="104"/></td>
    </tr>
  </table>
  <br>
  <input type="submit" align="center" value="Submit">
  </form:form>
      
  </div>
</body>
</html>
