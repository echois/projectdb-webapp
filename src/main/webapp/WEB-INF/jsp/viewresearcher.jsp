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
  <a onclick="return (confirm('Are you sure you want to delete this researcher?'))" href="<%=request.getContextPath()%>/html/deleteresearcher?id=${researcher.id}">Delete</a>
  
  <h3>${researcher.fullName}</h3>

  <br><img src="${researcher.pictureUrl}" width="80px"/><br><br>
  
  <table border="0" cellspacing="0" cellpadding="5">
  	<tr>
      <td valign="top">Preferred Name:</td>
      <td>${researcher.preferredName}</td>
    </tr>
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
      <td>${researcher.division}</td>
    </tr>
    <tr>
      <td>Department:</td>
      <td>${researcher.department}</td>
    </tr>
    <tr>
      <td>Institutional role:</td>
      <td>${researcher.institutionalRoleName}</td>
    </tr>
    <tr>
      <td>First Day:</td>
      <td>${researcher.startDate}</td>
    </tr>
    <tr>
      <td>Last Day:</td>
      <td>${researcher.endDate}</td>
    </tr>
    <tr>
      <td valign="top">Notes:</td>
      <td>${researcher.notes}</td>
    </tr>
  </table>
  <c:if test="${not empty linuxUsername}">
  	<br>
  	<b>External Records:</b><br/>
  	<a href="http://ganglia.uoa.nesi.org.nz/hpc/cgi-bin/showq.cgi?user=${linuxUsername }">Currently Running/Queued</a><br/>
  	<a href="http://ganglia.uoa.nesi.org.nz/jobaudit/html/userrecords?upi=${linuxUsername }">Jobaudit Records (History)</a>
  </c:if>
  <br><br>
  <b>Projects:</b>

  <table id="myTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Name</th>
	    <th>Code</th>
	    <th>Type</th>
	    <th>Status</th>
	    <th>First Day</th>
	    <th>Next review</th>
	    <th>Next follow-up</th>
	    <th>Last Day</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${projects}" var="project">
      <tr>
        <td><a href="<%=request.getContextPath()%>/html/viewproject?id=${project.id}">${project.name}</a></td>
        <td>${project.projectCode}</td>
        <td>${project.projectTypeName}</td>
        <td>${project.statusName}</td>
        <td>${project.startDate}</td>        
        <td>${project.nextReviewDate}</td>
        <td>${project.nextFollowUpDate}</td>
        <td>${project.endDate}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  </div>
</body>
</html>
