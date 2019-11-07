package iam.m09.chatserver.ortjosmalisa.model;

public class Message {
	
	private User sender;
	private String msg;
	
	public Message() {
		
	}
	
	public Message(User sender, String msg) {
		this.sender = sender;
		this.msg = msg;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
