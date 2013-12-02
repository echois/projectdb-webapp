<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
        $("#datepicker1").datepicker({ dateFormat: "yy-mm-dd" });
        $("#datepicker2").datepicker({ dateFormat: "yy-mm-dd" });

        $("#rplink_notes").watermark("Notes on the researcher on this particular project");
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <h3>${action} researcher on project</h3>
  <br>
  
  <form:form method="post" commandName="rpLink">
  <table border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td>Researcher:</td>
      <td>&nbsp;</td>
      <td><form:select path="researcherId" items="${researchers}"/></td>
    </tr>
    <tr>
      <td>Role on project:</td>
      <td>&nbsp;</td>
      <td><form:select path="researcherRoleId" items="${researcherRoles}"/></td>
    </tr>
    <tr>
      <td valign="top">Notes:</td>
      <td>&nbsp;</td>
      <td><form:textarea id="rplink_notes" path="notes" rows="3" cols="104"/></td>
    </tr>
  </table>
   
  <br>
  <input type="submit" align="center" value="Submit">
  </form:form>
      
  </div>
</body>
</html>
