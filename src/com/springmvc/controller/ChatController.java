package com.springmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpSession;

import org.jivesoftware.smack.chat.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.springmvc.dto.UserStatus;
import com.springmvc.pojo.ChatBase;
import com.springmvc.repo.ChatRepository;
import com.springmvc.util.MyConstants;

@Controller
@Scope("singleton")
@RequestMapping(value = "/chat")
public class ChatController {
	private ChatRepository repository;
	@Autowired 	private LoginController loginController;
	private List<UserStatus> statuslist;
	
	@Autowired 	
	public ChatController(ChatRepository repository) {
		this.repository = repository;		
	}
	
	// access to chat room.
	@RequestMapping(value="/chatroom", method=RequestMethod.GET)
	public String showChatroom(HttpSession session) {
		if(session.getAttribute("curuser") == null) {
			return "redirect:/login/loginform";
		}
		
		// checkout username list.
		if(statuslist == null) {
			checkoutUserlist(session);
		}
		System.out.println(session);
		return "chatroom";
	}
	
	// not ajax requests for user list (by json format).
	public void checkoutUserlist(HttpSession session) {
		List<String> namelist = repository.checkoutUserlist();
		statuslist = new ArrayList<>(namelist.size());
		String from = (String) session.getAttribute("curuser");
		
		for (String to : namelist) {
			String isAvailabe = String.valueOf(isUserAvaible(from,to));
			UserStatus status = new UserStatus(isAvailabe, to);
			statuslist.add(status);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json_result = mapper.writeValueAsString(statuslist);
			session.setAttribute("statuslist", json_result);		
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("statuslist.size = " + statuslist.size());
	}
	
	// create single chat.
	@RequestMapping(value = "/single", method=RequestMethod.GET)
	public String createSingleChat(
			@RequestParam String touser,
			HttpSession session) {
		System.out.println("executing createSingleChat " + touser);
		
		String curuser = (String)session.getAttribute("curuser");
		Chat chat = getChat(curuser, touser);		
		if(chat == null) {
			return "redirect:/login/loginform";
		}
		session.setAttribute("touser", touser);
		return "redirect:/chat/chatroom";
	}
	
	/**
	 * judge whether the user identified with to is available or not.
	 * @param from the sender's jid.
	 * @param to the receiver's jid.
	 * @return 1 as true, 0 as false.
	 */
	public int isUserAvaible(String from, String to) {
		String urlName = MyConstants.buildPresenceURL(from, to, "text"); 

		try {
			URL url = new URL(urlName);
			URLConnection connection = url.openConnection();
			
			connection.connect();
			Scanner in = new Scanner(connection.getInputStream());
			String isAvailable = in.nextLine(); 
			return isAvailable.equals("available") ? 1 : 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// get chat from ChatBase.chatBaseCollection(curuser).chatCollection(touser)
	public Chat getChat(String curuser, String touser) {
		ChatBase single = loginController.getSingleChatBase(curuser);
		if(single==null) {
			return null;
		}
		Chat chat = single.getChat(touser); 
		if(chat == null) {
			chat = single.createChat(touser);
		}
		return chat;  
	}

	public void setStatuslist(List<UserStatus> statuslist) {
		this.statuslist = statuslist;
	}
}
