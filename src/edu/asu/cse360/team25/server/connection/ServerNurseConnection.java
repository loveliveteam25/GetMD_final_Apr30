package edu.asu.cse360.team25.server.connection;

import java.io.IOException;
import java.net.Socket;

import edu.asu.cse360.team25.server.CaseManager;
import edu.asu.cse360.team25.server.ChatManager;
import edu.asu.cse360.team25.server.DoctorManager;
import edu.asu.cse360.team25.server.LabManager;
import edu.asu.cse360.team25.server.NurseManager;
import edu.asu.cse360.team25.server.PatientManager;
import edu.asu.cse360.team25.server.Server;

public class ServerNurseConnection extends ServerClientConnection {

	public ServerNurseConnection(Socket socket, Server server) throws IOException {
		super(socket, server);

	}

	@Override
	public void cleanup() {
		
	}

	@Override
	protected void dispatchReceivedMsg(String msg) {
		// TODO Auto-generated method stub
		
	}

	
	// message sent from server and received by patient
	
	
	protected void sendLoginAck() {
		
	}
	
	protected void sendLogoutAck() {
		
	}
	
	protected void sendQueryPatientProfileAck() {
		
	}

	protected void sendAddLabMeasurementAck() {
		
	}
	
	
	// message sent from patient and received by server
	
	
	
	protected void onLogin() {
		
		sendLoginAck();
	}
	
	protected void onLogout() {
		
		sendLogoutAck();
	}
	
	protected void onQueryPatientProfile() {
		
		sendQueryPatientProfileAck();
	}

	protected void onAddLabMeasurement() {
		
		sendAddLabMeasurementAck();
	}


	
	
	
}




































