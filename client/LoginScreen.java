
package com.paypal.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginScreen extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String STATUS_ONLINE = "online";
	private JTextField userNameField;
	private JButton submitButton;
	private JButton cancelButton;
	private JLabel userNameLabel;
	public static StringBuffer outBuffer =  new StringBuffer();
	public static StringBuffer inBuffer =  new StringBuffer();



	public HashMap<String, String> userStatusMap;

	public static void main(String[] args) {
		try {
			InetAddress ipAdd = InetAddress.getLocalHost();
			System.out.println(ipAdd);
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoginScreen login = new LoginScreen();
//		login.listenAndConnect();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				login.createAndShowGui();

			}
		});
//		login.listenAndConnect();

	}
	
		
		
	


	private void createAndShowGui() {

		JPanel panel = new JPanel();

		userNameLabel = new JLabel("Username");
		userNameLabel.setBounds(10, 10, 80, 25);
		panel.add(userNameLabel);

		userNameField = new JTextField();
		userNameField.setPreferredSize(new Dimension(120, 30));
		userNameField.setBounds(100, 100, 200, 25);
		panel.add(userNameField);

		submitButton = new JButton("Submit");
		panel.add(submitButton);
		cancelButton = new JButton("Cancel");
		panel.add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to exit?", "Alert",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					dispose();
				} else if (confirm == JOptionPane.NO_OPTION) {
					return;
				}
			}
		});

		submitButton.addActionListener(new ActionListener() {

			private ChatWindow cw;

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = userNameField.getText();
				if (username.equals("")) {
					JOptionPane.showMessageDialog(panel, "Please enter username", "Alert", JOptionPane.OK_OPTION);
					// dispose();
					return;
				}
//				System.out.println(username);
				userStatusMap =  new HashMap<String,String>();
				if (userStatusMap.containsKey(username)) {
					JOptionPane.showInternalMessageDialog(panel,
							"This username already exists. Please choose a unique name.");
				} else {
					userStatusMap.put(username, STATUS_ONLINE);
				}

				cw = new ChatWindow(username);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						cw.createAndShowGui();
					}

				});
//				cw.listenAndConnect();
				dispose();
			}
		});

		add(panel);
		setSize(250, 120);
		setTitle("Login");

		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	};
	
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
				if(inBuffer.length()!=0){
					inBuffer.append(fromServerUser);
				}

				String keyboardInput = stdIn.readLine();
				if(keyboardInput!=null){
//					output.println("CS: "+keyboardInput);
					
				}
				if(outBuffer.length()!=0){
					output.println("CS: "+outBuffer);
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
