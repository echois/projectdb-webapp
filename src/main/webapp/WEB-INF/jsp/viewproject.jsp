<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="c-rt" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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
      $("#researcherTable").tablesorter();
      $("#adviserTable").tablesorter();
      $("#kpiTable").tablesorter();
      $("#researchOutputTable").tablesorter();
      $("#attachmentTable").tablesorter();
      $("#reviewTable").tablesorter();
      $("#followUpTable").tablesorter();
      $("#adviserActionTable").tablesorter();
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">

  <a href="<%=request.getContextPath()%>/html/editproject?id=${pw.project.id}">Edit</a> | 
  <a onclick="return (confirm('Are you sure you want to delete this project?'))" href="<%=request.getContextPath()%>/html/deleteproject?id=${pw.project.id}">Delete</a>
  
  <h3>Project: ${pw.project.name}</h3>
  
  <c:if test="${expired}">
  	<h1 style="text-align:center;color:red;">EXPIRED</h1>
  </c:if>

  <br>
  <h4>Project Details</h4>
  
  <table border="0" cellspacing="0" cellpadding="5">
    <tr>
      <td><nobr>Name:</nobr></td>
      <td>${pw.project.name}</td>
    </tr>
    <tr>
      <td valign="top"><nobr>Description:</nobr></td>
      <td>${pw.project.description}</td>
    </tr>
    <tr>
      <td><nobr>Host Institution:</nobr></td>
      <td>${pw.project.hostInstitution}</td>
    </tr>
    <tr>
      <td valign="top"><nobr>HPC Facilities:</nobr></td>
      <td>
        <c:forEach items="${pw.projectFacilities}" var="facility">
          ${facility.facilityName}<br>
        </c:forEach>
      </td>
    </tr>
    <tr>
      <td><nobr>Type:</nobr></td>
      <td>${pw.project.projectTypeName}</td>
    </tr>
    <tr>
      <td><nobr>Status:</nobr></td>
      <td>${pw.project.statusName}</td>
    </tr>
    <tr>
      <td><nobr>First Day:</nobr></td>
      <td>${pw.project.startDate}</td>
    </tr>
    <tr>
      <td><nobr>Next review:</nobr></td>
      <td>${pw.project.nextReviewDate}</td>
    </tr>
    <tr>
      <td><nobr>Next follow-up:</nobr></td>
      <td>${pw.project.nextFollowUpDate}</td>
    </tr>
    <tr>
      <td><nobr>Last Day:</nobr></td>
      <td>${pw.project.endDate}</td>
    </tr>
    <tr>
      <td><nobr>Project Code:</nobr></td>
      <td>${pw.project.projectCode}</td>
    </tr>
    <tr>
      <td valign="top">Requirements:</td>
      <td valign="top">${pw.project.requirements}</td>
    </tr>
    <tr>
      <td valign="top"><nobr>Notes:</nobr></td>
      <td>${pw.project.notes}</td>
    </tr>
    <tr>
      <td valign="top"><nobr>Todo:</nobr></td>
      <td>${pw.project.todo}</td>
    </tr>
  </table>

  <br>
  <h4>Researchers on project</h4>

  <table id="researcherTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Picture</th>
	    <th>Name</th>
   	    <th>Role on Project</th>
   	    <th>Institution</th>
   	    <th>Institutional Role</th>
   	    <th>Notes</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${pw.rpLinks}" var="rpLink">
        <tr>
          <td><a href="<%=request.getContextPath()%>/html/viewresearcher?id=${rpLink.researcherId}"><img src="${rpLink.researcher.pictureUrl}" width="60px"/></a></td>
          <td><a href="<%=request.getContextPath()%>/html/viewresearcher?id=${rpLink.researcherId}">${rpLink.researcher.fullName}</a></td>
          <td>${rpLink.researcherRoleName}</td>
          <td>${rpLink.researcher.institution}</td>
          <td>${rpLink.researcher.institutionalRoleName}</td>
          <td>${rpLink.notes}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <br>
  <h4>Advisers on project</h4>

  <table id="adviserTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Picture</th>
	    <th>Name</th>
   	    <th>Role on Project</th>
   	    <th>Notes</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${pw.apLinks}" var="apLink">
        <tr>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${apLink.adviserId}"><img src="${apLink.adviser.pictureUrl}" width="60px"/></a></td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${apLink.adviserId}">${apLink.adviser.fullName}</a></td>
          <td>${apLink.adviserRoleName}</td>
          <td>${apLink.notes}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <br>
  <h4>KPIs</h4>
  
  <table id="kpiTable" class="tablesorter">
    <thead>
      <tr>
        <th>Date</th>
	    <th>Reported By</th>
	    <th>KPI</th>
	    <th>Code</th>
	    <th>Value</th>
	    <th>Notes</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${pw.projectKpis}" var="projectKpi">
        <tr>
          <td>${projectKpi.date}</td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${projectKpi.adviserId}">${projectKpi.adviserName}</a></td>
          <td>${projectKpi.kpiType}-${projectKpi.kpiId}: ${projectKpi.kpiTitle}</td>
          <td>${projectKpi.code}:${projectKpi.codeName}</td>
          <td>${projectKpi.value}</td>
          <td>${projectKpi.notes}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <br>
  <h4>Research Output</h4>

  <table id="researchOutputTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Date</th>
	    <th>Reported By</th>
	    <th>Type</th>
	    <th>Citation</th>
	    <th>Link</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${pw.researchOutputs}" var="researchOutput">
        <tr>
          <td>${researchOutput.date}</td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${researchOutput.adviserId}">${researchOutput.adviserName}</a></td>
          <td>${researchOutput.type}</td>
          <td>${researchOutput.description}</td>
          <td><a target="new" href="${researchOutput.link}">${researchOutput.link}</a></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

 
  <br>
  <h4>Reviews</h4>

  <table id="reviewTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Date</th>
	    <th>Reported By</th>
   	    <th>Notes</th>
   	    <th>Attachments</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${pw.reviews}" var="review">
        <tr>
          <td>${review.date}</td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${review.adviserId}">${review.adviserName}</a></td>
          <td>${review.notes}</td>
          <td>
          <c:forEach items="${review.attachments}" var="attachment">
            <c:if test="${attachment.reviewId eq review.id}">
              <b>Link</b>:<br>
              <a href="${attachment.link}">${attachment.link}</a><br>
              <b>Description</b>:<br>
              ${attachment.description}
              <hr/>
            </c:if>
          </c:forEach>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <br>
  <h4>Follow-ups</h4>

  <table id="followUpTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Date</th>
	    <th>Reported By</th>
   	    <th>Notes</th>
   	    <th>Attachments</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${pw.followUps}" var="followUp">
        <tr>
          <td>${followUp.date}</td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${followUp.adviserId}">${followUp.adviserName}</a></td>
          <td>${followUp.notes}</td>
          <td>
          <c:forEach items="${followUp.attachments}" var="attachment">
            <c:if test="${attachment.followUpId eq followUp.id}">
              <b>Link</b>:<br>
              <a href="${attachment.link}">${attachment.link}</a><br>
              <b>Description</b>:<br>
              ${attachment.description}<br>
              <hr/>
            </c:if>
          </c:forEach>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <br>
  <h4>Adviser Actions</h4>

  <table id="adviserActionTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Date</th>
	    <th>Reported By</th>
   	    <th>Action</th>
   	    <th>Attachments</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${pw.adviserActions}" var="adviserAction">
        <tr>
          <td>${adviserAction.date}</td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${adviserAction.adviserId}">${adviserAction.adviserName}</a></td>
          <td>${adviserAction.action}</td>
          <td>
          <c:forEach items="${adviserAction.attachments}" var="attachment">
            <c:if test="${attachment.adviserActionId eq adviserAction.id}">
              <b>Link</b>:<br>
              <a href="${attachment.link}">${attachment.link}</a><br>
              <b>Description</b>:<br>
              ${attachment.description}<br>
              <hr/>              
            </c:if>
          </c:forEach>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  </div>
</body>
</html>
