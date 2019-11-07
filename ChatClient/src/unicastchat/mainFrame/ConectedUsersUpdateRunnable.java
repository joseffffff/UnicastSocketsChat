package unicastchat.mainFrame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConectedUsersUpdateRunnable implements Runnable{
	PrintWriter out;
	boolean running;
	public  ConectedUsersUpdateRunnable(Socket socket) {
		try {
			this.out=new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		running=true;
		// TODO Auto-generated method stub
		while (running) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("getUserList");
		}
	}

	public void stopRunning() {
		running = false;
	}
}
