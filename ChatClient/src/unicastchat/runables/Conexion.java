package unicastchat.runables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

public class Conexion implements Runnable {
	
	String user1;
	List<String> listaUsers = new ArrayList<>();
	DefaultListModel<String> userList;
	Socket socket;
	
	public Conexion(String user1,DefaultListModel<String> userList,Socket socket) {
		this.user1 = user1;
		this.userList = userList;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			tryConectServer();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void tryConectServer() throws UnknownHostException, IOException {

		String msn;
		String msnSplit[];

		

		if (socket.isConnected()) {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			out.println("name:"+user1);
			
			msn=inp.readLine();
			
			//System.out.println(msn);
			
			if(msn.equals("OK")) {
				out.println("getUserList");
				msn=inp.readLine();
				
				msnSplit = msn.split(":");
				for(int i=1; i<msnSplit.length;i++) {
					listaUsers.add(msnSplit[i]);
					userList.addElement(msnSplit[i]);
				}
					//System.out.println(listaUsers.toString());
			}
			
		} else {
			System.out.println("no estoy conectado");
		}

	}
}
