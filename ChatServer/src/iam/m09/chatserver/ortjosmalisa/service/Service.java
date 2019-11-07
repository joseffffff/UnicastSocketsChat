package iam.m09.chatserver.ortjosmalisa.service;

import java.util.ArrayList;
import java.util.List;

import iam.m09.chatserver.ortjosmalisa.model.Chat;
import iam.m09.chatserver.ortjosmalisa.model.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Service {
	
	private final int PORT;
	private ServerSocket socketListener;
	private Thread threadNewUsersListener;
	private RunnableNewUsersListener newUsersListener;
	private List<User> connectedUsers;
	private List<Chat> chats;
	
	public Service (int port) throws IOException {
		this.PORT = port;
		connectedUsers = new ArrayList<User>();		
		chats = new ArrayList<Chat>();
		socketListener = new ServerSocket(PORT);
		newUsersListener = new RunnableNewUsersListener(socketListener, connectedUsers);
		threadNewUsersListener = new Thread(newUsersListener, "New User");
		//addFakeUsers();
	}
	
	public void reset() throws IOException {
		connectedUsers.removeAll(connectedUsers);
		chats.removeAll(chats);
	}

	public void startService() {
		threadNewUsersListener.start();
	}
	
	public void stopService() throws IOException {
		socketListener.close();
		newUsersListener.stop();
		reset();
	}
	
	public List<User> getConnectedUsers() {
		return connectedUsers;
	}
	
	public List<Chat> getChats() {
		return chats;
	}
	
	public void addFakeUsers() {
		
		try {
			User user = new User("testUser1", InetAddress.getByName("192.168.5.5"), 99999, null);
			User user2 = new User("testUser2", InetAddress.getByName("192.168.5.6"), 99999, null);
			User user3 = new User("testUser3", InetAddress.getByName("192.168.5.7"), 99999, null);
			
			connectedUsers.add(user);
			connectedUsers.add(user2);
			connectedUsers.add(user3);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
