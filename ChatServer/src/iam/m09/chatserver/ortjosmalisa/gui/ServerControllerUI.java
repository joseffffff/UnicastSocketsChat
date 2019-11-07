package iam.m09.chatserver.ortjosmalisa.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class ServerControllerUI extends JFrame {

	// Algo raro
	private static final long serialVersionUID = 1L;
	
	// JPanel principal
	private JPanel contentPane;
	
	// Cosas a cambiar al iniciar/parar el server
	private JLabel lblServerStopped;
	private JButton btnStartOrStop;
	private JLabel iconLabel;
	
	// fotitos 
	private ImageIcon iconStopped;
	private ImageIcon iconRunning;
	private static int imageSize = 60;
	
	// Valores que introduce el user
	private JFormattedTextField portTextField;
	private JFormattedTextField maxUsersTextField;

	// areas de chats activos y users activos
	private JTextArea areaConnectedUsers;
	private JTextArea areaActiveChats;
	
	// Listener para los clicks del botón
	private ActionListener actionListener;
	
	// serverStateRunning
	private boolean running;
	


	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws FontFormatException 
	 */
	public ServerControllerUI(ActionListener actionListener) throws FontFormatException, IOException {
		
		this.actionListener = actionListener;
		running = false;
		
		iconStopped = scaleImage(
						new ImageIcon(
								ServerControllerUI.class.getResource("icons/stoppedIcon.png")),
									imageSize, imageSize);
		
		iconRunning = scaleImage(
						new ImageIcon(
								ServerControllerUI.class.getResource("icons/runningIcon.png")),
									imageSize, imageSize);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 350);
		
		InputStream is = getClass().getResourceAsStream("fonts/Roboto-Bold.ttf");
		//Font font = Font.createFont(Font.TRUETYPE_FONT, is);
		is.close();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(0xB2DFDB));
		
		iconLabel = new JLabel();
		iconLabel.setIcon(iconStopped);
		iconLabel.setBounds(12, 42, 60, 60);
		contentPane.add(iconLabel);
		
		lblServerStopped = new JLabel("Server Stopped");
		lblServerStopped.setBounds(84, 59, 137, 25);
		lblServerStopped.setFont(new Font("Dialog", Font.PLAIN, 15));
		contentPane.add(lblServerStopped);
		
//		try {
//			MaskFormatter formatter = new MaskFormatter("#####");
//			formatter.setValueContainsLiteralCharacters ( false );  
//			formatter.setOverwriteMode ( true );  
//			formatter.setValidCharacters ( "0123456789" ); 
			
			portTextField = new JFormattedTextField();
			portTextField.setBounds(111, 124, 80, 31);
			contentPane.add(portTextField);
			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
		JLabel lblPort = new JLabel("Port ");
		lblPort.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblPort.setBounds(26, 132, 70, 15);
		contentPane.add(lblPort);
		
		JLabel lblMaxUsers = new JLabel("Max Users");
		lblMaxUsers.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMaxUsers.setBounds(26, 182, 80, 15);
		contentPane.add(lblMaxUsers);
		
		maxUsersTextField = new JFormattedTextField();
		maxUsersTextField.setBounds(111, 174, 80, 31);
		contentPane.add(maxUsersTextField);
		
		btnStartOrStop = new JButton("START");
		btnStartOrStop.setBounds(56, 234, 117, 25);
		btnStartOrStop.setActionCommand("startOrStopServer");
		btnStartOrStop.addActionListener(this.actionListener);
		contentPane.add(btnStartOrStop);
		
		areaConnectedUsers = new JTextArea();
		areaConnectedUsers.setEditable(false);
		areaConnectedUsers.setBounds(239, 40, 200, 270);
		contentPane.add(areaConnectedUsers);
		
		areaActiveChats = new JTextArea();
		areaActiveChats.setBounds(480, 40, 200, 270);
		contentPane.add(areaActiveChats);
		
		JLabel lblOpenedChats = new JLabel("Active chats");
		lblOpenedChats.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblOpenedChats.setBounds(540, 13, 96, 15);
		contentPane.add(lblOpenedChats);
		
		JLabel lblConnectedUsers = new JLabel("Connected Users");
		lblConnectedUsers.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblConnectedUsers.setBounds(274, 13, 155, 15);
		contentPane.add(lblConnectedUsers);
		
		JLabel lblServerInfo = new JLabel("Server Info");
		lblServerInfo.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblServerInfo.setBounds(65, 8, 109, 25);
		contentPane.add(lblServerInfo);
	}
	
	public void changeTextButtonAfterStart() {
		
		if (running) {
			btnStartOrStop.setText("Stop");
		} else {
			btnStartOrStop.setText("Start");
		}
	}
	
	public void changeGuiAfterChangeServerStatus() {
		running = !running;
		changeTextButtonAfterStart();
		changeStateIcon();
		changeStateText();
		repaint();
	}

	private void changeStateText() {
		
		if (running) {
			lblServerStopped.setText("Server Running!");
		} else {
			lblServerStopped.setText("Server Stopped");
		}		
	}

	private void changeStateIcon() {
		
		if (running) {
			iconLabel.setIcon(iconRunning);
		} else {
			iconLabel.setIcon(iconStopped);		
		}			
	}
	
	public int getPort() {
		
		String portString  = portTextField.getText();
		
		System.out.println("Port: " + portString);
		
		return Integer.parseInt(portString);
		//return (Integer) portTextField.getValue();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public JTextArea getAreaConnectedUsers() {
		return areaConnectedUsers;
	}
	
	 // Resize an image
    private ImageIcon scaleImage(ImageIcon imageIcon, int width, int height){       
        Image image = imageIcon.getImage();                                             // transform it
        Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);     // scale it the smooth way 
        return new ImageIcon(newimg);
    }
}
