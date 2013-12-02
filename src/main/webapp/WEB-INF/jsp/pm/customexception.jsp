<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <meta charset="utf-8">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/style/common.css" type="text/css"/>  
</head>
<body>

<%@include file="includes/header.jsp" %>

  <div id="body">
  <br>
  <h2><font color="red">${exception.customMsg}</font></h2>
  If you think this is an error, please send a problem report to <a href="mailto:eresearch@nesi.org.nz?Subject=project%20database%20webapp%20authorization%20problem">eresearch@nesi.org.nz</a>
  </div>
  
</body>
</html>
