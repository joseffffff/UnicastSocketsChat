package iam.m09.chatserver.ortjosmalisa.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

import iam.m09.chatserver.ortjosmalisa.model.User;

public class RunnableNewUsersListener implements Runnable{
	
	private ServerSocket socketListener;
	private boolean running;
	
	private List<User> connectedUsers;
	
	public RunnableNewUsersListener(ServerSocket socketListener, List<User> connectedUsers) {
		this.socketListener = socketListener;
		this.connectedUsers = connectedUsers;
	}

	@Override
	public void run() {
		running = true;
		try {

			while (running) {
				Socket socket2Send = socketListener.accept();
				System.out.println(socket2Send.getInetAddress() + " connected. Port: " + socket2Send.getPort());
				
				(new Thread(new InConnectionWithClientRunnable(socket2Send, connectedUsers))).start();
			}
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Finished");
		}
		
	}
	
	public void stop() {
		running = false;
	}

}
