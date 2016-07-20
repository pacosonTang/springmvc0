package com.springmvc.controller;

import org.jivesoftware.smack.chat.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.springmvc.pojo.MessageBody;
import com.springmvc.pojo.MyTextMessage;

@Controller
@Scope("singleton")
public class StompController {
	@Autowired private ChatController chatController;
	@Autowired public SimpMessagingTemplate template; 
	
	@MessageMapping("/sendtext") // 发送消息到特定用户.
	public void sendtext(MyTextMessage message) throws Exception {		
		System.out.println("access sendtext successfully.");
		String touser = message.getTouser();
		String curuser = message.getCuruser();
		String textmsg = message.getText();
		
		try {
			Chat chat = chatController.getChat(curuser, touser);
			if(chat==null) {
				return ;
			}
			chat.sendMessage(textmsg);
			template.convertAndSendToUser(curuser + "2" + touser, "/forwardtext", 
					new MyTextMessage(textmsg, null, null));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	 
	// 无需 stomp client 发送请求，而stomp server 返回接收到的msg(return chat log)
	@SubscribeMapping("/macro")
	public MessageBody handleSubscription(String body) {
		System.out.println("msg.body = " + body);
		MessageBody messageBody = new MessageBody(body);
		return messageBody;
	}
}