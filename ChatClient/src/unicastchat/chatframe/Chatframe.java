package unicastchat.chatframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;

public class Chatframe extends JFrame implements ActionListener, WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	JTextArea textMessage;
	JTextArea chatText;
	boolean exit = false;
	private Socket socket;
	String otherUser;
	String nick;


	/**
	 * Create the frame.
	 */
	public Chatframe(Socket socket, String otherUser, String nick) {
		
		this.socket = socket;
		this.otherUser=otherUser;
		this.nick = nick;
		
		setTitle("SALA DE CHAT CON: "+otherUser);
		
		addWindowListener(this);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 423, 136);
		scrollPane.setAutoscrolls(true);
		contentPane.add(scrollPane);		
		
		chatText = new JTextArea();
		chatText.setWrapStyleWord(true);
		chatText.setLineWrap(true);
		//chatText.setBounds(12, 12, 423, 136);
		scrollPane.setViewportView(chatText);
		chatText.setCaretPosition(chatText.getText().length());
		
		//contentPane.add(chatText);
		chatText.setEditable(false);
		
		textMessage = new JTextArea();
		textMessage.setBounds(12, 174, 423, 37);
		contentPane.add(textMessage);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(105, 223, 99, 25);
		contentPane.add(btnSend);
		btnSend.addActionListener(this);
		
		JButton buttonExit = new JButton("Exit");
		buttonExit.setBounds(239, 223, 99, 25);
		contentPane.add(buttonExit);
		buttonExit.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd;
		cmd = e.getActionCommand();
		switch(cmd) {
		case "Send":
			if(!textMessage.getText().trim().isEmpty()) {
				sendMessage();
				System.out.println("send");
				chatText.append(nick+": "+getMessage()+"\n");
				chatText.moveCaretPosition(chatText.getText().length());
				chatText.repaint();
				textMessage.setText("");
			}
			break;
			
		case "Exit":
			System.out.println("exit");
//			try {
//				//socket.close();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			dispose();
			break;
			
		
		
		}
		// TODO Auto-generated method stub
		
	}
	// get message de la interfaz del chat dodne escribe el usuario
	public String getMessage() {
		String msn;
		
		msn = textMessage.getText().trim();
		
		return msn;
	}
	// escribir en la pantalla del chat.
	public void setMessage(String msn) {
		chatText.setText(msn);
		
	}
	public boolean setExit() {
		
		
		return exit;
	}
	public void  getExit() {
		
	}
	public void sendMessage() {
		String text = getMessage().replace(':', '&');
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("msg:"+text+":to:"+otherUser);
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	public String getOtherUser() {
		return otherUser;
	}

	public void otherUserHasTalk(String msg) {
		// TODO Auto-generated method stub
		chatText.append(otherUser + ": " + msg + "\n");
		chatText.moveCaretPosition(chatText.getText().length());
		chatText.repaint();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {

		String title = "You're closing this chat.";
		String answer = "Are you sure you want to close this chat?";
		
		int promptResult = JOptionPane.showConfirmDialog(null, answer, title, JOptionPane.YES_NO_OPTION);
		
		if (promptResult == JOptionPane.YES_OPTION) {
			
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println("closeChatWith:"+otherUser);
				this.dispose();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
		
		
	}
	public JTextArea getchatText() {
		
		return this.chatText;
	}
	
	public JTextArea getTextMessage() {
		return textMessage;
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

