package edu.asu.cse360.team25.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.asu.cse360.team25.server.Server;

public class ServerClientConnectionManager {

	Server server;
	
	protected Thread pt;
	protected Thread dt;
	protected Thread nt;

	protected ServerSocket pListener;
	protected ServerSocket dListener;
	protected ServerSocket nListener;

	// connection set
	protected Set<ServerPatientConnection> SPCSet = new HashSet<ServerPatientConnection>();
	protected Set<ServerDoctorConnection> SDCSet = new HashSet<ServerDoctorConnection>();
	protected Set<ServerNurseConnection> SNCSet = new HashSet<ServerNurseConnection>();

	// connection indexed by id
	protected Map<Integer, ServerPatientConnection> SPCMap = new HashMap<Integer, ServerPatientConnection>();
	protected Map<Integer, ServerDoctorConnection> SDCMap = new HashMap<Integer, ServerDoctorConnection>();
	protected Map<Integer, ServerNurseConnection> SNCMap = new HashMap<Integer, ServerNurseConnection>();

	protected static final int patientListeningPort = 10230;
	protected static final int doctorListeningPort = 10231;
	protected static final int nurseListeningPort = 10232;

	

	public ServerClientConnectionManager(Server server) throws IOException {

		this.server = server;
		
		pListener = new ServerSocket(patientListeningPort);
		dListener = new ServerSocket(doctorListeningPort);
		nListener = new ServerSocket(nurseListeningPort);

		pt = new ServerPatientListeningThread();

		dt = new ServerDoctorListeningThread();

		nt = new ServerNurseListeningThread();

	}

	public void startListening() {

		pt.start();
		dt.start();
		nt.start();

	}

	public void waitForListeningThread() throws InterruptedException {

		pt.join();
		dt.join();
		nt.join();

	}

	protected void addServerClientConnection(int id, ServerClientConnection scc) {

		if (scc instanceof ServerPatientConnection) {

			SPCMap.put(id, (ServerPatientConnection) scc);

		} else if (scc instanceof ServerDoctorConnection) {

			SDCMap.put(id, (ServerDoctorConnection) scc);

		} else if (scc instanceof ServerNurseConnection) {

			SNCMap.put(id, (ServerNurseConnection) scc);

		} else {

			// TODO: simply ignore spurious connection
		}

	}

	protected void removeServerClientConnection(int id,
			ServerClientConnection scc) {

		if (scc instanceof ServerPatientConnection) {

			SPCMap.remove(id);

		} else if (scc instanceof ServerDoctorConnection) {

			SDCMap.remove(id);

		} else if (scc instanceof ServerNurseConnection) {

			SNCMap.remove(id);

		} else {

			// simply ignore spurious connection
		}

	}

	protected ServerPatientConnection findServerPatientConnection(int id) {

		return SPCMap.get(id);
	}

	protected ServerDoctorConnection findServerDoctorConnection(int id) {

		return SDCMap.get(id);
	}

	protected ServerNurseConnection findServerNurseConnection(int id) {

		return SNCMap.get(id);
	}

	
	class ServerPatientListeningThread extends Thread {
		@Override
		public void run() {

			System.out.println("Start listening for patient connection.");
			
			while (true) {
				try {
					Socket socket = pListener.accept();

					System.out.println("Patient connection accepted. addr = " + socket.getRemoteSocketAddress() + ", port = " + socket.getPort());

					ServerPatientConnection spc = new ServerPatientConnection(
							socket, server);
					SPCSet.add(spc);

					spc.startReceiving();
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
	}
	
	class ServerDoctorListeningThread extends Thread {
		@Override
		public void run() {

			while (true) {
				try {
					Socket socket = dListener.accept();

					ServerDoctorConnection sdc = new ServerDoctorConnection(
							socket, server);
					SDCSet.add(sdc);

					sdc.startReceiving();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
	}
	class ServerNurseListeningThread extends Thread {
		@Override
		public void run() {

			while (true) {
				try {
					Socket socket = nListener.accept();

					ServerNurseConnection snc = new ServerNurseConnection(
							socket, server);
					SNCSet.add(snc);

					snc.startReceiving();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		
	}
}
