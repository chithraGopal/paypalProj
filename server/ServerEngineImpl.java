package com.paypal.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerEngineImpl implements ServerEngine {
	ServerSocket server;
	Socket connection;
	String dataInput;
	private PrintWriter output;
	private BufferedReader input;
	private BufferedReader userInput;

	@Override
	public void createAndConnect() {
		try {
			System.out.println("Server started");
			server = new ServerSocket(3000);
			
			//Establish Connection
			connection = server.accept();
			System.out.println("Connection accepted, Server up and running");
			
			//Initialize the i/o streams
			output = new PrintWriter(connection.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			userInput = new BufferedReader(new InputStreamReader(System.in));
			
			output.println("hi..let's Chat");
			
			
			while((dataInput = input.readLine())!=null){
				
				System.out.println(dataInput);
				System.out.println("Server: ");
				
//				if(dataInput.equalsIgnoreCase("Exit")){
//					break;
//				}
				
				 String keyboardInput = userInput.readLine();
					output.println(keyboardInput);
				
			}
			
			
			input.close();
			output.close();
			userInput.close();
			connection.close();
			

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String args[]){
		
		ServerEngine chatServer = new ServerEngineImpl();
		chatServer.createAndConnect();
		
	}

}
