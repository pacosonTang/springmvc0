package com.springmvc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springmvc.pojo.ChatBase;

// this class encapsulates some info for
// connection, login, creating chat.
@Controller
@Scope("singleton")
@RequestMapping(value = "/login")
public class LoginController {
	private HashMap<String, ChatBase> chatBaseCollection = new HashMap<>(200); 
	@Autowired private ChatController chatController;
	
	public LoginController() { }
	
	// login route.
	@RequestMapping(value = "/loginform", method = RequestMethod.GET)
	public String showLoginForm(HttpSession session) {
		if(session.getAttribute("curuser") != null) {
			return "forward:/chat/chatroom";
		}
		return "login";
	}
	
	// logout route for post request.
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		String curuser = (String)session.getAttribute("curuser");
		ChatBase single = chatBaseCollection.get(curuser);
		if(single != null) {
			single.disconnect();
		}
		setAllAttributesNull(session);
		chatBaseCollection.remove(curuser);
		return "forward:/login/loginform";
 	}
	
	// login route for post request.
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(
			@RequestParam String username, 
			@RequestParam String password,
			HttpSession session) {
		ChatBase single = new ChatBase(username, password);
		try {
			single.connectAndLogin();
			session.setAttribute("curuser", username);
			chatBaseCollection.put(username, single);
			System.out.println("collection size = " + chatBaseCollection.size());
			return "redirect:/chat/chatroom/";
			// 遇到 post 转 get 使用 redirect，其他用 forward请求转发.
		} catch (Exception e) {
			e.printStackTrace();
			setAllAttributesNull(session);
			chatBaseCollection.remove(username);
			return "redirect:/login/loginform";
		}  
 	}
	
	/**
	 * get chat timestamp, also time recoded when the msg starts to send.
	 * @param msg 
	 * @return timestamp.
	 */
	public String getChatTimestamp(Message msg) {
		ExtensionElement delay = DelayInformation.from(msg);
		
		if(delay == null) {
			return null;
		}
		Date date = ((DelayInformation) delay).getStamp();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return format.format(date);
	}
	
	// set all attributes(including attributes of session) null.
	public void setAllAttributesNull(HttpSession session) {		
		Enumeration<String> names = session.getAttributeNames();
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println("name = " + name);
			session.setAttribute(name, null);
		}
		chatController.setStatuslist(null);
	}
	
	public ChatBase getSingleChatBase(String key) {
		return chatBaseCollection.get(key);
	}
}
