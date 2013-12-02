<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <meta charset="utf-8">
  <script src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/common.css" type="text/css"/>  
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/jquery-ui.css" type="text/css"/>
  <script type="text/javascript">

  function beforeSubmit() {
	  alert($("#institution").html());
  }

    $(document).ready(function() {
    	
      // make fields visible/invisible dependent on applicants institution
      $("#institution").change(function() {
          if($("#institution").val() == "other") {
            $("#other_inst").css("display","inline");
          } else {
       	    $("#other_inst").css("display","none");     	
          }
        }
      );

      // make fields visible/invisible dependent on applicants institutionalRole
      $("#institutionalRole").change(function() {
    	  var institutionalRole = $("#institutionalRole option:selected").attr("id")
          if(institutionalRole == "other") {
            $("#other_institutionalRole").css("display","inline");
          } else {
       	    $("#other_institutionalRole").css("display","none");     	
          }
        }
      );
      
    });
  </script>
<style> 
.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
</head>

<body>

  <form:form method="POST" commandName="requestaccount" onsubmit="beforeSubmit()" action='requestaccount'>

  <!-- Applicants information -->
  
  <b>Tell us about yourself:</b>
  <hr/>
    <form:errors path="*" cssClass="errorblock" element="div"/>
    <table cellpadding="5">
    <tbody>
      <tr>
        <td>Full Name:</td>
        <td><form:input id="fullname" path="fullName" value="Joe Blogs"/></td>
      </tr>
      <tr>
        <td>Institution:</td>
        <td>
          <form:select id="institution" path="institution">
            <form:option value=''></form:option>
            <form:option value='landcare'>Landcare Research</form:option>
            <form:option value='niwa'>NIWA</form:option>
            <form:option value='uoa'>University of Auckland</form:option>
            <form:option value='uoc'>University of Canterbury</form:option>
            <form:option value='uoo'>University of Otago</form:option>
            <form:option value='other'>Other</form:option>
          </form:select>
          <div id="other_inst" style="display:none;">
            Please specify: <form:input id="other_institution" value="" path="institution"/>          
          </div>
        </td>
      </tr>
      <tr>
        <td>Faculty or Division:<br>(if applicable)</td>
        <td valign="top"><form:input id="division" path="division"/></td>
      </tr>
      <tr>
        <td>Department or School:<br>(if applicable)</td>
        <td valign="top"><form:input id="department" path="department"/></td>
      </tr>
      <tr>
        <td>Contact phone number:</td>
        <td><form:input id="phone" path="phone"/></td>
      </tr>
      <tr>
        <td>E-mail address:</td>
        <td><form:input id="email" path="email" value="joe@blogs.org"/></td>
      </tr>
      <tr>
        <td>Institutional role:<br>(at institution you named above)</td>
        <td valign="top">
          <form:select id="institutionalRole" path="institutionalRole">
            <form:option value=''></form:option>
            <form:option value='staff'>Staff or PostDoc</form:option>
            <form:option value='phd'>PhD Student</form:option>
            <form:option value='student'>Other Student</form:option>
            <form:option value='other'>Other</form:option>
          </form:select>
          <div id="other_institutionalRole" style="display:none;">
            Please specify: <form:input id="institutionalRole_other_value" value="" path="institutionalRole"/>          
          </div>
        </td>
      </tr>
    </tbody>
  </table>

  <br>
  <input type="submit" value="Next">
  
</form:form>

</body>