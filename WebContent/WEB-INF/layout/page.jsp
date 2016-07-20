<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page session="false" %>
<html>
  <head>
    <title>AsiaInfo Smack Communication System</title>
    <link rel="stylesheet" 
          type="text/css" 
          href="<s:url value="/resources/style.css" />" >
  </head>
  <body>
    <div id="header">
      <t:insertAttribute name="header" />
    </div>
    <div id="content">
      <t:insertAttribute name="body" />
    </div>
    <div id="footer">
      <t:insertAttribute name="footer" />
    </div>
  </body>
</html>
