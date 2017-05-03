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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
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
	String sendToServer;
	private String userNameText;
	// private JMenuItem menuItem;

	private static final long serialVersionUID = 1L;
	public static final String ME_TEXT = "Me: ";
	public static final String IMAGE_FILE_PATH = "C:\\Users\\Bhargav\\workspace\\ChatWithMe\\src\\com\\paypal\\util\\chatIcon.png";

	// public static void main(String[] args) {
	// ChatWindow frame = new ChatWindow("");
	// frame.listenAndConnect();
	// }

	public ChatWindow(String userName) throws HeadlessException {

		setTitle("Welcome " + userName + "!");
		this.userNameText = userName;
		SendAndReceiveWorker task = new SendAndReceiveWorker();
		task.execute();

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

		// TextFieldInputStream tfis = new TextFieldInputStream(myTextField);
		// System.setIn(tfis);
		myTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String actionCommand = arg0.getActionCommand();
				if (actionCommand != null) {
					textArea.append("\n");
					textArea.append(ME_TEXT + actionCommand);
					inBuffer.append(actionCommand);
					textArea.append("\n");
					myTextField.setText("");
					sendToServer = actionCommand;

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
			}
		});

		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
		setLocationRelativeTo(null);
		 ImageIcon icon = new ImageIcon(getClass().getResource("chatIcon.png"));

		 setIconImage(icon.getImage());
		 setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// pack();
		setVisible(true);

	}

	class SendAndReceiveWorker extends SwingWorker<List<String>, String> {

		private Socket connection;

		@Override
		protected List<String> doInBackground() throws Exception {
			PrintWriter output;
			BufferedReader input;
			List<String> dataList = new ArrayList<String>();

			connection = new Socket(InetAddress.getLocalHost(), 3000);

			output = new PrintWriter(connection.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String fromServerUser;

			while (true) {
				fromServerUser = input.readLine();
				publish(fromServerUser);
				dataList.add(fromServerUser);
				String keyboardInput = sendToServer;
				output.println(getUserNameText() + ": " + keyboardInput);
				if (fromServerUser.equalsIgnoreCase("EXIT")) {
					break;
				}
			}
			output.close();
			input.close();
			connection.close();
			return dataList;
		}

		@Override
		protected void process(List<String> list) {
			for (String data : list) {
				textArea.append("\nFrom Server: " + data);
			}
		}
	}

}
