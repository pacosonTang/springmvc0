package com.springmvc.pojo;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.springmvc.component.MyChatManagerListener;
import com.springmvc.util.MyConstants;

public class ChatBase {
	private XMPPTCPConnectionConfiguration conf;
	private AbstractXMPPConnection connection;
	private ChatManager chatManager;
	private String username;
	private String password;
	private HashMap<String, Chat> chatCollection = new HashMap<>(2);
	
	public ChatBase(String... args) {
		init(args);
	}
	
	/**
	 * @param args refers to an array with ordered values as follows: user, password, host, port.
	 */
	public void init (String... args) {
		username = args[0];
		password = args[1];
		conf = getConf();
		connection = getConnection();
		chatManager = getChatManager();
	}
	
	/**
	 * connect to and login in openfire server.
	 * @throws XMPPException 
	 * @throws IOException 
	 * @throws SmackException 
	 */
	public void connectAndLogin() throws SmackException, IOException, XMPPException {
		System.out.println("executing connectAndLogin method.");
		System.out.println("connection = " + connection);
		if(!connection.isConnected()) {
			connection.connect();
		}
		System.out.println("successfully connection.");
		connection.login(); // client logins into openfire server.
		System.out.println("successfully login.");
	}
	
	/**
	 * disconnect to and logout from openfire server.
	 * @throws XMPPException 
	 * @throws IOException 
	 * @throws SmackException 
	 */
	public void disconnect() {
		System.out.println("executing disconnect method.");
		try {
			if(connection != null && connection.isConnected()) {
				connection.disconnect();
				System.out.println("connection.disconnect()");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// a series of getXXX methods.
	public XMPPTCPConnectionConfiguration getConf() {
		if(conf == null) {
			conf = XMPPTCPConnectionConfiguration.builder().setUsernameAndPassword(username, password)
					.setServiceName(MyConstants.HOST)
					.setHost(MyConstants.HOST)
					.setPort(Integer.valueOf(MyConstants.PORT))
					.setSecurityMode(SecurityMode.disabled) // (attention of this line about SSL authentication.)
					.build();
		}
		return conf;
	}

	public AbstractXMPPConnection getConnection() {
		if(connection == null) {
			if(conf == null) {
				conf = getConf();
			}
			connection = new XMPPTCPConnection(conf);
		}
		return connection;
	}

	private ChatManager getChatManager() {
		if(chatManager == null) {
			if(connection == null) {
				connection = getConnection();
			}
			chatManager = ChatManager.getInstanceFor(connection); 
			chatManager.addChatListener(new MyChatManagerListener());
		}
		return chatManager;
	}

	// create a chat instance with toUser as input parameter.
	public Chat createChat(String touser) {
		if(username==null || password==null) {
			return null;
		}
		touser += "@" + MyConstants.HOST;
		Chat chat = getChatManager().createChat(touser);
		chatCollection.put(touser, chat);
		return chat;
	}
	
	public Chat getChat(String touser) {
		return chatCollection.get(touser); 
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
