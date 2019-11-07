package unicastchat.mainFrame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import unicastchat.runables.Conexion;
import unicastchat.runables.ConexionWithServerRunnable;

import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JList;

public class LoginInterface extends JFrame implements ActionListener, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private String nameUser;
	private JTextPane tpUsername;
	private JLabel lblNewLabel;
	private JList<String> userList;
	private DefaultListModel<String> defaultList;
	JButton btnAceptar;
	JLabel officon;
	JLabel onIcon;

	ConexionWithServerRunnable listener;

	private static String serverChannel;
	private static int port;
	Socket socket;

	private ConectedUsersUpdateRunnable updater;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.err.println("FAIL. \nSyntax: <program> <ip> <port>");
			System.exit(1);
		}

		serverChannel = args[0];
		port = Integer.parseInt(args[1]);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginInterface frame = new LoginInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public LoginInterface() {

		setResizable(false);
		setTitle("Unicast Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		addWindowListener(this);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tpUsername = new JTextPane();
		tpUsername.setBounds(238, 20, 170, 25);
		contentPane.add(tpUsername);

		lblNewLabel = new JLabel("User name");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(130, 20, 94, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(LoginInterface.class.getResource("/img/icons8-user-50.png")));
		lblNewLabel_1.setBounds(67, 12, 62, 75);
		contentPane.add(lblNewLabel_1);

		btnAceptar = new JButton("Conectar");
		btnAceptar.setBackground(UIManager.getColor("MenuItem.acceleratorForeground"));
		btnAceptar.setForeground(Color.BLACK);
		btnAceptar.setBounds(238, 57, 170, 25);
		contentPane.add(btnAceptar);
		btnAceptar.addActionListener(this);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(
				LoginInterface.class.getResource("/img/friends_talking_sms_talk_chat_conversation-64.png")));
		lblNewLabel_2.setBounds(67, 133, 82, 91);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Select user");
		lblNewLabel_3.setForeground(Color.BLACK);
		lblNewLabel_3.setBounds(143, 166, 81, 15);
		contentPane.add(lblNewLabel_3);

		defaultList = new DefaultListModel<>();
		userList = new JList<String>(defaultList);
		userList.setBounds(238, 133, 170, 91);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(-1);
		contentPane.add(userList);

		JButton btnPrueba = new JButton("chat");
		btnPrueba.setBounds(271, 235, 117, 25);
		contentPane.add(btnPrueba);

		onIcon = new JLabel("");
		onIcon.setIcon(new ImageIcon(LoginInterface.class.getResource("/img/onbueno.png")));
		onIcon.setBounds(392, 94, 16, 27);
		contentPane.add(onIcon);
		onIcon.setVisible(false);

		JLabel lblNewLabel_4 = new JLabel("Status");
		lblNewLabel_4.setBounds(330, 106, 47, 15);
		contentPane.add(lblNewLabel_4);

		officon = new JLabel("");
		officon.setIcon(new ImageIcon(LoginInterface.class.getResource("/img/offbueno.png")));
		officon.setBounds(392, 94, 16, 27);
		contentPane.add(officon);
		btnPrueba.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		switch (cmd) {
		case ("Conectar"):

			try {
				socket = new Socket(serverChannel, port);

			} catch (IOException e1) {
				btnAceptar.setText("Conectar");
				
				System.err.println("No s'ha pogut conectar");
				e1.printStackTrace();
			}

			nameUser = tpUsername.getText();
			if (nameUser.equalsIgnoreCase("")) {

			} else {
				Thread threadConex = new Thread(new Conexion(tpUsername.getText(), defaultList, socket));
				threadConex.start();
				if (!socket.isClosed()) {
					btnAceptar.setText("Desconectar");
					onIcon.setVisible(true);
					officon.setVisible(false);
					
				}

				try {
					threadConex.join();
					System.out.println("Hola soy " + nameUser);
				} catch (InterruptedException e1) {

					e1.printStackTrace();
				}
			}
			listener = new ConexionWithServerRunnable(socket, nameUser, this, defaultList);

			Thread threadListener = new Thread(listener);
			threadListener.start();

			updater = new ConectedUsersUpdateRunnable(socket);
			Thread threadUpdater = new Thread(updater);
			threadUpdater.start();

			break;

		case ("chat"):

			String userSelected = userList.getSelectedValue();
			PrintWriter out;
			//BufferedReader inp;
			//String chatRequest = "";
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				// inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.println("wantToTalkWith:" + userSelected);
				// chatRequest = inp.readLine();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;
		case "Desconectar":
			disconnect();
			break;

		}

	}

	public void disconnect() {
		try {
			updater.stopRunning();
			listener.stopListening();
			socket.close();
			tpUsername.setText("");
			btnAceptar.setText("Conectar");
			defaultList.removeAllElements();
			officon.setVisible(true);
			onIcon.setVisible(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public JList<String> getUserList() {
		return userList;
	}

	public void setUserList(JList<String> userList) {
		this.userList = userList;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			disconnect();
		} catch(Exception e) {
			
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
