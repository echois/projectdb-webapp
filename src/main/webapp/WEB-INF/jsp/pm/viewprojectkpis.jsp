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
      $("#kpis").tablesorter({sortList: [[1,1]], headers: {0: {sorter:false}}});
      var table = $("#kpis");
      table.bind("sortEnd",function() { 
          var i = 1;
          table.find("tr:gt(0)").each(function(){
              $(this).find("td:eq(0)").text(i);
              i++;
          });
      }); 
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <h3>Project KPIs (${f:length(projectKpis)})</h3>

  <table id="kpis" class="tablesorter">
    <thead>
      <tr>
	    <th>#</th>
	    <th>Date</th>
	    <th>KPI</th>
	    <th>Code</th>
	    <th>Value</th>
	    <th><nobr>Reported By</nobr></th>
	    <th><nobr>Project</nobr></th>
	    <th>Notes</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${projectKpis}" var="projectKpi">
      <tr>
        <td>&nbsp;</td>
        <td>${projectKpi.date}</td>
        <td><nobr>${projectKpi.kpiType}-${projectKpi.kpiId}: ${projectKpi.kpiTitle}</nobr></td>
        <td>${projectKpi.code}:${projectKpi.codeName}</td>
        <td><nobr>${projectKpi.value}</nobr></td>
        <td><nobr>${projectKpi.adviserName}</nobr></td>
        <td><a href="<%=request.getContextPath()%>/html/viewproject?id=${projectKpi.projectId}"><nobr>Go to project</nobr></a></td>
        <td>${projectKpi.notes}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  </div>
</body>
</html>
