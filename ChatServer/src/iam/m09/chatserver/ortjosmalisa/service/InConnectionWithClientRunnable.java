package iam.m09.chatserver.ortjosmalisa.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import iam.m09.chatserver.ortjosmalisa.model.User;

public class InConnectionWithClientRunnable implements Runnable{

	private Socket socket2Send;
	private List<User> connectedUsers;
	private User user;
	
	public InConnectionWithClientRunnable(Socket socket2Send,  List<User> connectedUsers) {
		this.socket2Send = socket2Send;
		this.connectedUsers = connectedUsers;	
	}
	
	@Override
	public void run() {

		System.out.println("usersize inconnection: " + connectedUsers.size());
		
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(socket2Send.getInputStream()));
			PrintWriter out = new PrintWriter(socket2Send.getOutputStream(), true);

			String line;

			while ((line = in.readLine()) != null) {
				
				System.out.println("Recieved: " + line);

				String response = getCommandResponse(line);
				
				out.println(response);
				
				System.out.println("Sended: " + response);
			}
			
			connectedUsers.remove(user);
			System.out.println(socket2Send.getInetAddress() + " disconnected.");
			
			out.close();
			in.close();

		} catch (IOException e) {
			System.out.println("Connection fail. ");
		} 
		
	}
	

	
	private String getCommandResponse(String command) {
		
		/*
		 * Separated by &
		 * name:value -> utilizado al connectar
		 * getUsersConnecteds 
		 * sender:value
		 * reciever:value
		 * msg:value
		 */
		
		String wordToReturn = "";
		
		String[] words = command.trim().split(":");
		
		switch (words[0]) {
		
			case "name": //name:name (login)
				
				if (checkIfThisNameAlreadyExists(words[1])) {
					user = new User(words[1], socket2Send);
					connectedUsers.add(user);
					wordToReturn = "OK";
				} else {
					wordToReturn = "Fail";
				}
				
				break;
		
			case "getUserList": // without value, return the users list
				wordToReturn = getUsersNamesForReturnToClient();
				break;
				
			case "wantToTalkWith": // wantToTalkWith:value open chat to the specified person
				wordToReturn = openChatToOtherUser(words[1]);
				break;
				
			case "msg": // msg:text:to:user
				String msg = words[1]; // msg:text:to:user
				
				if (words[2].equalsIgnoreCase("to")) {
					String otherUser = words[3];
					//System.out.println(user.getName() + " envia " + msg + " a " + otherUser);
					wordToReturn = sendMsgToThisUser(otherUser, msg);
				} else {
					wordToReturn = "SomeThing went wrong";
				}
				
				break;
			
			case "closeChatWith": // cerramos el chat con la persona que indica
				
				String usernameToSendCloseChat = words[1];
				wordToReturn = sendCloseMessageToThisUser(usernameToSendCloseChat);
				
				break;
			default:
				wordToReturn = "Unknown Command";		
		}
		
		return wordToReturn;
	}
	
	private String sendCloseMessageToThisUser(String usernameToSendCloseChat) {
		
		User userToCloseChat = findThisUserObject(usernameToSendCloseChat);
		
		try {
			PrintWriter out = new PrintWriter(userToCloseChat.getSocket().getOutputStream(), true);
			out.println("closeChat:" + user.getName());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Sended";
	}

	private String sendMsgToThisUser(String otherUser, String msg) {
		
		User userToSend = findThisUserObject(otherUser);
		System.out.println("Se le va a enviar " + msg + " a " + userToSend.getName());
		try {
			PrintWriter out = new PrintWriter(userToSend.getSocket().getOutputStream(), true);
			String chatMsg = "msg:" + user.getName() + ":" + msg;
			System.out.println(" CHAT " + chatMsg);
			out.println(chatMsg);
		} catch (IOException e) {
			return "Fail";
		} catch (NullPointerException e) {
			return "Fail";
		}
				
		return "sended";
	}
	
	private String openChatToOtherUser(String otherUser) {
		
		User userToOpenChat = findThisUserObject(otherUser);
		
		try {
			PrintWriter out = new PrintWriter(userToOpenChat.getSocket().getOutputStream(), true);
			String openChat ="openChatWithU:"+user.getName();
			System.out.println("Sended to other: " + openChat);
			out.println(openChat);
		} catch (IOException e) {
			return "Fail";
		} catch (NullPointerException e) {
			return "Fail";
		}
				
		return "chatRequestSended";
	}

	private User findThisUserObject(String otherUser) {
		
		for (User user : connectedUsers) {
			if (user.getName().equalsIgnoreCase(otherUser)){
				return user;
			}
		}
		
		return null;
	}

	private String getUsersNamesForReturnToClient() {
		
		String toReturn = "userList:";
		
		for (User userAux : connectedUsers) {
			
			if (userAux != user) {
				toReturn += userAux.getName() + ":";
			}		
		}

		return toReturn;
	}

	private boolean checkIfThisNameAlreadyExists(String name) {
		
		for (int i = 0; i < connectedUsers.size(); i++) {
			if (connectedUsers.get(i) != user && connectedUsers.get(i).getName().equalsIgnoreCase(name)) {
				return false;
			}
		}
		
		return true;
	}
	
}