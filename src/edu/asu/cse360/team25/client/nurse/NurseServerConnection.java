package edu.asu.cse360.team25.client.nurse;

import java.io.IOException;
import java.net.Socket;

import edu.asu.cse360.team25.client.ClientServerConnection;

public class NurseServerConnection extends ClientServerConnection {

	protected static final int nurseListeningPort = 10232;
	protected static final String serverAddress = "localhost";

	
	public NurseServerConnection() throws IOException {
		super(serverAddress, nurseListeningPort);
	}


	@Override
	protected void dispatchReceivedMsg(String msg) {
		// TODO Auto-generated method stub
		
	}
	
	// message sent from server and received by patient
	
	
	protected void onLoginAck() {
		
	}
	
	protected void onLogoutAck() {
		
	}
	
	protected void onQueryPatientProfileAck() {
		
	}

	protected void onAddLabMeasurementAck() {
		
	}
	
	
	// message sent from patient and received by server
	
	
	
	protected void sendLogin() {
		
	}
	
	protected void sendLogout() {
		
	}
	
	protected void sendQueryPatientProfile() {
		
	}

	protected void sendAddLabMeasurement() {
		
	}


	
	
	
}
