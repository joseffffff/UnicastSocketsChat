package iam.m09.chatserver.ortjosmalisa.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
	
	private User user1;
	private User user2;
	
	private List<Message> msgs;
	
	public Chat() {
		msgs = new ArrayList<Message>();
	}
	
	public Chat(User user1, User user2) {
		this();
		this.user1 = user1;
		this.user2 = user2;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public List<Message> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Message> msgs) {
		this.msgs = msgs;
	}

	
}
