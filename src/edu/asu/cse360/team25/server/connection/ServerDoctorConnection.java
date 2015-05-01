package edu.asu.cse360.team25.server.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

import edu.asu.cse360.team25.protocol.CaseInfo;
import edu.asu.cse360.team25.protocol.ChatInfo;
import edu.asu.cse360.team25.protocol.DoctorServerMsg;
import edu.asu.cse360.team25.protocol.LabMeasurementInfo;
import edu.asu.cse360.team25.protocol.PatientInfo;
import edu.asu.cse360.team25.protocol.exception.InvalidProtocolStateException;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;
import edu.asu.cse360.team25.server.Case;
import edu.asu.cse360.team25.server.Chat;
import edu.asu.cse360.team25.server.LabMeasurement;
import edu.asu.cse360.team25.server.Patient;
import edu.asu.cse360.team25.server.Server;

public class ServerDoctorConnection extends ServerClientConnection {

	protected int doctorID = -1;

	// patient ID in the processing case
	protected int patientID = -1;

	// current processing case
	protected Case currentCase = null;

	protected ConnectionState state = ConnectionState.INIT;

	protected boolean expectingSpecificMsg = false;
	protected HashSet<DoctorServerMsg> emsg = new HashSet<DoctorServerMsg>();

	protected static enum ConnectionState {
		INIT, ONLINE, IN_CASE
	}

	public ServerDoctorConnection(Socket socket, Server server)
			throws IOException {
		super(socket, server);

		state = ConnectionState.INIT;
		setExpectedMsgInit();

	}

	@Override
	public void cleanup() throws IOException {

		if(doctorID != -1) {
			sccm.removeServerClientConnection(doctorID, this);
			dm.markDoctorOffline(doctorID);
		}
		
		if(patientID != -1) {
			ServerPatientConnection spc = sccm
					.findServerPatientConnection(patientID);
			if(spc != null)
				spc.closeConnection();
		}
		
		if(currentCase != null) {
			casem.suspendCase(currentCase.getCaseID());
		}
	}

	@Override
	protected void dispatchReceivedMsg(String msg) throws IOException,
			ProtocolErrorException {

		// message format: <XXXX#content>,
		// where XXXX denotes the message type as readable string.

		int mark = msg.indexOf('#');
		String type = msg.substring(0, mark);
		String content = msg.substring(mark + 1);
		if (type.equals("Login"))
			onLogin(content);
		else if (type.equals("Logout"))
			onLogout(content);
		else if (type.equals("QueryPatientProfile"))
			onQueryPatientProfile(content);
		else if (type.equals("QueryAllCaseIDOfOnePatient"))
			onQueryAllCaseIDOfOnePatient(content);
		else if (type.equals("QueryCase"))
			onQueryCase(content);
		else if (type.equals("QueryAllLabMeasurementIDOfOnePatient"))
			onQueryAllLabMeasurementIDOfOnePatient(content);
		else if (type.equals("QueryLabMeasurement"))
			onQueryLabMeasurement(content);
		else if (type.equals("QueryChatHistory"))
			onQueryChatHistory(content);
		else if (type.equals("ForwardHandleCaseAck"))
			onForwardHandleCaseAck(content);
		else if (type.equals("LinkCaseToCurrentCase"))
			onLinkCaseToCurrentCase(content);
		else if (type.equals("LinkLabMeasurementToCurrentCase"))
			onLinkLabMeasurementToCurrentCase(content);
		else if (type.equals("ChatMessage"))
			onChatMessage(content);
		else if (type.equals("ForwardSuspendCaseByPatientAck"))
			onForwardSuspendCaseByPatientAck(content);
		else if (type.equals("SuspendCaseByDoctor"))
			onSuspendCaseByDoctor(content);
		else if (type.equals("FinishCase"))
			onFinishCase(content);
		else
			throw new ProtocolErrorException(
					"Unrecognized message received on doctor side!");

	}

	protected void setExpectedMsgInit() {

		emsg.clear();
		emsg.add(DoctorServerMsg.LOGIN);

	}

	protected void setExpectedMsgOnline() {

		emsg.clear();
		emsg.add(DoctorServerMsg.LOGOUT);
		emsg.add(DoctorServerMsg.QUERY_PATIENT_PROFILE);
		emsg.add(DoctorServerMsg.QUERY_ALL_CASE_ID_OF_ONE_PATIENT);
		emsg.add(DoctorServerMsg.QUERY_CASE);
		emsg.add(DoctorServerMsg.QUERY_ALL_LAB_MEASUREMENT_ID_OF_ONE_PATIENT);
		emsg.add(DoctorServerMsg.QUERY_LAB_MEASUREMENT);
		emsg.add(DoctorServerMsg.QUERY_CHAT_HISTORY);
		// emsg.add(DoctorServerMsg.FORWARD_HANDLE_CASE_ACK);

	}

	protected void setExpectedMsgInCaseProcessing() {

		emsg.clear();
		emsg.add(DoctorServerMsg.CHAT_MESSAGE);
		emsg.add(DoctorServerMsg.QUERY_CHAT_HISTORY);
		emsg.add(DoctorServerMsg.LINK_CASE_TO_CURRENT_CASE);
		emsg.add(DoctorServerMsg.LINK_LAB_MEASUREMENT_TO_CURRENT_CASE);
		// emsg.add(DoctorServerMsg.FORWARD_SUSPEND_CASE_BY_PATIENT_ACK);
		emsg.add(DoctorServerMsg.SUSPEND_CASE_BY_DOCTOR);
		emsg.add(DoctorServerMsg.FINISH_CASE);

	}

	protected void setSpecificExpectedMsg(DoctorServerMsg msg) {

		emsg.clear();
		emsg.add(msg);
		expectingSpecificMsg = true;
	}

	protected void clearSpecificExpectedMsg() {

		emsg.clear();
		expectingSpecificMsg = false;
	}

	protected void addSpecificExpectedMsg(DoctorServerMsg msg) {

		emsg.add(msg);
	}

	protected void removeSpecificExpectedMsg(DoctorServerMsg msg) {

		emsg.remove(msg);
	}

	protected boolean isExpectingMsg(DoctorServerMsg msg) {

		return emsg.contains(msg);
	}

	// -------------------- message protocol --------------------------

	// part 1: register, login, logout

	protected void onLogin(String content) throws IOException,
			ProtocolErrorException {

		if (state != ConnectionState.INIT
				|| !isExpectingMsg(DoctorServerMsg.LOGIN))
			throw new InvalidProtocolStateException(
					"Spurious Login message received in the middle!!!");

		// content = <userID#password>

		String[] contents = content.split("#");
		if (contents.length != 2) {
			throw new ProtocolErrorException(
					"Invalid message content for <Login>!!!");
		}

		// if id and password match, accept
		// otherwise reject
		int id = 0;
		try {
			id = Integer.parseInt(contents[0]);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid doctor ID in <Login>!!! doctor ID = "
							+ contents[0]);
		}
		String password = contents[1];

		boolean result = dm.checkDoctorLoginRequest(id, password);

		sendLoginAck(result);

		if (result) {
			dm.markDoctorOnline(id);
			doctorID = id;
			state = ConnectionState.ONLINE;
			setExpectedMsgOnline();

			sccm.addServerClientConnection(doctorID, this);
		}
	}

	protected void sendLoginAck(boolean result) throws IOException {

		String content = result ? "OK!" : "Error!";

		sendMsg("LoginAck#" + content);

		System.out.println("LoginAck sent. content = <" + content + ">");
	}

	protected void onLogout(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("Logout received. content = <" + content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(DoctorServerMsg.LOGOUT))
			throw new InvalidProtocolStateException(
					"Logout received in the middle of communication!!!");

		sendLogoutAck();

		dm.markDoctorOffline(doctorID);
		doctorID = -1;
		state = ConnectionState.INIT;
		setExpectedMsgInit();

		sccm.removeServerClientConnection(doctorID, this);
	}

	protected void sendLogoutAck() throws IOException {

		String content = "";

		sendMsg("LogoutAck#" + content);

		System.out.println("LogoutAck sent. content = <" + content + ">");

		closeConnection();
	}

	// part 2: query profile, query case list, query doctor list

	protected void onQueryPatientProfile(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("QueryPatientProfile received. content = <"
				+ content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(DoctorServerMsg.QUERY_PATIENT_PROFILE))
			throw new InvalidProtocolStateException(
					"Spurious QueryPatientProfile message received in the middle!!!");

		// content = <patientID>

		int pid = 0;
		try {
			pid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid patient ID in <QueryPatientProfile>!!! patient ID = "
							+ content);
		}

		Patient p = pm.findPatientByID(pid);

		sendQueryPatientProfileAck(p);
	}

	protected void sendQueryPatientProfileAck(Patient p) throws IOException {

		PatientInfo pi = (PatientInfo) p;

		String content = "";

		if (pi != null) {
			content = pi.toString();
		} else {
			content = "null";
		}

		// <QueryPatientProfileAck#PatientInfo.toString()>

		sendMsg("QueryPatientProfileAck#" + content);

		System.out.println("QueryPatientProfileAck sent. content = <" + content
				+ ">");
	}

	protected void onQueryAllCaseIDOfOnePatient(String content)
			throws IOException, ProtocolErrorException {

		System.out.println("QueryAllCaseIDOfOnePatient received. content = <"
				+ content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(DoctorServerMsg.QUERY_ALL_CASE_ID_OF_ONE_PATIENT))
			throw new InvalidProtocolStateException(
					"Spurious QueryAllCaseIDOfOnePatient message received in the middle!!!");

		// content = <patientID>

		int pid = 0;
		try {
			pid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid patient ID in <QueryAllCaseIDOfOnePatient>!!! patient ID = "
							+ content);
		}

		List<Case> cases = casem.listCasesByPatientID(pid);

		sendQueryAllCaseIDOfOnePatientAck(cases);
	}

	protected void sendQueryAllCaseIDOfOnePatientAck(List<Case> cases)
			throws IOException {

		StringBuilder sb = new StringBuilder();
		sb.append("QueryAllCaseIDOfOnePatientAck#");
		if (cases != null && !cases.isEmpty()) {
			for (Case c : cases) {
				sb.append(c.getCaseID());
				sb.append("$");
				sb.append(c.getSymptom());
				sb.append("$");
				sb.append(c.getDateTime());
				sb.append("#");
			}
			// Remove the last "#"
			sb.deleteCharAt(sb.length() - 1);
		} else {
			sb.append("null");
		}

		// <QueryAllCaseIDOfOnePatientAck#[caseID$symptom$dateTime]+>
		// or <QueryAllCaseIDOfOnePatientAck#null>

		sendMsg(sb.toString());

		System.out.println("QueryAllCaseIDOfOnePatientAck sent");
	}

	protected void onQueryCase(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("QueryCase received. content = <" + content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(DoctorServerMsg.QUERY_CASE))
			throw new InvalidProtocolStateException(
					"Spurious QueryCase message received in the middle!!!");

		// content = <caseID>

		int cid = 0;
		try {
			cid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid case ID in <QueryCase>!!! case ID = " + content);
		}

		Case c = casem.findCaseByID(cid);

		sendQueryCaseAck(c);
	}

	protected void sendQueryCaseAck(Case c) throws IOException {

		CaseInfo ci = (CaseInfo) c;

		String content = "";

		if (ci != null) {
			content = ci.toString();
		} else {
			content = "null";
		}

		// <QueryCaseAck#CaseInfo.toString()>

		sendMsg("QueryCaseAck#" + content);

		System.out.println("QueryCaseAck sent. content = <" + content + ">");

	}

	protected void onQueryAllLabMeasurementIDOfOnePatient(String content)
			throws IOException, ProtocolErrorException {

		System.out
				.println("QueryAllLabMeasurementIDOfOnePatient received. content = <"
						+ content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(DoctorServerMsg.QUERY_ALL_LAB_MEASUREMENT_ID_OF_ONE_PATIENT))
			throw new InvalidProtocolStateException(
					"Spurious QueryAllLabMeasurementIDOfOnePatient message received in the middle!!!");

		// content = <patientID>

		int pid = 0;
		try {
			pid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid patient ID in <QueryAllLabMeasurementIDOfOnePatient>!!! patient ID = "
							+ content);
		}

		List<LabMeasurement> labs = labm
				.findAllLabMeasurementOfGivenPatient(pid);

		sendQueryAllLabMeasurementIDOfOnePatientAck(labs);
	}

	protected void sendQueryAllLabMeasurementIDOfOnePatientAck(
			List<LabMeasurement> labs) throws IOException {

		StringBuilder sb = new StringBuilder();
		sb.append("QueryAllLabMeasurementIDOfOnePatientAck#");
		if (labs != null && !labs.isEmpty()) {
			for (LabMeasurement l : labs) {
				sb.append(l.getLabMeasurementID());
				sb.append("$");
				sb.append(l.getType());
				sb.append("$");
				sb.append(l.getDateTime());
				sb.append("#");
			}
			sb.deleteCharAt(sb.length() - 1);
		} else {
			sb.append("null");
		}

		// <QueryAllLabMeasurementIDOfOnePatientAck#[labID$type$dateTime]+>

		sendMsg(sb.toString());

		System.out.println("QueryAllLabMeasurementIDOfOnePatientAck sent");
	}

	protected void onQueryLabMeasurement(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("QueryLabMeasurement received. content = <"
				+ content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(DoctorServerMsg.QUERY_LAB_MEASUREMENT))
			throw new InvalidProtocolStateException(
					"Spurious QueryLabMeasurement message received in the middle!!!");

		// content = <labID>

		int labID = 0;
		LabMeasurement lm = labm.findLabMeasurementByID(labID);

		sendQueryLabMeasurementAck(lm);
	}

	protected void sendQueryLabMeasurementAck(LabMeasurement lm)
			throws IOException {

		LabMeasurementInfo li = (LabMeasurementInfo) lm;

		String content = "";

		if (li != null) {
			content = li.toString();
		} else {
			content = "null";
		}

		// <QueryLabMeasurementAck#LabMeasurementInfo.toString()>

		sendMsg("QueryLabMeasurementAck#" + content);

		System.out.println("QueryLabMeasurementAck sent. content = <" + content
				+ ">");

	}

	protected void onQueryChatHistory(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("QueryChatHistory received. content = <" + content
				+ ">");

		if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
				|| !isExpectingMsg(DoctorServerMsg.QUERY_CHAT_HISTORY))
			throw new InvalidProtocolStateException(
					"Spurious QueryChatHistory message received in the middle!!!");

		// content = <caseID>

		int cid = 0;
		try {
			cid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid case ID in <QueryChatHistory>!!! case ID = "
							+ content);
		}
		// request chat history from the database
		List<Chat> chatHistory = chatm.findChatHistory(cid);

		// serialize chat history and send ack

		sendQueryChatHistoryAck(chatHistory);
	}

	protected void sendQueryChatHistoryAck(List<Chat> chats) throws IOException {

		StringBuffer sb = new StringBuffer();
		sb.append("QueryChatHistoryAck#");
		if (chats != null && !chats.isEmpty()) {
			for (Chat c : chats) {

				sb.append(((ChatInfo) c).toString());
				sb.append("#");
			}
			// Remove the last "#"
			sb.deleteCharAt(sb.length() - 1);
		} else {
			sb.append("null");
		}

		// <QueryChatHistoryAck#ChatInfo.toString()#ChatInfo.toString()#...>
		// or <QueryChatHistoryAck#null>

		sendMsg(sb.toString());

		System.out.println("QueryChatHistoryAck sent");

	}

	// part 3: handle case, link case and lab measurement

	// CAUTION: this function is called by server patient connection
	protected void sendForwardHandleCase(Case c) throws IOException {

		CaseInfo ci = (CaseInfo) c;

		patientID = c.getPatientID();
		currentCase = c;

		sendMsg("ForwardHandleCase#" + ci.toString());

		System.out.println("ForwardHandleCase sent");

		addSpecificExpectedMsg(DoctorServerMsg.FORWARD_HANDLE_CASE_ACK);
	}

	protected void onForwardHandleCaseAck(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("ForwardHandleCaseAck received.");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(DoctorServerMsg.FORWARD_HANDLE_CASE_ACK))
			throw new InvalidProtocolStateException(
					"Spurious ForwardHandleCaseAck message received in the middle!!!");

		removeSpecificExpectedMsg(DoctorServerMsg.FORWARD_HANDLE_CASE_ACK);

		ServerPatientConnection spc = sccm
				.findServerPatientConnection(patientID);

		// TODO: handle exception caused by another connection

		if (spc != null) {
			if (currentCase.isInitial()) {
				spc.sendCreateCaseAck(true, null);
			} else if (currentCase.isSuspended()) {
				spc.sendResumeCaseAck(true, null);
			} else {
				System.out.println("BUG!!! Invalid case state in "
						+ "onForwardHandleCaseAck");
				// BUG!!!!
			}

			casem.markCaseProcessing(currentCase.getCaseID());

			state = ConnectionState.IN_CASE;
			setExpectedMsgInCaseProcessing();

		} else {
			System.out.println("BUG!!! Missing patient connection!");
		}

	}

	protected void onLinkCaseToCurrentCase(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("LinkCaseToCurrentCase received. content = <"
				+ content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(DoctorServerMsg.LINK_CASE_TO_CURRENT_CASE))
			throw new InvalidProtocolStateException(
					"Spurious LinkCaseToCurrentCase message received in the middle!!!");

		// content = <caseID>

		int cid = 0;
		try {
			cid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid case ID in <LinkCaseToCurrentCase>!!! case ID = "
							+ content);
		}

		boolean result = casem.linkRefCase(currentCase.getCaseID(), cid);

		sendLinkCaseToCurrentCaseAck(result);
	}

	protected void sendLinkCaseToCurrentCaseAck(boolean result)
			throws IOException {

		String content = result ? "OK!" : "Error!";

		sendMsg("LinkCaseToCurrentCaseAck#" + content);

		System.out.println("LinkCaseToCurrentCaseAck sent. content = <"
				+ content + ">");
	}

	protected void onLinkLabMeasurementToCurrentCase(String content)
			throws IOException, ProtocolErrorException {

		System.out.println("LinkLabMeasurementToCurrentCase received. "
				+ "content = <" + content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(DoctorServerMsg.LINK_LAB_MEASUREMENT_TO_CURRENT_CASE))
			throw new InvalidProtocolStateException(
					"Spurious LinkLabMeasurementToCurrentCase message received in the middle!!!");

		// content = <labID>

		int lid = 0;
		try {
			lid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid lab ID in <LinkLabMeasurementToCurrentCase>!!! case ID = "
							+ content);
		}

		boolean result = false;

		LabMeasurement lm = labm.findLabMeasurementByID(lid);
		if (lm != null && lm.getPatientID() != patientID) {
			casem.linkRefLabMeasurement(currentCase.getCaseID(),
					lm.getLabMeasurementID());
			result = true;
		} else {
			result = false;
		}

		sendLinkLabMeasurementToCurrentCaseAck(result);
	}

	protected void sendLinkLabMeasurementToCurrentCaseAck(boolean result)
			throws IOException {

		String content = result ? "OK!" : "Error!";

		sendMsg("LinkLabMeasurementToCurrentCaseAck#" + content);

		System.out.println("LinkLabMeasurementToCurrentCaseAck sent. "
				+ "content = <" + content + ">");
	}

	// part 4: chat message

	protected void onChatMessage(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("ChatMessage received. content = <" + content
				+ ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(DoctorServerMsg.CHAT_MESSAGE))
			throw new InvalidProtocolStateException(
					"Spurious ChatMessage message received in the middle!!!");

		chatm.logChat(currentCase.getCaseID(), patientID, doctorID,
				Chat.DOCTOR_TO_PATIENT, content);

		ServerPatientConnection spc = sccm
				.findServerPatientConnection(patientID);
		if (spc != null) {
			spc.forwardChatMessage(content); // to doctor
		} else {

		}

		// TODO: handle exception caused by the other end

	}

	protected void forwardChatMessage(String msg) throws IOException {

		sendMsg("ForwardChatMessage#" + msg);
	}

	// part 5: suspend and finish case

	// CAUTION: this function is called by patient-server connection
	protected void forwardSuspendCaseByPatient(String content)
			throws IOException {

		sendMsg("ForwardSuspendCaseByPatient#" + content);

		addSpecificExpectedMsg(DoctorServerMsg.FORWARD_SUSPEND_CASE_BY_PATIENT_ACK);

		System.out.println("ForwardSuspendCaseByPatient sent.");
	}

	protected void onForwardSuspendCaseByPatientAck(String content)
			throws IOException, InvalidProtocolStateException {

		System.out
				.println("ForwardSuspendCaseByPatientAck received. content = <"
						+ content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(DoctorServerMsg.FORWARD_SUSPEND_CASE_BY_PATIENT_ACK))
			throw new InvalidProtocolStateException(
					"Spurious ForwardSuspendCaseByPatientAck message received in the middle!!!");

		removeSpecificExpectedMsg(DoctorServerMsg.FORWARD_SUSPEND_CASE_BY_PATIENT_ACK);

		// change state here
		state = ConnectionState.ONLINE;
		setExpectedMsgOnline();
		currentCase = null;
		patientID = -1;

	}

	protected void onSuspendCaseByDoctor(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("SuspendCaseByDoctor received. content = <"
				+ content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(DoctorServerMsg.SUSPEND_CASE_BY_DOCTOR))
			throw new InvalidProtocolStateException(
					"Spurious SuspendCaseByDoctor message received in the middle!!!");

		casem.suspendCase(currentCase.getCaseID());

		ServerPatientConnection spc = sccm
				.findServerPatientConnection(patientID);
		if (spc != null) {
			spc.forwardSuspendCaseByDoctor(content);
		} else {

		}

		sendSuspendCaseByDoctorAck();

		state = ConnectionState.ONLINE;
		setExpectedMsgOnline();
		currentCase = null;
		patientID = -1;

	}

	protected void sendSuspendCaseByDoctorAck() throws IOException {

		sendMsg("SuspendCaseByDoctorAck#");
	}

	protected void onFinishCase(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("FinishCase received. content = <" + content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(DoctorServerMsg.FINISH_CASE))
			throw new InvalidProtocolStateException(
					"Spurious FinishCase message received in the middle!!!");

		casem.finishCase(currentCase.getCaseID(), content);
		ServerPatientConnection spc = sccm
				.findServerPatientConnection(patientID);

		if (spc != null) {
			spc.forwardFinishCase(content);
		} else {

		}
	}

	// CAUTION: this function is called by patient doctor connection
	protected void sendFinishCaseAck(String evaluation) throws IOException {

		sendMsg("FinishCaseAck#");

		state = ConnectionState.ONLINE;
		setExpectedMsgOnline();
		currentCase = null;
		patientID = -1;

	}

}
