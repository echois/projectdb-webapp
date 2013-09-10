<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <script src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery.tablesorter.min.js"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/common.css" type="text/css"/>  
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/jquery-ui.css" type="text/css"/>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/tablesorter/blue/style.css" type="text/css"/>
  <script>
    $(document).ready(function() {
      $("#myTable").tablesorter();
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">

  <a href="<%=request.getContextPath()%>/html/editresearcher?id=${researcher.id}">Edit</a> | 
  <a href="<%=request.getContextPath()%>/html/deleteresearcher?id=${researcher.id}">Delete</a>
  
  <h3>${researcher.fullName}</h3>

  <br><img src="${researcher.pictureUrl}" width="80px"/><br><br>
  
  <table border="0" cellspacing="0" cellpadding="5">
    <tr>
      <td valign="top">Email:</td>
      <td>${researcher.email}</td>
    </tr>
    <tr>
      <td valign="top">Phone:</td>
      <td>${researcher.phone}</td>
    </tr>
    <tr>
      <td>Institution:</td>
      <td>${researcher.institution}</td>
    </tr>
    <tr>
      <td>Division/Faculty:</td>
      <td>${researcher.department1}</td>
    </tr>
    <tr>
      <td>Department:</td>
      <td>${researcher.department2}</td>
    </tr>
    <tr>
      <td>Institutional role:</td>
      <td>${researcher.institutionalRoleName}</td>
    </tr>
    <tr>
      <td>Start date:</td>
      <td>${researcher.startDate}</td>
    </tr>
    <tr>
      <td>End date:</td>
      <td>${researcher.endDate}</td>
    </tr>
    <tr>
      <td valign="top">Notes:</td>
      <td>${researcher.notes}</td>
    </tr>
  </table>
  
  <br><br>   
  <b>Projects:</b>

  <table id="myTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Name</th>
	    <th>Next review</th>
	    <th>Next follow-up</th>
	    <th>Type</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${projects}" var="project">
      <tr>
        <td><a href="<%=request.getContextPath()%>/html/viewproject?id=${project.id}">${project.name}</a></td>
        <td>${project.nextReviewDate}</td>
        <td>${project.nextFollowUpDate}</td>
        <td>${project.projectTypeName}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  </div>
</body>
</html>
