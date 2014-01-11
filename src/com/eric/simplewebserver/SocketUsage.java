package com.eric.simplewebserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketUsage {
	private Socket socket = null;
	private StringBuffer responseText = new StringBuffer();
	private String ip;
	private int port;
	
	public SocketUsage(String ip,int port){
		this.ip=ip;
		this.port=port;
	}

	public static void main(String[] args) throws Exception {
		new SocketUsage("127.0.0.1",8080).process();
	}

	public String process() throws Exception {
		initSocket();
		sendRequest();
		readResponse();
		closeSocket();
		return displayResponse();
	}
	
	private void initSocket() throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getByName(ip), port);
	}

	private void sendRequest() throws IOException {
		if (socket == null) {
			return;
		}
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println("GET /index.jsp HTTP/1.1");
		out.println("Host: localhost:8080");
		out.println("Connection: close");
		out.println();
	}

	private void readResponse() throws IOException, InterruptedException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		boolean loop = true;
		while (loop) {
			if (in.ready()) {
				int result = 0;
				while (result != -1) {
					result = in.read();
					responseText.append((char) result);
				}
				loop = false;
			}
			Thread.sleep(50);
		}
	}

	private String displayResponse() {
		System.out.println(responseText);
		return responseText.toString();
	}

	private void closeSocket() throws IOException {
		if (socket != null) {
			socket.close();
		}
	}
}
