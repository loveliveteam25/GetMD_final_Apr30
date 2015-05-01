package edu.asu.cse360.team25.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.asu.cse360.team25.protocol.Connection;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;

public abstract class ClientServerConnection extends Connection {

	protected Thread t; // receiving thread

	InetSocketAddress serverAddr;
	
	protected boolean closed = false;

	
	public ClientServerConnection(String serverAddress, int listeningPort) {
		super();
		
		socket = new Socket();
		serverAddr = new InetSocketAddress(serverAddress, listeningPort);
		
		t = new ClientServerReceivingThread();
	}

	public void connect() throws IOException {
		
		socket.connect(serverAddr);
		
		in = socket.getInputStream();
		out = socket.getOutputStream();

	}

	public void disconnect() throws IOException {
		
		if(!closed) {
			socket.shutdownOutput();
			socket.shutdownInput();
			closed = true;
		}
	}
	
	public void startReceiving() {
		
		t.start();
	}

	public void waitForReceivingThread() throws InterruptedException {
		
		t.join();
	}
	
	protected abstract void dispatchReceivedMsg(String msg) throws IOException, ProtocolErrorException;

	protected class ClientServerReceivingThread extends Thread {
		@Override
		public void run() {

			try {

				while (true) {
					
					String msg = receiveMsg();
					if(msg == null)
						break;
					
					dispatchReceivedMsg(msg);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ProtocolErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// clean up this connection
			try {
				socket.close();
				closed = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
}






















