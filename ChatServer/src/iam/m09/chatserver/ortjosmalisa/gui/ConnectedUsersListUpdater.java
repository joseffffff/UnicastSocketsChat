package iam.m09.chatserver.ortjosmalisa.gui;

import java.util.List;

import javax.swing.JTextArea;

import iam.m09.chatserver.ortjosmalisa.model.User;

public class ConnectedUsersListUpdater implements Runnable{
	
	private List<User> connectedUsers;
	private ServerControllerUI gui;
	JTextArea areaConnecteds;
	
	public ConnectedUsersListUpdater(List<User> connectedUsers, ServerControllerUI gui) {
		this.connectedUsers = connectedUsers;
		this.gui = gui;
		areaConnecteds = gui.getAreaConnectedUsers();
	}

	@Override
	public void run() {
		
		while (true) {			
		
			pinta();
			
			try {
				Thread.sleep(1500);
				
				if (!gui.isRunning()) {
					Thread.sleep(500);
					pinta();
					break;
				}
							
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	}
	
	public void pinta() {
		areaConnecteds.setText("");
		
		//System.out.println("usersize: " + connectedUsers.size());
		
		for (int i = 0; i < connectedUsers.size(); i++) {
			areaConnecteds.setText(areaConnecteds.getText() + 
					connectedUsers.get(i).getTextForConnectedArea() + "\n");
			
			//System.out.println("Entra: " + connectedUsers.get(i).getTextForConnectedArea());
		}
	}

}
