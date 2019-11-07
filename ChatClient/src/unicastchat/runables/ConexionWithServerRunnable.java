package unicastchat.runables;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import unicastchat.chatframe.Chatframe;
import unicastchat.mainFrame.LoginInterface;

public class ConexionWithServerRunnable implements Runnable {

	Socket socket;
	BufferedReader in;
	PrintWriter out;
	boolean listening;

	List<Chatframe> chats;
	String myNick;
	LoginInterface ui;
	DefaultListModel<String> userList;

	public ConexionWithServerRunnable(Socket socket, String myNick, LoginInterface ui, DefaultListModel<String> userList) {
		this.ui = ui;
		this.myNick = myNick;
		this.socket = socket;
		this.userList = userList;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		chats = new ArrayList<Chatframe>();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		listening = true;
		
		while (listening) {
			
			try {
				
				String info = in.readLine();
				String[] splitInfo = info.split(":");
				
				System.out.println(info);
				
				switch (splitInfo[0]) {

					case ("openChatWithU"):
						String userName = splitInfo[1];
	
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									Chatframe frame = new Chatframe(socket, userName, myNick);
									frame.setVisible(true);
									System.out.println("Me han abierto un chat");
									chats.add(frame);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						
						break;
						
					case "msg":
						
						String user = splitInfo[1];
						String msg = splitInfo[2];
						
						System.out.println("Mensaje de: " + user + ", Ha enviado: " + msg);
						
						Chatframe chatWherePrint = inWhichChatIsThisUser(user);
						
						if (chatWherePrint != null) {
							chatWherePrint.otherUserHasTalk(msg);
						} else {
							System.out.println("failako");
						}
						
						break;
						
					case "chatRequestSended":
						
						EventQueue.invokeLater(new Runnable() {
							public void run() {

								try {
									Chatframe frame = new Chatframe(socket,ui.getUserList().getSelectedValue(),myNick);
									frame.setVisible(true);
									chats.add(frame);
									System.out.println("Abro un chat");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						
						break;
						
					case "userList" :
						
						userList.clear();
						
						for (int i = 1; i < splitInfo.length; i++) {
							userList.addElement(splitInfo[i]);
						}
						
						break;
					
					case "closeChat":
						Chatframe frame = inWhichChatIsThisUser(splitInfo[1]);
						frame.getchatText().setText("Se ha desconectado: " + splitInfo[1] + "\n");
						frame.getTextMessage().setEnabled(false);
						break;
						
					default:
						System.out.println("Ha llegado algo y no he sabido que hacer, algo mal.");

				}
				
			} catch (IOException e) {
				System.err.println("Se ha cerrado la conexión con el servidor.");
			}
		}

	}
	
	public Chatframe inWhichChatIsThisUser(String user) {
		
		for (Chatframe chatframe : chats) {
			if (chatframe.getOtherUser().equalsIgnoreCase(user)) {
				return chatframe;
			}
		}
		
		return null;
	}

	public void stopListening() {
		listening = false;
	}

	public void addChat(Chatframe chat) {
		chats.add(chat);
	}
}
