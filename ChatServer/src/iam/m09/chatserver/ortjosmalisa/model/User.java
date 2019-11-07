package iam.m09.chatserver.ortjosmalisa.model;

import java.net.InetAddress;
import java.net.Socket;

public class User {
	
	private String name;
	private InetAddress ip;
	private int port;
	private Socket socket;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String name, InetAddress ip, int port, Socket socket) {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.socket = socket;
	}
	
	public User(String name, Socket socket) {
		this.name = name;
		this.socket = socket;
		this.ip = socket.getInetAddress();
		this.port = socket.getPort();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getTextForConnectedArea() {
		return name + " " + ip.toString() + ":" + port;
	}

}
