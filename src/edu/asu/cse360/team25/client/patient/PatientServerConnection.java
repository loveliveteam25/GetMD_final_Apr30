package edu.asu.cse360.team25.client.patient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.asu.cse360.team25.client.ClientServerConnection;
import edu.asu.cse360.team25.client.patient.PatientMainFrame;
import edu.asu.cse360.team25.protocol.CaseInfo;
import edu.asu.cse360.team25.protocol.ChatInfo;
import edu.asu.cse360.team25.protocol.DoctorInfo;
import edu.asu.cse360.team25.protocol.PatientInfo;
import edu.asu.cse360.team25.protocol.PatientServerMsg;
import edu.asu.cse360.team25.protocol.exception.InvalidDataRecordException;
import edu.asu.cse360.team25.protocol.exception.InvalidProtocolStateException;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;

public class PatientServerConnection extends ClientServerConnection {

    protected static final int patientListeningPort = 10230;
    protected static final String serverAddress = "localhost";

    protected PatientClient pc;

    protected PatientMainFrame pmf;

    protected int patientID;

    // doctor ID in the processing case
    protected int doctorID;

    // current processing case
    protected CaseInfo currentCaseInfo;

    protected ConnectionState state = ConnectionState.INIT;

    protected boolean expectingSpecificMsg = false;
    protected HashSet<PatientServerMsg> emsg = new HashSet<PatientServerMsg>();

    protected static enum ConnectionState {

        INIT, ONLINE, IN_CASE
    }

	// message sent from patient and received by server
    public PatientServerConnection(PatientClient pc) {

        super(serverAddress, patientListeningPort);

        state = ConnectionState.INIT;

        this.pc = pc;
    }

//	public void setPatientMainFrame(PatientMainFrame pmf) {
//
//		this.pmf = pmf;
//		state = ConnectionState.INIT;
//	}
    @Override
    protected void dispatchReceivedMsg(String msg) throws IOException,
            ProtocolErrorException {

		// message format: <XXXX#content>,
        // where XXXX denotes the message type as readable string.
        int mark = msg.indexOf('#');
        String type = msg.substring(0, mark);
        String content = msg.substring(mark + 1);
        if (type.equals("LoginAck")) {
            onLoginAck(content);
        } else if (type.equals("RegisterAck")) {
            onRegisterAck(content);
        } else if (type.equals("LogoutAck")) {
            onLogoutAck(content);
        } else if (type.equals("QueryPatientProfileAck")) {
            onQueryPatientProfileAck(content);
        } else if (type.equals("UpdatePatientProfileAck")) {
            onUpdatePatientProfileAck(content);
        } else if (type.equals("QueryDoctorListAck")) {
            onQueryDoctorListAck(content);
        } else if (type.equals("QueryCaseListAck")) {
            onQueryCaseListAck(content);
        } else if (type.equals("QueryChatHistoryAck")) {
            onQueryChatHistoryAck(content);
        } else if (type.equals("CreateCaseAck")) {
            onCreateCaseAck(content);
        } else if (type.equals("ResumeCaseAck")) {
            onResumeCaseAck(content);
        } else if (type.equals("ForwardChatMessage")) {
            onForwardChatMessage(content);
        } else if (type.equals("SuspendCaseByPatientAck")) {
            onSuspendCaseByPatientAck(content);
        } else if (type.equals("ForwardSuspendCaseByDoctor")) {
            onForwardSuspendCaseByDoctor(content);
        } else if (type.equals("ForwardFinishCase")) {
            onForwardFinishCase(content);
        } else {
            throw new ProtocolErrorException(
                    "Unrecognized message received!");
        }

    }

    protected void setExpectedMsgInCaseProcessing() {

        emsg.clear();
        emsg.add(PatientServerMsg.FORWARD_CHAT_MESSAGE);
        emsg.add(PatientServerMsg.FORWARD_SUSPEND_CASE_BY_DOCTOR);
        emsg.add(PatientServerMsg.FORWARD_FINISH_CASE);

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
    public void sendRegister(String password, String name, String gender,
            String height, String weight, String birthday) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.INIT) {
            throw new InvalidProtocolStateException(
                    "Can not send register message in the middle!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.REGISTER_ACK);

		// <Register#password#name#gender#height#weight#birthday>
        sendMsg("Register#" + password + "#" + name + "#" + gender + "#"
                + height + "#" + weight + "#" + birthday);

        System.out.println("Register sent.");

    }

    protected void onRegisterAck(String content) throws ProtocolErrorException {

        System.out.println("RegisterAck received. content = <" + content + ">");

        if (state != ConnectionState.INIT
                || !isExpectingMsg(PatientServerMsg.REGISTER_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious RegisterAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// content = <userID>
        int pid = 0;
        try {
            pid = Integer.parseInt(content);
        } catch (NumberFormatException e) {
            throw new ProtocolErrorException(
                    "Invalid patient ID in <RegisterAck>!!! case ID = "
                    + content);
        }

        pc.patientID = pid;
        pc.signupOK = true;

        synchronized (pc) {
            pc.notify();
        }

    }

    public void sendLogin(int id, String password) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.INIT) {
            throw new InvalidProtocolStateException(
                    "Can not send login message in the middle!!!");
        }

        patientID = id;

        setSpecificExpectedMsg(PatientServerMsg.LOGIN_ACK);

		// <Login#PatientID#Password>
        sendMsg("Login#" + id + "#" + password);

        System.out.println("Login sent.");
    }

    
    public void switchStatus(){
        if(state==ConnectionState.IN_CASE)
        {
            state=ConnectionState.ONLINE;
        }
        else if(state==ConnectionState.ONLINE)
        {
            state=ConnectionState.IN_CASE;
        }
    }
    protected void onLoginAck(String content)
            throws InvalidProtocolStateException {

        System.out.println("LoginAck received. content = <" + content + ">");

        if (state != ConnectionState.INIT
                || !isExpectingMsg(PatientServerMsg.LOGIN_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious login message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// content = <OK!> or <Error!>
        if (content.equals("OK!")) {
            state = ConnectionState.ONLINE;
            pc.loginOK = true;
        } else {
            patientID = -1;
            state = ConnectionState.INIT;
            pc.loginOK = false;
        }

        synchronized (pc) {
            pc.notify();
        }

		// enter the main page
    }

    public void sendLogout() throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send logout message when processing case or before login!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.LOGOUT_ACK);

		// <Logout#>
        sendMsg("Logout#");

    }

    protected void onLogoutAck(String content)
            throws InvalidProtocolStateException {

        System.out.println("LogoutAck received. content = <" + content + ">");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(PatientServerMsg.LOGOUT_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious LogoutAck message received in the middle!!!");
        }

		// <LogoutAck#>
        state = ConnectionState.INIT;

        clearSpecificExpectedMsg();

    }

	// part 2: query / update profile, query case list, query doctor list
    public void sendQueryPatientProfile() throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryPatientProfile message when processing case or before login!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.QUERY_PATIENT_PROFILE_ACK);

		// <QueryPatientProfile#>
        sendMsg("QueryPatientProfile#");

    }

    protected void onQueryPatientProfileAck(String content)
            throws ProtocolErrorException {

        System.out.println("QueryPatientProfileAck received. content = <"
                + content + ">");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(PatientServerMsg.QUERY_PATIENT_PROFILE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryPatientProfileAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// display patient profile
        PatientInfo pi = new PatientInfo(content);

        pc.setPatientInfo(pi);

        System.out.println("\t" + pi.toString());

        synchronized (pc) {
            pc.notify();
        }
        
    }

    public void sendUpdatePatientProfile(String name, String gender,
            String height, String weight, String birthday) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send UpdatePatientProfile message when processing case or before login!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.UPDATE_PATIENT_PROFILE_ACK);

		// <UpdatePatientProfile#Name#Gender#Height#Weight#birthday>
        sendMsg("UpdatePatientProfile#" + name + "#" + gender + "#" + height
                + "#" + weight + "#" + birthday);

        pc.piUpdate = new PatientInfo(patientID, "******", name, gender, height, weight, birthday);
    }

    protected void onUpdatePatientProfileAck(String content)
            throws InvalidProtocolStateException {

        System.out.println("UpdatePatientProfileAck received. content = <"
                + content + ">");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(PatientServerMsg.UPDATE_PATIENT_PROFILE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious UpdatePatientProfileAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// content = <OK!> or <Error!>
        if (content.equals("OK!")) {
            pc.pi = pc.piUpdate;
            pmf.showPatientInfo();
        }

    }

    public void sendQueryDoctorList(String department, String expertise)
            throws IOException, ProtocolErrorException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send UpdatePatientProfile message when processing case or before login!!!");
        }

        if (department.contains("#") || expertise.contains("#")) {

            throw new ProtocolErrorException(
                    "Invalid department string or expertise string!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.QUERY_DOCTOR_LIST_ACK);

		// <QueryDoctor#department#expertise>, note that both part can be "*"
        sendMsg("QueryDoctorList#" + department + "#" + expertise);

    }

    protected void onQueryDoctorListAck(String content)
            throws InvalidProtocolStateException, InvalidDataRecordException {

        System.out.println("QueryDoctorListAck received.");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(PatientServerMsg.QUERY_DOCTOR_LIST_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryDoctorAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// content = <DoctorInfo.toString()#...> or <null>
        if (content.equals("null")) {
            
            // display empty list
            pc.doctors = new ArrayList<DoctorInfo>();
            pmf.showDoctorList();
        } else {

            String[] contents = content.split("#");

            List<DoctorInfo> docList = new ArrayList<DoctorInfo>();

            for (int i = 0; i < contents.length; i++) {
                DoctorInfo di = new DoctorInfo(contents[i]);
                docList.add(di);
            }

            pc.doctors = docList;
            pmf.showDoctorList();
        }
    }

    public void sendQueryCaseList() throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryCaseList message when processing case or before login!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.QUERY_CASE_LIST_ACK);

		// <QueryCaseList#>
        sendMsg("QueryCaseList#");

    }

    protected void onQueryCaseListAck(String content)
            throws InvalidProtocolStateException, InvalidDataRecordException {

        System.out.println("QueryCaseListAck received. content = <" + content
                + ">");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(PatientServerMsg.QUERY_CASE_LIST_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryCaseListAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// content = <CaseInfo.toString()#...> or <null>
        if (content.equals("null")) {

        } else {

            String[] contents = content.split("#");

            List<CaseInfo> caseList = new ArrayList<CaseInfo>();

            for (int i = 0; i < contents.length; i++) {
                CaseInfo ci = new CaseInfo(contents[i]);
                caseList.add(ci);
            }

            pc.cases = caseList;
            pmf.showCaseList();
        }
        
        synchronized (pc) {
            pc.notify();
        }
    }

    public void sendQueryChatHistory(int caseID) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryChatHistory message when processing case or before login!!!");
        }

        addSpecificExpectedMsg(PatientServerMsg.QUERY_CHAT_HISTORY_ACK);

		// <QueryChatHistory#caseID>
        sendMsg("QueryChatHistory#" + caseID);

    }

    protected void onQueryChatHistoryAck(String content)
            throws InvalidProtocolStateException, InvalidDataRecordException {

        System.out.println("QueryChatHistoryAck received.");

        if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
                || !isExpectingMsg(PatientServerMsg.QUERY_CHAT_HISTORY_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryChatHistoryAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(PatientServerMsg.QUERY_CHAT_HISTORY_ACK);

        if (content.equals("null")) {
            
            // show blank
            pc.chatList = new ArrayList<ChatInfo>();
            pmf.showChatHistory();
            
        } else {

            String[] contents = content.split("#");

            List<ChatInfo> chatList = new ArrayList<ChatInfo>();

            for (int i = 0; i < contents.length; i++) {
                ChatInfo ci = new ChatInfo(contents[i]);
                chatList.add(ci);
            }

            pc.chatList = chatList;
            pmf.showChatHistory();
        }
    }

	// part 3: create / resume case
    public void sendCreateCase(String painLevel, String symptom, int doctorID)
            throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send CreateCase message when processing case or before login!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.CREATE_CASE_ACK);

		// <CreateCase#symptom#painLevel#doctorID>
        sendMsg("CreateCase#" + symptom + "#" + painLevel + "#" + doctorID);

    }

    protected void onCreateCaseAck(String content)
            throws ProtocolErrorException {

        System.out.println("CreateCaseAck received. content = <" + content
                + ">");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(PatientServerMsg.CREATE_CASE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious CreateCaseAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// content = <OK!#CaseInfo.toString()> or <Error!>
        String[] contents = content.split("#");
        if (contents.length != 2) {
            throw new ProtocolErrorException("Invalid content in CreateCaseAck. content = " + content);
        }

        if (contents[0].equals("OK!")) {

            state = ConnectionState.IN_CASE;
            System.out.println("State has been changed to IN_CASE");
            setExpectedMsgInCaseProcessing();

            currentCaseInfo = new CaseInfo(contents[1]);

        } else {

			// Stay at ON_LINE state
        }

        // display case created
    }

    public void sendResumeCase(int caseID) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send ResumeCase message when processing case or before login!!!");
        }

        setSpecificExpectedMsg(PatientServerMsg.RESUME_CASE_ACK);

		// <ResumeCase#caseID>
        sendMsg("ResumeCase#" + caseID);

    }

    protected void onResumeCaseAck(String content)
            throws ProtocolErrorException {

        System.out.println("ResumeCaseAck received. content = <" + content
                + ">");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(PatientServerMsg.RESUME_CASE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious ResumeCaseAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();

		// content = <OK!#CaseInfo.toString()> or <Error!>
        String[] contents = content.split("#");
        if (contents.length != 2) {
            throw new ProtocolErrorException("Invalid content in ResumeCaseAck. content = " + content);
        }

        if (contents[0].equals("OK!")) {

            state = ConnectionState.IN_CASE;
            setExpectedMsgInCaseProcessing();

            currentCaseInfo = new CaseInfo(contents[1]);

        } else {

			// Stay at ON_LINE state
        }

        // display case resumed
    }

	// part 4: chat message
    public void sendChatMessage(String cmsg) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send ChatMessage message when not processing case or before login!!!");
        }

		// <ChatMessage#Message>
        sendMsg("ChatMessage#" + cmsg);

        // no change for expected message
    }

    protected void onForwardChatMessage(String content)
            throws InvalidProtocolStateException {

        System.out.println("ForwardChatMessage received." + " content = <"
                + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(PatientServerMsg.FORWARD_CHAT_MESSAGE)) {
            throw new InvalidProtocolStateException(
                    "Spurious ForwardChatMessage message received in the middle!!!");
        }
        
        pmf.showChatReceived(content);

		// No change on expected message
        // display the chat content
    }

	// part 5: suspend / finish case
    public void sendSuspendCaseByPatient(String reason) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send SuspendCaseByPatient message when not processing case!!!");
        }

        addSpecificExpectedMsg(PatientServerMsg.SUSPEND_CASE_BY_PATIENT_ACK);

		// <SuspendCaseByPatient#reason>
        sendMsg("SuspendCaseByPatient#" + reason);

    }

    protected void onSuspendCaseByPatientAck(String content)
            throws InvalidProtocolStateException {

        System.out.println("SuspendCaseByPatientAck received. content = <"
                + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(PatientServerMsg.SUSPEND_CASE_BY_PATIENT_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious SuspendCaseByPatientAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(PatientServerMsg.SUSPEND_CASE_BY_PATIENT_ACK);

        state = ConnectionState.ONLINE;

        // Temporary quit by the patient
    }

    public void onForwardSuspendCaseByDoctor(String content)
            throws InvalidProtocolStateException, IOException {

        System.out.println("SuspendCaseByDoctor received. content = <"
                + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(PatientServerMsg.FORWARD_SUSPEND_CASE_BY_DOCTOR)) {
            throw new InvalidProtocolStateException(
                    "Spurious SuspendCaseByDoctor message received in the middle!!!");
        }

		// content = <>
        sendForwardSuspendCaseByDoctorAck();

        // change state here
        state = ConnectionState.ONLINE;
        clearSpecificExpectedMsg();

    }

    protected void sendForwardSuspendCaseByDoctorAck() throws IOException,
            InvalidProtocolStateException {

		// <SuspendCaseByDoctorAck#>
        sendMsg("ForwardSuspendCaseByDoctorAck#");

    }

    protected void onForwardFinishCase(String content) throws IOException,
            InvalidProtocolStateException {

        System.out.println("ForwardFinishCase received. content = <" + content + ">");

        pmf.finishable=true;
        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(PatientServerMsg.FORWARD_FINISH_CASE)) {
            throw new InvalidProtocolStateException(
                    "Spurious FinishCase message received in the middle!!!");
        }

    }

    public void sendForwardFinishCaseAck(int rate) throws IOException,
            InvalidProtocolStateException {

        state = ConnectionState.ONLINE;
        clearSpecificExpectedMsg();
        currentCaseInfo = null;

		// <FinishCaseAck#rate>
        sendMsg("ForwardFinishCaseAck#" + rate);

    }

}
