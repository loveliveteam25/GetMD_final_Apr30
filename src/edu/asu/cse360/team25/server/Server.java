package edu.asu.cse360.team25.server;

import java.io.IOException;

import edu.asu.cse360.team25.server.connection.ServerClientConnectionManager;
import edu.asu.cse360.team25.server.database.DBManager;

public class Server {

	ServerClientConnectionManager sccm;

	DoctorManager dm;
	PatientManager pm;
	NurseManager nm;

	CaseManager casem;
	LabManager labm;
	ChatManager chatm;

	DBManager dbm;

	public Server() throws IOException {
		super();

		// construct all the manager
		dm = new DoctorManager();
		pm = new PatientManager();
		nm = new NurseManager();

		casem = new CaseManager();
		labm = new LabManager();
		chatm = new ChatManager();

		// construct database manager

		dbm = new DBManager();

		// construct connection manager

		sccm = new ServerClientConnectionManager(this);
		
		
		// for testing
		dm.setupDummyDoctor();
		pm.setupDummyPatient();
		casem.setupDummyCase();
		labm.setupDummyLabMeasurement();
		chatm.setupDummyChats();
		
	}

	public void start() throws InterruptedException {

		// connect database
		dbm.connect();

		// start listening
		sccm.startListening();

		
		System.out.println("Server started successfully~~~");
		
		sccm.waitForListeningThread();
	}

	
	
	public ServerClientConnectionManager getSCCM() {
		return sccm;
	}

	public DoctorManager getDM() {
		return dm;
	}

	public PatientManager getPM() {
		return pm;
	}

	public NurseManager getNM() {
		return nm;
	}

	public CaseManager getCaseM() {
		return casem;
	}

	public LabManager getLabM() {
		return labm;
	}

	public ChatManager getChatM() {
		return chatm;
	}

	public DBManager getDBM() {
		return dbm;
	}

	public static void main(String[] argv) {

		try {
			Server s = new Server();

			s.start();

		} catch (IOException e) {
			// Unable to create server listening socket.
			e.printStackTrace();
		} catch (InterruptedException e) {
			// Waiting for listening thread is interrupted. 
			e.printStackTrace();
		}

	}

}
