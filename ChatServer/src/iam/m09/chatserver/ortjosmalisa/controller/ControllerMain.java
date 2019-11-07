package iam.m09.chatserver.ortjosmalisa.controller;

import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import iam.m09.chatserver.ortjosmalisa.gui.ConnectedUsersListUpdater;
import iam.m09.chatserver.ortjosmalisa.gui.ServerControllerUI;
import iam.m09.chatserver.ortjosmalisa.service.Service;

public class ControllerMain implements ActionListener{
	
	private static ServerControllerUI ui;
	private Service service;

	public static void main(String[] args) {
		
		try {
			startGUI();
			
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch (e.getActionCommand()) {
		
			case "startOrStopServer":
				startOrStopServer();
				break;
				
			default:
				break;
		
		}
		
	}
	
	public void startOrStopServer() {
		
		ui.changeGuiAfterChangeServerStatus();
		
		try {
			
			if (ui.isRunning()) {
				service = new Service(ui.getPort());
				System.out.println("Server running in port: " + ui.getPort());
				(new Thread(new ConnectedUsersListUpdater(service.getConnectedUsers(), ui))).start();
				service.startService();				
				System.out.println("Server Started");				
			} else {
				service.stopService();
				System.out.println("Stopped");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startGUI() throws FontFormatException, IOException {
		ui = new ServerControllerUI(new ControllerMain());
		ui.setVisible(true);
	}

}
