<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> <!-- 编码格式为jsp文件的存储格式 -->
<%@ page contentType="text/html;charset=UTF-8"%> <!--  -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>

<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html charset=UTF-8"> <!-- 浏览器解析jsp的编码 -->
	</head>
	
	<body>		
		<jsp:include page="header.jsp"></jsp:include>
		<c:if test="${username == null}">
			<a href="<c:url value="/login/loginform" />">登录</a> <br/>
		</c:if>
		<c:if test="${username != null}">
			<a href="<c:url value="/chat/chatroom" />">进入聊天室</a> <br/>
		</c:if>
		<a href="<c:url value="/chat/peertopeerchat" />">端对端聊天</a> <br/>
		<a href="<c:url value="/chat/groupchat" />">群组聊天</a> <br/>
		 
	</body>
</html>