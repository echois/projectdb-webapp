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
      $("#output").tablesorter({sortList: [[1,1]], headers: {0: {sorter:false}}});
      var table = $("#output");
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
  
  <h3>Research output (${f:length(researchOutputs)})</h3>

  <table id="output" class="tablesorter">
    <thead>
      <tr>
	    <th>#</th>
	    <th>Date</th>
	    <th>Type</th>
	    <th>Citation</th>
	    <th>Link</th>
	    <th>Project</th>
	    <th><nobr>Reported By</nobr></th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${researchOutputs}" var="researchOutput">
      <tr>
        <td>&nbsp;</td>
        <td>${researchOutput.date}</td>
        <td><nobr>${researchOutput.type}</nobr></td>
        <td>${researchOutput.description}</td>
        <td>${researchOutput.link}</td>
        <td><a href="<%=request.getContextPath()%>/html/viewproject?id=${researchOutput.projectId}"><nobr>Go to project</nobr></a></td>
        <td>${researchOutput.adviserName}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  </div>
</body>
</html>
