package com.paypal.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class ChatWindow extends JFrame {

	/**
	 * This is a swing class which defines the UI of the chat application window
	 */

	private JPanel panel;
	private JTextArea usersTextArea;
	private JTextField myTextField;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JMenuBar menuBar;
	private JMenuItem homeMenuItem;
	private JMenuItem usersMenuItem;
	private JMenuItem helpMenuItem;
	private JPopupMenu popUp;
	public static StringBuffer outBuffer = new StringBuffer();
	public static StringBuffer inBuffer = new StringBuffer();
	volatile String sendToServer;
	volatile boolean thereIsClientInput;
	private String userNameText;
	// private JMenuItem menuItem;

	private static final long serialVersionUID = 1L;
	public static final String ME_TEXT = "Me: ";
	public static final String IMAGE_FILE_PATH = "C:\\Users\\Bhargav\\workspace\\ChatWithMe\\src\\com\\paypal\\util\\chatIcon.png";

	public static void main(String[] args) {
		ChatWindow frame = new ChatWindow("");
		frame.listenAndConnect();
		// ChatWindow cw = new ChatWindow();
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		// cw.createAndShowGui();
		// }
		//
		// });

	}

	public ChatWindow(String userName) throws HeadlessException {
		setTitle("Welcome " + userName + "!");
		this.userNameText = userName;
		SendAndReceiveWorker task = new SendAndReceiveWorker();
		task.execute();
		
		// super();
	}

	public String getUserNameText() {
		return userNameText;
	}

	public void setUserNameText(String userNameText) {
		this.userNameText = userNameText;
	}

	void createAndShowGui() {

		panel = new JPanel();
		myTextField = new JTextField();
		textArea = new JTextArea();
		usersTextArea = new JTextArea();

		menuBar = new JMenuBar();
		homeMenuItem = new JMenuItem("Home");
		popUp = new JPopupMenu("Logout");
		// homeMenuItem.add(popUp,);
		usersMenuItem = new JMenuItem("Users");
		helpMenuItem = new JMenuItem("Help");

		menuBar.add(homeMenuItem);
		menuBar.add(usersMenuItem);
		menuBar.add(helpMenuItem);

		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setEditable(false);
		panel.add(textArea);
		panel.add(myTextField);

		usersTextArea.setMargin(new Insets(5, 5, 5, 5));
		usersTextArea.setEditable(false);
		usersTextArea.add(textArea);

		// add(menuBar, BorderLayout.NORTH);
		add(panel, BorderLayout.SOUTH);
		// setTitle("My Chat");
		setLocationRelativeTo(null);
		setSize(400, 400);

		panel.setLayout(new GridLayout(1, 1));
		

		myTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String actionCommand = arg0.getActionCommand();
				if (actionCommand != null) {
					// System.out.println(actionCommand);
					textArea.append("\n");
					textArea.append(ME_TEXT + actionCommand);
					textArea.append("\n");
					// LoginScreen.outBuffer.append(actionCommand);
					myTextField.setText("");
					sendToServer = actionCommand;
//					thereIsClientInput = true;
					
									}
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to exit My Chat?",
						"Alert", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					dispose();
				}
				// super.windowClosing(e);
			}
		});

		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
		setLocationRelativeTo(null);
		// ImageIcon icon = new
		// ImageIcon(getClass().getResource("chatIcon.png"));
		// setIconImage(icon.getImage());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// pack();
		setVisible(true);

	}

	class SendAndReceiveWorker extends SwingWorker<List<String>, String> {

//		JTextArea ta;
//		String user;

		
		@Override
		protected List<String> doInBackground() throws Exception {
			PrintWriter output;
			BufferedReader input;
			Socket connection;
			List<String> dataList = new ArrayList<String>();

			connection = new Socket(InetAddress.getLocalHost(), 3000);

			output = new PrintWriter(connection.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String fromServerUser;
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

			while ((fromServerUser = input.readLine()) != null) {
				publish(fromServerUser);
				dataList.add(fromServerUser);
				String keyboardInput = userInput.readLine();// sendToServer;//
					output.println(getUserNameText() + ": " + keyboardInput);
//				}
			}
			output.close();
			input.close();
			connection.close();

			return dataList;

		}

		@Override
		protected void process(List<String> list) {
			// TODO Auto-generated method stub
			for (String data : list) {
				textArea.append("\nFrom Server: " + data);
			}
			// super.done();
		}
	}

	void listenAndConnect() {
		try {
			PrintWriter output;
			BufferedReader input;
			Socket connection;

			connection = new Socket(InetAddress.getLocalHost(), 3000);

			output = new PrintWriter(connection.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServerUser;

			while ((fromServerUser = input.readLine()) != null) {
				// textArea.setText("User" + serverInput);
				System.out.println(" SS: " + fromServerUser);

				String keyboardInput = stdIn.readLine();
				if (keyboardInput != null) {
					output.println("CS: " + keyboardInput);
				}

			}
			output.close();
			input.close();
			connection.close();

		} catch (UnknownHostException e) {
			// TODO Auto-g}enerated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
