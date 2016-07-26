<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!-- 编码格式为jsp文件的存储格式 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<!--  -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="zh-CN">
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
	<title>聊天室</title>
	<link href="<c:url value="/"/>bootstrap/css/bootstrap.min.css" rel="stylesheet">
	
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<c:url value="/"/>bootstrap/jquery/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<c:url value="/"/>bootstrap/js/bootstrap.min.js"></script>
    <script src="<c:url value="/resources/sockjs-1.1.1.js" />"></script>
    <script src="<c:url value="/resources/stomp.js" />"></script>
    
	<script type="text/javascript">		
		$(document).ready(function(){
			$("#first").hide();
			
			// trigger event enterkey typed.
			$("input[id='send']").keydown(function(event){ 
				if(event.which == "13") {					
					sendTextByStomp();
				}
			});
			checkoutUserlist();
			connectWithStomp();			
		});
		
		// checkout username and isAvailable info.
		function checkoutUserlist(){
			curuser = '${curuser}';
			statuslist = ${statuslist};						
			for(var i=0; i<statuslist.length; i++) {				
				appendAIntoDiv("second_userlist", statuslist[i].isAvailabe, statuslist[i].name);
			}
		} // checkout over.
		
		// append <a> into a div.
		function appendAIntoDiv(objId, isAvailable, name) {
			var imgurl = "<c:url value='/image/status" + isAvailable + ".gif'/>";
			var funcParam = "selectToUser('" + name + "')";
			$("#"+objId).append("<a href=javascript:" + funcParam 
					+ " class='list-group-item'>&nbsp;&nbsp;<img src='" + imgurl + "' />"+ "&nbsp;" + name +"</a>");
		} // append over.
		
		// connect with stomp server and subscribe all points.
		function connectWithStomp() {
            var socket = new SockJS("<c:url value='/stompServer'/>");  
            stompClient = Stomp.over(socket);  
            stompClient.connect({}, function(frame) {  
                console.log('Connected: ' + frame);
                // subscribe all points.
                for(var i=0; i<statuslist.length; i++) {				
                	subscribePoint(statuslist[i].name+ '2' + curuser);
    			} 
            }); 
        } // connect over.
    
        // subscribe point.
        function subscribePoint(username) {
        	 stompClient.subscribe('/user/' + username +'/forwardtext',function(msg){             	
                 alert("info arrived from server.");
             }); 
        } // subscribe over.
        
        function selectToUser(touser) {
        	myToUser = touser;
        	$("h3[id='touser']").empty();
        	$("h3[id='touser']").append("与" + touser + "对话中");
        	$("input[id='send']").focus();
        }
        
        // send text via stomp.
		function sendTextByStomp() {
        	alert(sendTextByStomp);
			var msg = $("input[id='send']").val();			
			alert(myToUser);
	        stompClient.send("/app/sendtext", {}, JSON.stringify({
	        	'curuser': curuser,
	        	'text': msg,
	        	'touser': myToUser})
			);
		} // send over.
		
	</script>
</head>

<body>	
	<jsp:include page="header.jsp" />
	<div align="center"><table>
		<tr valign="top">
			<td>
				<div class="panel panel-success" style="width: 550px;">
				  <div class="panel-heading">
				    <h3 id="touser" class="panel-title">
				    	<c:if test="${touser != null}">
				    		与&nbsp;${touser }&nbsp;对话中
			    		</c:if>
			    	</h3> 
				  </div>
				  <div id="panel_chat" class="panel-footer" style="height: 450px;overflow: scroll;">
						<span class="label label-info" style="float: right;">mine info</span><br/>
						<span class="label label-info" style="float: left;">the other info</span><br/>
				  </div>
				  
				  <div class="panel-body">
				    <div class="input-group">
					  <input id="send" type="text" class="form-control" aria-describedby="basic-addon2" 
					  		 style="width:530px;height: 80px;padding: 5px;">
					</div>
						<button type="button" class="btn btn-default" style="float: left" onclick="javascript:sendPicture();">图片</button>
						<button type="button" class="btn btn-default" style="float: left" onclick="javascript:sendFile();">文件</button>
						<button type="button" class="btn btn-default" style="float: right" onclick="javascript:sendTextByStomp();">发送</button>
				  </div>
				</div>
			</td>
			<td>
				<div class="panel panel-success" style="width: 251px;height: 550px;">
					  
					  <div class="panel-heading">
					  	<div class="input-group">
						  <input id="search" type="text" class="form-control" placeholder="查找联系人或群" aria-describedby="basic-addon2">
						  <span class="input-group-btn">
					        <button class="btn btn-default" type="button">搜索</button>
					      </span>
						</div>
					  </div>
					  
					  <div class="panel-body">
					    <!-- first or second displayed -->
					    <div id="first0" class="btn-group btn-group-justified" role="group" aria-label="Justified button group">
							<a href="#" class="btn btn-default" role="button">联系人</a> 
							<a href="#" class="btn btn-default" role="button">群</a> 
						</div>
						<div id="first1">
							联系人列表 or 群组列表
						</div>
						
						<div id="second">
							<div class="list-group" id="second_userlist">
							  <a href="#" class="list-group-item active">用户列表</a>							  							  
							</div>
						</div>
					  </div>
				</div>
			</td>
		</tr>
	</table></div>
	
</body>
</html>