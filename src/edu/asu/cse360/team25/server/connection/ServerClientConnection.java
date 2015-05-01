package edu.asu.cse360.team25.server.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import edu.asu.cse360.team25.protocol.Connection;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;
import edu.asu.cse360.team25.server.CaseManager;
import edu.asu.cse360.team25.server.ChatManager;
import edu.asu.cse360.team25.server.DoctorManager;
import edu.asu.cse360.team25.server.LabManager;
import edu.asu.cse360.team25.server.NurseManager;
import edu.asu.cse360.team25.server.PatientManager;
import edu.asu.cse360.team25.server.Server;

public abstract class ServerClientConnection extends Connection {

	protected Thread t; // receiving thread

	protected ServerClientConnectionManager sccm;

	protected DoctorManager dm;
	protected PatientManager pm;
	protected NurseManager nm;

	protected CaseManager casem;
	protected LabManager labm;
	protected ChatManager chatm;

	protected boolean closed = false;
	
	public ServerClientConnection(Socket socket, Server server) throws IOException {
		super();
		
		this.dm = server.getDM();
		this.pm = server.getPM();
		this.nm = server.getNM();
		this.casem = server.getCaseM();
		this.labm = server.getLabM();
		this.chatm = server.getChatM();

		this.sccm = server.getSCCM();
		
		this.socket = socket;
		in = socket.getInputStream();
		out = socket.getOutputStream();
		
		t = new ServerClientReceivingThread();
	}

	protected void closeConnection() throws IOException {
		
		if(!closed) {
			socket.shutdownInput();
			socket.shutdownOutput();
			closed = true;
		}
	}
	
	protected void startReceiving() {
		
		t.start();
	}

	protected abstract void cleanup() throws IOException;
	
	protected abstract void dispatchReceivedMsg(String msg) throws IOException, ProtocolErrorException;

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	class ServerClientReceivingThread extends Thread {
		@Override
		public void run() {

			System.out.println("Server client connection starts receiving.");
			
			try {

				while (true) {
					
					String msg = receiveMsg();
					if(msg == null)
						break;
					
					dispatchReceivedMsg(msg);
				}

				
				
			} catch (IOException e) {
				System.out.println("Client exception (possibly connection reset)");
			} catch (ProtocolErrorException e) {
				System.out.println(e.getMessage());
			} finally {
				
				try {
					cleanup();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			

			
			// clean up this connection
			
			System.out.println("Client disconnect.");

			
		}
		
	}
}
