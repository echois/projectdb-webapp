<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <script src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery.tablesorter.min.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery.watermark.min.js"></script>
  <script src="<%=request.getContextPath()%>/js/util.js"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/common.css" type="text/css"/>  
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/jquery-ui.css" type="text/css"/>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/tablesorter/blue/style.css" type="text/css"/>
  <script>
  
    setInterval(function() {
    	var secondsLeft = parseInt($('#secondsLeft').text());
        $('#secondsLeft').text((secondsLeft < 1) ? 0 : (secondsLeft - 1));
      }, 1000);

    $(function() {
    	$("#researcherTable").tablesorter();
        $("#datepicker1").datepicker({ dateFormat: "yy-mm-dd" });
        $("#datepicker2").datepicker({ dateFormat: "yy-mm-dd" });
        $("#datepicker3").datepicker({ dateFormat: "yy-mm-dd" });
        $("#datepicker4").datepicker({ dateFormat: "yy-mm-dd" });

        $("#project_name").watermark("Name of the project");
        $("#project_description").watermark("Description of the scientific background and goals of the project. Please also add a paragraph how the use of HPC facilities are beneficial to the project");
        $("#project_requirements").watermark("Potential things to add here are required disk space, help with scripting, help with code parallelization, etc");
        $("#project_notes").watermark("Other notes on the project");
        $("#project_hostInstitution").watermark("Institution that administers the research project. If in doubt, this is the institution where the PI is working.");
        $("#project_todo").watermark("Things that need to be done on this project");

        $('.update').click(function() {
        	if ($(this).hasClass("delete")) {
				if (!confirm('Are you sure you want to delete?')) {
					return false;
				}
        	}
        	$('#operation').val('UPDATE');
        	$('#redirect').val($(this).val());
            $('#form').submit();
        });
        $('.saveAndContinueEditing').click(function() {
        	$('#operation').val('SAVE_AND_CONTINUE_EDITING');
            $('#form').submit();
        });
        $('.saveAndFinishEditing').click(function() {
        	$('#operation').val('SAVE_AND_FINISH_EDITING');
        	$('#redirect').val($(this).val());
            $('#form').submit();
        });
        $('.reset').click(function() {
        	$('#operation').val('RESET');
            $('#form').submit();
        });
        $('.cancel').click(function() {
        	$('#operation').val('CANCEL');
            $('#form').submit();
        });
    });
  </script>
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  
  <c:choose>
    <c:when test="${projectWrapper.project.id < 0}">
      <h3>Create Project</h3>
    </c:when>
    <c:otherwise>
      <h3>Edit Project</h3>
    </c:otherwise>
  </c:choose>
  <br>
  
  <form:form id="form" method="post" modelAttribute="projectWrapper" commandName="projectWrapper">

  <button class="saveAndFinishEditing" value="viewproject?id=${projectWrapper.project.id}">Save &amp; Finish Editing</button>
  <button class="saveAndContinueEditing">Save &amp; Continue Editing</button>
  <button class="reset">Reset</button>
  <button class="cancel">Cancel</button><br>
  
  <br>
  Seconds left in session: <span id="secondsLeft">${projectWrapper.secondsLeft}</span>
  
  <c:if test="${not empty projectWrapper.errorMessage}">
      <br><br>
      <font color="red"><b>${projectWrapper.errorMessage}</b></font>
  </c:if>

  <form:hidden id="operation" path="operation"/>
  <form:hidden id="redirect" path="redirect"/>
  <form:hidden id="project.id" path="project.id"/>
  
  <br>
  <h4>Details</h4>
    
  <table border="0" cellpadding="3">
    <tr>
      <td>Name:</td>
      <td>&nbsp;</td>
      <td><form:input id="project_name" path="project.name" size="120"/></td>
    </tr>
    <tr>
      <td valign="top">Description:</td>
      <td valign="top">&nbsp;</td>
      <td><form:textarea id="project_description" path="project.description" rows="10" cols="104"/></td>
    </tr>
    <tr>
      <td>Host Institution:</td>
      <td>&nbsp;</td>
      <td><form:select path="project.hostInstitution" items="${institutions}"/></td>
    </tr>
    <tr>
      <td>Type:</td>
      <td>&nbsp;</td>
      <td><form:select path="project.projectTypeId" items="${projectTypes}"/></td>
    </tr>
    <tr>
      <td>Status:</td>
      <td>&nbsp;</td>
      <td><form:select path="project.statusId" items="${statuses}"/></td>
    </tr>
    <tr>
      <td>First Day:</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker1" path="project.startDate" size="20"/></td>
    </tr>
    <tr>
      <td>Next review date:</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker2" path="project.nextReviewDate" size="20"/></td>
    </tr>
    <tr>
      <td>Next follow-up date:</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker3" path="project.nextFollowUpDate" size="20"/></td>
    </tr>
    <tr>
      <td>Last Day:</td>
      <td>&nbsp;</td>
      <td><form:input id="datepicker4" path="project.endDate" size="20"/></td>
    </tr>
    <tr>
      <td valign="top">Requirements:</td>
      <td valign="top">&nbsp;</td>
      <td><form:textarea id="project_requirements" path="project.requirements" rows="5" cols="104"/></td>
    </tr>
    <tr>
      <td valign="top">Notes:</td>
      <td valign="top">&nbsp;</td>
      <td><form:textarea id="project_notes" path="project.notes" rows="5" cols="104"/></td>
    </tr>
    <tr>
      <td valign="top">Todo:</td>
      <td valign="top">&nbsp;</td>
      <td><form:textarea id="project_todo" path="project.todo" rows="5" cols="104"/></td>
    </tr>
  </table>
  
  <br>
  <a id="researchers"></a>
  <h4>Researchers on project</h4>

  <table id="researcherTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Picture</th>
	    <th>Name</th>
   	    <th>Role on Project</th>
   	    <th>Institution</th>
   	    <th>Institutional role</th>
   	    <th>Notes</th>
   	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.rpLinks}" var="rpLink" varStatus="loop">
        <tr>
          <td><a href="<%=request.getContextPath()%>/html/viewresearcher?id=${rpLink.researcherId}"><img src="${rpLink.researcher.pictureUrl}" width="60px"/></a></td>
          <td><a href="<%=request.getContextPath()%>/html/viewresearcher?id=${rpLink.researcherId}">${rpLink.researcher.fullName}</a></td>
          <td>${rpLink.researcherRoleName}</td>
          <td>${rpLink.researcher.institution}</td>
          <td>${rpLink.researcher.institutionalRoleName}</td>
          <td>${rpLink.notes}</td>
          <td>
          	<button class="update" value="editrplink?projectId=${projectWrapper.project.id}&rid=${rpLink.researcherId}">Edit</button>
             <button class="update delete" value="deleterplink?projectId=${projectWrapper.project.id}&rid=${rpLink.researcherId}">Delete researcher from project</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <button class="update" value="editrplink?projectId=${projectWrapper.project.id}">Add researcher to project</button><br>


  <br>
  <a id="advisers"></a>
  <h4>Advisers on project</h4>
  <b><font color="orange">Note: If you are not an adviser on a project you may not be able to edit it after it has been saved.</font></b>
  <table id="adviserTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Picture</th>
	    <th>Name</th>
   	    <th>Role on Project</th>
   	    <th>Notes</th>
   	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.apLinks}" var="apLink" varStatus="loop">
        <tr>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${apLink.adviserId}"><img src="${apLink.adviser.pictureUrl}" width="60px"/></a></td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${apLink.adviserId}">${apLink.adviser.fullName}</a></td>
          <td>${apLink.adviserRoleName}</td>
          <td>${apLink.notes}</td>
          <td>
          	 <button class="update" value="editaplink?projectId=${projectWrapper.project.id}&aid=${apLink.adviserId}">Edit</button>
             <button class="update delete" value="deleteaplink?projectId=${projectWrapper.project.id}&aid=${apLink.adviserId}">Delete adviser from project</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
 
  <button class="update" value="editaplink?projectId=${projectWrapper.project.id}">Add adviser to project</button><br>  
 
  <br>
  
  <a id="facilities"></a>
  <h4>HPC Facilities</h4>

  <table id="facilityTable" class="tablesorter">
    <thead>
      <tr>
   	    <th>HPC Facility</th>
   	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.projectFacilities}" var="projectFacility">
        <tr>
          <td>${projectFacility.facilityName}</td>
          <td>
          	<button class="update" value="editprojectfacility?fid=${projectFacility.facilityId}&projectId=${projectWrapper.project.id}">Edit</button>
            <button class="update delete" value="deleteprojectfacility?fid=${projectFacility.facilityId}&projectId=${projectWrapper.project.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <button class="update" value="editprojectfacility?projectId=${projectWrapper.project.id}">Add HPC facility</button><br>
  <br>
  
  <a id="kpis"></a>
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
	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.projectKpis}" var="projectKpi">
        <tr>
          <td>${projectKpi.date}</td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${projectKpi.adviserId}">${projectKpi.adviserName}</a></td>
          <td>${projectKpi.kpiType}-${projectKpi.kpiId}: ${projectKpi.kpiTitle}</td>
          <td>${projectKpi.code}:${projectKpi.codeName}</td>
          <td>${projectKpi.value}</td>
          <td>${projectKpi.notes}</td>
          <td>
          	<button class="update" value="editprojectkpi?id=${projectKpi.id}&projectId=${projectWrapper.project.id}">Edit</button>
            <button class="update delete" value="deleteprojectkpi?id=${projectKpi.id}&projectId=${projectWrapper.project.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  
  <button class="update" value="editprojectkpi?projectId=${projectWrapper.project.id}">Add KPI</button><br>  
  
  <br>
  <a id="outputs"></a>
  <h4>Research Output</h4>

  <table id="researchOutputTable" class="tablesorter">
    <thead>
      <tr>
        <th>Date</th>
	    <th>Reported By</th>
	    <th>Type</th>
	    <th>Citation</th>
	    <th>Link</th>
   	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.researchOutputs}" var="researchOutput">
        <tr>
          <td>${researchOutput.date}</td>
          <td><a href="<%=request.getContextPath()%>/html/viewadviser?id=${researchOutput.adviserId}">${researchOutput.adviserName}</a></td>
          <td>${researchOutput.type}</td>
          <td>${researchOutput.description}</td>
          <td><a target="new" href="${researchOutput.link}">${researchOutput.link}</a></td>
          <td>
          	<button class="update" value="editresearchoutput?id=${researchOutput.id}&projectId=${projectWrapper.project.id}">Edit</button>
            <button class="update delete" value="deleteresearchoutput?id=${researchOutput.id}&projectId=${projectWrapper.project.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <button class="update" value="editresearchoutput?projectId=${projectWrapper.project.id}">Add research output</button><br>
 
  <br>
  <a id="reviews"></a>
  <h4>Reviews</h4>
    
  <table id="reviewTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Date</th>
	    <th>Reported By</th>
   	    <th>Notes</th>
   	    <th>Attachments</th>
   	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.reviews}" var="review">
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
              ${attachment.description}<br>
              <button class="update" value="editattachment?id=${attachment.id}&type=reviews&typeId=${review.id}&projectId=${projectWrapper.project.id}">Edit attachment</button>
              <button class="update delete" value="deleteattachment?id=${attachment.id}&type=reviews&typeId=${review.id}&projectId=${projectWrapper.project.id}">Delete attachment</button>
              <hr/>
            </c:if>
          </c:forEach>
          <button class="update" value="editattachment?projectId=${projectWrapper.project.id}&type=reviews&typeId=${review.id}">Add attachment</button>
          </td>
          <td>
          	<button class="update" value="editreview?id=${review.id}&projectId=${projectWrapper.project.id}">Edit</button>
            <button class="update delete" value="deletereview?id=${review.id}&projectId=${projectWrapper.project.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <button class="update" value="editreview?projectId=${projectWrapper.project.id}">Add review</button><br>

  <br>
  <a id="followups"></a>
  <h4>Follow-ups</h4>

  <table id="followUpTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Date</th>
	    <th>Reported By</th>
   	    <th>Notes</th>
   	    <th>Attachments</th>
   	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.followUps}" var="followUp">
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
              <button class="update" value="editattachment?id=${attachment.id}&type=followups&typeId=${followUp.id}&projectId=${projectWrapper.project.id}">Edit attachment</button>
              <button class="update delete" value="deleteattachment?attachmentId=${attachment.id}&projectId=${projectWrapper.project.id}">Delete attachment</button>
              <hr/>
            </c:if>
          </c:forEach>
          <button class="update" value="editattachment?type=followups&typeId=${followUp.id}&projectId=${projectWrapper.project.id}">Add attachment</button>
          </td>
          <td>
          	<button class="update" value="editfollowup?id=${followUp.id}&projectId=${projectWrapper.project.id}">Edit</button>
            <button class="update delete" value="deletefollowup?id=${followUp.id}&projectId=${projectWrapper.project.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <button class="update" value="editfollowup?projectId=${projectWrapper.project.id}">Add follow-up</button><br>

  <br>
  <a id="adviseractions"></a>
  <h4>Adviser Actions</h4>

  <table id="adviserActionTable" class="tablesorter">
    <thead>
      <tr>
	    <th>Date</th>
	    <th>Reported By</th>
   	    <th>Adviser Action</th>
   	    <th>Attachments</th>
   	    <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${projectWrapper.adviserActions}" var="adviserAction">
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
              <button class="update" value="editattachment?id=${attachment.id}&type=adviseractions&typeId=${adviserAction.id}&projectId=${projectWrapper.project.id}">Edit attachment</button>
              <button class="update delete" value="deleteattachment?attachmentId=${attachment.id}&projectId=${projectWrapper.project.id}">Delete attachment</button>
              <hr/>
            </c:if>
          </c:forEach>
          <button class="update" value="editattachment?type=adviseractions&typeId=${adviserAction.id}&projectId=${projectWrapper.project.id}">Add attachment</button>
          </td>
          <td>
          	<button class="update" value="editadviseraction?id=${adviserAction.id}&projectId=${projectWrapper.project.id}">Edit</button>
            <button class="update delete" value="deleteadviseraction?id=${adviserAction.id}&projectId=${projectWrapper.project.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <button class="update" value="editadviseraction?projectId=${projectWrapper.project.id}">Add adviser action</button><br>

  <br>

  <button class="saveAndFinishEditing" value="viewproject?id=${projectWrapper.project.id}">Save &amp; Finish Editing</button>
  <button class="saveAndContinueEditing">Save &amp; Continue Editing</button>
  <button class="reset">Reset</button>
  <button class="cancel">Cancel</button>
  
  </form:form>

  </div>
    
</body>
</html>
