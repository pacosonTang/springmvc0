<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>端对端聊天</title>

<script src="<c:url value="/"/>resources/jquery/jquery.min.js"></script>
<script src="<c:url value="/"/>resources/strophe/strophe.min.js"></script>
<script src="<c:url value="/"/>resources/strophe/groupchat.js"></script>
</head>

<body>
	<jsp:include page="header.jsp"></jsp:include>
	JID：
	<input type="text" id="input-jid">
	<br> 密码：
	<input type="password" id="input-pwd">
	<br>
	<button id="btn-login">登录</button>
	<div id="msg" style="height: 400px; width: 400px; overflow: scroll;"></div>
	<br> 消息：
	<br>
	<textarea id="input-msg" cols="30" rows="4"></textarea>
	<br>
	<button id="btn-send">发送</button>
</body>
</html>