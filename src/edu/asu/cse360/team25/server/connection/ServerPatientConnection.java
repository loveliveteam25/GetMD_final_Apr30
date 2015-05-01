package edu.asu.cse360.team25.server.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

import edu.asu.cse360.team25.protocol.CaseInfo;
import edu.asu.cse360.team25.protocol.ChatInfo;
import edu.asu.cse360.team25.protocol.DoctorInfo;
import edu.asu.cse360.team25.protocol.PatientInfo;
import edu.asu.cse360.team25.protocol.PatientServerMsg;
import edu.asu.cse360.team25.protocol.exception.InvalidProtocolStateException;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;
import edu.asu.cse360.team25.server.Case;
import edu.asu.cse360.team25.server.Chat;
import edu.asu.cse360.team25.server.Doctor;
import edu.asu.cse360.team25.server.Patient;
import edu.asu.cse360.team25.server.Server;

public class ServerPatientConnection extends ServerClientConnection {

	protected int patientID = -1;

	// doctor ID in current processing case
	protected int doctorID = -1;

	// current processing case
	protected Case currentCase = null;

	protected ConnectionState state = ConnectionState.INIT;

	protected boolean expectingSpecificMsg = false;
	protected HashSet<PatientServerMsg> emsg = new HashSet<PatientServerMsg>();

	protected static enum ConnectionState {
		INIT, ONLINE, IN_CASE
	}

	public ServerPatientConnection(Socket socket, Server server)
			throws IOException {

		super(socket, server);

		state = ConnectionState.INIT;
		setExpectedMsgInit();
	}

	@Override
	public void cleanup() throws IOException {

		if(patientID != -1) {
			sccm.removeServerClientConnection(patientID, this);
		}
		
		if(doctorID != -1) {
			ServerDoctorConnection sdc = sccm
					.findServerDoctorConnection(doctorID);
			if(sdc != null)
				sdc.closeConnection();
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

		if (type.equals("Register"))
			onRegister(content);
		else if (type.equals("Login"))
			onLogin(content);
		else if (type.equals("Logout"))
			onLogout(content);
		else if (type.equals("QueryPatientProfile"))
			onQueryPatientProfile(content);
		else if (type.equals("UpdatePatientProfile"))
			onUpdatePatientProfile(content);
		else if (type.equals("QueryDoctorList"))
			onQueryDoctorList(content);
		else if (type.equals("QueryCaseList"))
			onQueryCaseList(content);
		else if (type.equals("QueryChatHistory"))
			onQueryChatHistory(content);
		else if (type.equals("CreateCase"))
			onCreateCase(content);
		else if (type.equals("ResumeCase"))
			onResumeCase(content);
		else if (type.equals("ChatMessage"))
			onChatMessage(content);
		else if (type.equals("SuspendCaseByPatient"))
			onSuspendCaseByPatient(content);
		else if (type.equals("ForwardSuspendCaseByDoctorAck"))
			onForwardSuspendCaseByDoctorAck(content);
		else if (type.equals("ForwardFinishCaseAck"))
			onForwardFinishCaseAck(content);
		else
			throw new ProtocolErrorException(
					"Unrecognized message received on patient side!");
	}

	protected void setExpectedMsgInit() {

		emsg.clear();
		emsg.add(PatientServerMsg.REGISTER);
		emsg.add(PatientServerMsg.LOGIN);

	}

	protected void setExpectedMsgOnline() {

		emsg.clear();
		emsg.add(PatientServerMsg.LOGOUT);
		emsg.add(PatientServerMsg.QUERY_PATIENT_PROFILE);
		emsg.add(PatientServerMsg.UPDATE_PATIENT_PROFILE);
		emsg.add(PatientServerMsg.QUERY_DOCTOR_LIST);
		emsg.add(PatientServerMsg.QUERY_CASE_LIST);
		emsg.add(PatientServerMsg.CREATE_CASE);
		emsg.add(PatientServerMsg.RESUME_CASE);
		emsg.add(PatientServerMsg.QUERY_CHAT_HISTORY);

	}

	protected void setExpectedMsgInCaseProcessing() {

		emsg.clear();
		emsg.add(PatientServerMsg.CHAT_MESSAGE);
		emsg.add(PatientServerMsg.QUERY_CHAT_HISTORY);
		emsg.add(PatientServerMsg.SUSPEND_CASE_BY_PATIENT);
		// emsg.add(PatientServerMsg.FORWARD_SUSPEND_CASE_BY_DOCTOR_ACK);
		// emsg.add(PatientServerMsg.FORWARD_FINISH_CASE_ACK);

	}

	protected void setSpecificExpectedMsg(PatientServerMsg msg) {

		emsg.clear();
		emsg.add(msg);
		expectingSpecificMsg = true;
	}

	protected void clearSpecificExpectedMsg() {

		emsg.clear();
		expectingSpecificMsg = false;
	}

	protected void addSpecificExpectedMsg(PatientServerMsg msg) {

		emsg.add(msg);
	}

	protected void removeSpecificExpectedMsg(PatientServerMsg msg) {

		emsg.remove(msg);
	}

	protected boolean isExpectingMsg(PatientServerMsg msg) {

		return emsg.contains(msg);
	}

	// -------------------- message protocol --------------------------

	// part 1: register, login, logout

	protected void onRegister(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("Register received. content = <" + content + ">");

		if (state != ConnectionState.INIT
				|| !isExpectingMsg(PatientServerMsg.REGISTER))
			throw new InvalidProtocolStateException(
					"Spurious Register message received in the middle!!!");

		// content = <password#name#gender#height#weight#birthday>

		String[] contents = content.split("#");
		if (contents.length != 6) {
			throw new ProtocolErrorException(
					"Invalid message content for <Register>!!! content = "
							+ content);
		}

		int id = pm.registerPatient(contents[0], contents[1], contents[2],
				contents[3], contents[4], contents[5]);

		sendRegisterAck(id);

	}

	protected void sendRegisterAck(int id) throws IOException {

		String content = String.valueOf(id);

		// <RegisterAck#userID>

		sendMsg("RegisterAck#" + content);

		System.out.println("RegisterAck sent. content = <" + content + ">");
	}

	protected void onLogin(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("Login received. content = <" + content + ">");

		if (state != ConnectionState.INIT
				|| !isExpectingMsg(PatientServerMsg.LOGIN))
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
					"Invalid patient ID in <Login>!!! patient ID = "
							+ contents[0]);
		}
		String password = contents[1];

		boolean result = pm.checkPatientLoginReuest(id, password);

		sendLoginAck(result);

		if (result) {
			pm.markPatientLogin(id);
			patientID = id;
			state = ConnectionState.ONLINE;
			setExpectedMsgOnline();

			sccm.addServerClientConnection(patientID, this);
		}
	}

	protected void sendLoginAck(boolean result) throws IOException {

		String content = result ? "OK!" : "Error!";

		// <LoginAck#OK!> or <LoginAck#Error!>

		sendMsg("LoginAck#" + content);

		System.out.println("LoginAck sent. content = <" + content + ">");
	}

	protected void onLogout(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("Logout received. content = <" + content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(PatientServerMsg.LOGOUT))
			throw new InvalidProtocolStateException(
					"Logout received in the middle of communication!!!");

		// content = <>

		sendLogoutAck();

		pm.markPatientLogout(patientID);
		patientID = -1;
		state = ConnectionState.INIT;
		setExpectedMsgInit();
	}

	protected void sendLogoutAck() throws IOException {

		String content = "";

		sendMsg("LogoutAck#" + content);

		System.out.println("LogoutAck sent. content = <" + content + ">");

		closeConnection();

	}

	// part 2: query / update profile, query case list, query doctor list

	protected void onQueryPatientProfile(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("QueryPatientProfile received. content = <"
				+ content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(PatientServerMsg.QUERY_PATIENT_PROFILE))
			throw new InvalidProtocolStateException(
					"Spurious QueryPatientProfile message received in the middle!!!");

		// content = <>

		Patient p = pm.findPatientByID(patientID);

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

	protected void onUpdatePatientProfile(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("UpdatePatientProfile received. content = <"
				+ content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(PatientServerMsg.UPDATE_PATIENT_PROFILE))
			throw new InvalidProtocolStateException(
					"Spurious UpdatePatientProfile message received in the middle!!!");

		// content = <Name#Gender#Height#Weight#BloodType>

		String[] contents = content.split("#");
		if (contents.length != 5) {
			throw new ProtocolErrorException(
					"Invalid message content for <UpdatePatientProfile>!!!");
		}

		boolean result = pm.updatePatientProfile(patientID, contents[0],
				contents[1], contents[2], contents[3], contents[4]);

		sendUpdatePatientProfileAck(result);
	}

	protected void sendUpdatePatientProfileAck(boolean result)
			throws IOException {

		String content = "";

		if (result) {
			content = "OK!";
		} else {
			content = "Error!";
		}

		// <UpdatePatientProfileAck#OK!> or <UpdatePatientProfileAck#Error!>

		sendMsg("UpdatePatientProfileAck#" + content);

		System.out.println("UpdatePatientProfileAck sent. content = <"
				+ content + ">");
	}

	protected void onQueryDoctorList(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("QueryDoctorList received. content = <" + content
				+ ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(PatientServerMsg.QUERY_DOCTOR_LIST))
			throw new InvalidProtocolStateException(
					"Spurious QueryDoctor message received in the middle!!!");

		// content = <department#expertise>, note that both part can be "*"

		String[] contents = content.split("#");
		if (contents.length != 2) {
			throw new ProtocolErrorException(
					"Invalid message content for <QueryDoctor>!!! content = "
							+ content);
		}

		List<Doctor> doctorlist = dm.listDoctors(contents[0], contents[1]);

		// serialize dlist, and send back to the patient

		sendQueryDoctorListAck(doctorlist);

	}

	protected void sendQueryDoctorListAck(List<Doctor> doctorList)
			throws IOException {

		StringBuffer sb = new StringBuffer();
		sb.append("QueryDoctorListAck#");
		if (doctorList != null && !doctorList.isEmpty()) {
			for (Doctor doctor : doctorList) {

				sb.append(((DoctorInfo) doctor).toString());
				sb.append("#");
			}
			// Remove the last "#"
			sb.deleteCharAt(sb.length() - 1);
		} else {
			sb.append("null");
		}

		// <QueryDoctorListAck#DoctorInfo.toString()#DoctorInfo.toString()#...>
		// or <QueryDoctorListAck#null>

		sendMsg(sb.toString());

		System.out.println("QueryDoctorListAck sent.");
	}

	protected void onQueryCaseList(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("QueryCaseList received. content = <" + content
				+ ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(PatientServerMsg.QUERY_CASE_LIST))
			throw new InvalidProtocolStateException(
					"Spurious QueryCaseList message received in the middle!!!");

		// content = <>

		List<Case> caselist = casem.listCasesByPatientID(patientID);
		sendQueryCaseListAck(caselist);
	}

	protected void sendQueryCaseListAck(List<Case> caseList) throws IOException {

		StringBuffer sb = new StringBuffer();
		sb.append("QueryCaseListAck#");
		if (caseList != null && !caseList.isEmpty()) {
			for (Case c : caseList) {

				sb.append(c.toString());
				sb.append("#");
			}
			// Remove the last "#"
			sb.deleteCharAt(sb.length() - 1);
		} else {
			sb.append("null");
		}

		// <QueryCaseListAck#CaseInfo.toString()#CaseInfo.toString()#...>
		// or <QueryCaseListAck#null>

		sendMsg(sb.toString());

		System.out.println("QueryCaseListAck sent");

	}

	protected void onQueryChatHistory(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("QueryChatHistory received. content = <" + content
				+ ">");

		if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
				|| !isExpectingMsg(PatientServerMsg.QUERY_CHAT_HISTORY))
			throw new InvalidProtocolStateException(
					"Spurious QueryChatHistory message received in the middle!!!");

		// content = <caseID>

		// request chat history from the database
		int cid = 0;
		try {
			cid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid case ID in <ResumeCase>!!! case ID = " + content);
		}
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

	// part 3: create / resume case

	protected void onCreateCase(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("CreateCase received. content = <" + content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(PatientServerMsg.CREATE_CASE))
			throw new InvalidProtocolStateException(
					"Spurious CreateCase message received in the middle!!!");

		// content = <symptom#painLevel#doctorID>

		String[] contents = content.split("#");
		if (contents.length != 3) {
			throw new ProtocolErrorException(
					"Invalid message content for <CreateCase>!!! content = "
							+ content);
		}

		int did = 0;
		try {
			did = Integer.parseInt(contents[2]);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid doctor ID in <CreateCase>!!! doctor ID = "
							+ contents[0]);
		}

		String symptom = contents[0];
		String painLevel = contents[1];

		Doctor d = dm.findDoctorByID(did);
		ServerDoctorConnection sdc = sccm.findServerDoctorConnection(did);

		if (d.isFree()) {

			// then create case with the above information
			Case c = casem.createCase(patientID, did, symptom, painLevel);

			doctorID = did;
			currentCase = c;

			// doctor is free implicitly means sdc != null
			sdc.sendForwardHandleCase(c); // to doctor

			// TODO: handle exception caused by the other end

		} else {
			sendCreateCaseAck(false, "Doctor is " + d.getState());
		}

	}

	// CAUTION: this function can be called by server doctor connection
	protected void sendCreateCaseAck(boolean result, String reason)
			throws IOException {

		if (result) {
			state = ConnectionState.IN_CASE;
			setExpectedMsgInCaseProcessing();
			sendMsg("CreateCaseAck#OK!#" + ((CaseInfo) currentCase).toString());

		} else {
			currentCase = null;
			doctorID = -1;
			sendMsg("CreateCaseAck#Error!");
		}

		String content = result ? "OK!" : "Error!";

		// <CreateCaseAck#OK!#CaseInfo.toString()> or <CreateCaseAck#Error!>

		System.out.println("CreateCaseAck sent. content = <" + content + ">");

	}

	protected void onResumeCase(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("ResumeCase received. content = <" + content + ">");

		if (state != ConnectionState.ONLINE
				|| !isExpectingMsg(PatientServerMsg.RESUME_CASE))
			throw new InvalidProtocolStateException(
					"Spurious ResumeCase message received in the middle!!!");

		// content = <caseID>

		int cid = 0;
		try {
			cid = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid case ID in <ResumeCase>!!! case ID = " + content);
		}

		// then resume case with the above information
		Case c = casem.findCaseByID(cid);
		if (c == null) {
			sendResumeCaseAck(false, "No such case.");
			return;
		}

		doctorID = c.getDoctorID();
		currentCase = c;

		Doctor d = dm.findDoctorByID(doctorID);

		if (d.isFree()) {

			ServerDoctorConnection sdc = sccm
					.findServerDoctorConnection(doctorID);

			sdc.sendForwardHandleCase(c); // to doctor

			// TODO: handle exception caused by the other end

		} else {
			sendResumeCaseAck(false, "Doctor is " + d.getState());
		}
	}

	// CAUTION: this function can be called by server doctor connection
	protected void sendResumeCaseAck(boolean result, String reason)
			throws IOException {

		if (result) {
			state = ConnectionState.IN_CASE;
			setExpectedMsgInCaseProcessing();
			sendMsg("ResumeCaseAck#OK!#" + ((CaseInfo) currentCase).toString());

		} else {
			currentCase = null;
			doctorID = -1;
			sendMsg("ResumeCaseAck#Error!" + reason);
		}

		String content = result ? "OK!" : "Error!";

		System.out.println("ResumeCaseAck sent. content = <" + content + ">");

	}

	// part 4: chat message

	protected void onChatMessage(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("ChatMessage received. content = <" + content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(PatientServerMsg.CHAT_MESSAGE))
			throw new InvalidProtocolStateException(
					"Spurious ChatMessage message received in the middle!!!");

		chatm.logChat(currentCase.getCaseID(), patientID, doctorID,
				Chat.PATIENT_TO_DOCTOR, content);

		ServerDoctorConnection sdc = sccm.findServerDoctorConnection(doctorID);
		if (sdc != null) {
			sdc.forwardChatMessage(content); // to doctor
		} else {

		}

		// TODO: handle exception caused by the other end

	}

	protected void forwardChatMessage(String content) throws IOException {

		sendMsg("ForwardChatMessage#" + content);
	}

	// part 5: suspend / finish case

	// CAUTION: no need to wait for doctor to suspend case
	protected void onSuspendCaseByPatient(String content) throws IOException,
			InvalidProtocolStateException {

		System.out.println("SuspendCaseByPatient received. content = <"
				+ content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(PatientServerMsg.SUSPEND_CASE_BY_PATIENT))
			throw new InvalidProtocolStateException(
					"Spurious SuspendCaseByPatient message received in the middle!!!");

		casem.suspendCase(currentCase.getCaseID());

		ServerDoctorConnection sdc = sccm.findServerDoctorConnection(doctorID);
		if (sdc != null) {
			sdc.forwardSuspendCaseByPatient(content);
		} else {
			System.out.println("BUG!!! Doctor connection is missing!!!");
		}

		// TODO: handle exception caused by the other end

		sendSuspendCaseByPatientAck();

		state = ConnectionState.ONLINE;
		setExpectedMsgOnline();

	}

	protected void sendSuspendCaseByPatientAck() throws IOException {

		sendMsg("SuspendCaseByPatientAck#");

		System.out.println("SuspendCaseByPatientAck sent.");

	}

	// CAUTION: this function is called by server-doctor connection
	protected void forwardSuspendCaseByDoctor(String reason) throws IOException {

		sendMsg("ForwardSuspendCaseByDoctor#" + reason);

		System.out.println("ForwardSuspendCaseByDoctor sent. content = <"
				+ reason + ">");

		casem.suspendCase(currentCase.getCaseID());

		addSpecificExpectedMsg(PatientServerMsg.FORWARD_SUSPEND_CASE_BY_DOCTOR_ACK);

	}

	protected void onForwardSuspendCaseByDoctorAck(String content)
			throws IOException, InvalidProtocolStateException {

		System.out.println("SuspendCaseByDoctorAck received. content = <"
				+ content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(PatientServerMsg.FORWARD_SUSPEND_CASE_BY_DOCTOR_ACK))
			throw new InvalidProtocolStateException(
					"Spurious SuspendCaseAck message received in the middle!!!");

		removeSpecificExpectedMsg(PatientServerMsg.FORWARD_SUSPEND_CASE_BY_DOCTOR_ACK);

		state = ConnectionState.ONLINE;
		setExpectedMsgOnline();

	}

	// CAUTION: this function is called by server-doctor connection
	protected void forwardFinishCase(String finalDiagnose) throws IOException {

		sendMsg("ForwardFinishCase#" + finalDiagnose);

		System.out.println("ForwardFinishCase sent. content = <"
				+ finalDiagnose + ">");

		// Note that it is finished in doctor side, so no need to call
		// casem.finishCase() here.
		// casem.finishCase(currentCase.getCaseID(), finalDiagnose);

		addSpecificExpectedMsg(PatientServerMsg.FORWARD_FINISH_CASE_ACK);

	}

	protected void onForwardFinishCaseAck(String content) throws IOException,
			ProtocolErrorException {

		System.out.println("ForwardFinishCaseAck received. content = <"
				+ content + ">");

		if (state != ConnectionState.IN_CASE
				|| !isExpectingMsg(PatientServerMsg.FORWARD_FINISH_CASE_ACK))
			throw new InvalidProtocolStateException(
					"Spurious FinishCaseAck message received in the middle!!!");

		removeSpecificExpectedMsg(PatientServerMsg.FORWARD_FINISH_CASE_ACK);

		int rate = 0;
		try {
			rate = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			throw new ProtocolErrorException(
					"Invalid rate number in <ForwardFinishCaseAck>!!! rate = "
							+ content);
		}

		dm.rateDoctor(doctorID, rate);

		ServerDoctorConnection sdc = sccm.findServerDoctorConnection(doctorID);
		if (sdc != null) {
			sdc.sendFinishCaseAck(content);
		} else {

		}

		state = ConnectionState.ONLINE;
		setExpectedMsgOnline();
	}

}
