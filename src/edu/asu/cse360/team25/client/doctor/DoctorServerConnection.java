package edu.asu.cse360.team25.client.doctor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.asu.cse360.team25.client.ClientServerConnection;
import edu.asu.cse360.team25.protocol.CaseInfo;
import edu.asu.cse360.team25.protocol.ChatInfo;
import edu.asu.cse360.team25.protocol.DoctorServerMsg;
import edu.asu.cse360.team25.protocol.LabMeasurementInfo;
import edu.asu.cse360.team25.protocol.PatientInfo;
import edu.asu.cse360.team25.protocol.exception.InvalidDataRecordException;
import edu.asu.cse360.team25.protocol.exception.InvalidProtocolStateException;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;
import javax.swing.JOptionPane;

public class DoctorServerConnection extends ClientServerConnection {

    protected DoctorClient dc;
    protected DoctorMainFrame dmf;
    protected static final int doctorListeningPort = 10231;
    protected static final String serverAddress = "localhost";

    protected int doctorID;

    // patient ID in the processing case
    protected int patientID;

    // current processing case
    protected CaseInfo currentCaseInfo;

    public ConnectionState state = ConnectionState.INIT;

    protected boolean expectingSpecificMsg = false;
    protected HashSet<DoctorServerMsg> emsg = new HashSet<DoctorServerMsg>();

    protected static enum ConnectionState {

        INIT, ONLINE, IN_CASE
    }

    public DoctorServerConnection(DoctorClient dc) {

        super(serverAddress, doctorListeningPort);

        state = ConnectionState.INIT;

        this.dc = dc;

    }

    @Override
    protected void dispatchReceivedMsg(String msg)
            throws ProtocolErrorException, IOException {

        // message format: <XXXX#content>,
        // where XXXX denotes the message type as readable string.
        int mark = msg.indexOf('#');
        String type = msg.substring(0, mark);
        String content = msg.substring(mark + 1);
        if (type.equals("LoginAck")) {
            onLoginAck(content);
        } else if (type.equals("LogoutAck")) {
            onLogoutAck(content);
        } else if (type.equals("QueryPatientProfileAck")) {
            onQueryPatientProfileAck(content);
        } else if (type.equals("QueryAllCaseIDOfOnePatientAck")) {
            onQueryAllCaseIDOfOnePatientAck(content);
        } else if (type.equals("QueryCaseAck")) {
            onQueryCaseAck(content);
        } else if (type.equals("QueryAllLabMeasurementIDOfOnePatientAck")) {
            onQueryAllLabMeasurementIDOfOnePatientAck(content);
        } else if (type.equals("QueryLabMeasurementAck")) {
            onQueryLabMeasurementAck(content);
        } else if (type.equals("QueryChatHistoryAck")) {
            onQueryChatHistoryAck(content);
        } else if (type.equals("ForwardHandleCase")) {
            onForwardHandleCase(content);
        } else if (type.equals("LinkCaseToCurrentCaseAck")) {
            onLinkCaseToCurrentCaseAck(content);
        } else if (type.equals("LinkLabMeasurementToCurrentCaseAck")) {
            onLinkLabMeasurementToCurrentCaseAck(content);
        } else if (type.equals("ForwardChatMessage")) {
            onForwardChatMessage(content);
        } else if (type.equals("ForwardSuspendCaseByPatient")) {
            onForwardSuspendCaseByPatient(content);
        } else if (type.equals("SuspendCaseByDoctorAck")) {
            onSuspendCaseByDoctorAck(content);
        } else if (type.equals("FinishCaseAck")) {
            onFinishCaseAck(content);
        } else {
            throw new ProtocolErrorException(
                    "Unrecognized message received!");
        }

    }

    protected void setExpectedMsgOnline() {

        emsg.clear();
        emsg.add(DoctorServerMsg.FORWARD_HANDLE_CASE);

    }

    protected void setExpectedMsgInCaseProcessing() {

        emsg.clear();
        emsg.add(DoctorServerMsg.FORWARD_CHAT_MESSAGE);
        emsg.add(DoctorServerMsg.FORWARD_SUSPEND_CASE_BY_PATIENT);

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
    public void sendLogin(int id, String password) throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.INIT) {
            throw new InvalidProtocolStateException(
                    "Can not send login message in the middle!!!");
        }

        doctorID = id;

        setSpecificExpectedMsg(DoctorServerMsg.LOGIN_ACK);

        // <Login#DoctorID#Password>
        sendMsg("Login#" + id + "#" + password);

        System.out.println("Login sent.");

    }

    protected void onLoginAck(String content) throws InvalidProtocolStateException {

        System.out.println("LoginAck received. content = <" + content + ">");

        if (state != ConnectionState.INIT
                || !isExpectingMsg(DoctorServerMsg.LOGIN_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious LoginAck message received in the middle!!!");
        }

        clearSpecificExpectedMsg();
        this.setExpectedMsgOnline();

        // content = <OK!> or <Error!>
        if (content.equals("OK!")) {
            dc.loginOK = true;
            state = ConnectionState.ONLINE;

        } else {
            doctorID = -1;
            state = ConnectionState.INIT;
            dc.loginOK = false;

        }
        synchronized (dc) {
            dc.notify();
        }

    }

    public void sendLogout() throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE) {
            throw new InvalidProtocolStateException(
                    "Can not send logout message when processing case or before login!!!");
        }

        setSpecificExpectedMsg(DoctorServerMsg.LOGOUT_ACK);

        // <Logout#>
        sendMsg("Logout#");

    }

    protected void onLogoutAck(String content) throws InvalidProtocolStateException {

        System.out.println("LogoutAck received. content = <" + content + ">");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(DoctorServerMsg.LOGOUT_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious LogoutAck message received in the middle!!!");
        }

        // <LogoutAck#>
        state = ConnectionState.INIT;

        clearSpecificExpectedMsg();

    }

    // part 2: query profile, query case list, query doctor list
    public void sendQueryPatientProfile(int patientID) throws IOException,
            InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryPatientProfile message before login!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.QUERY_PATIENT_PROFILE_ACK);

        // <QueryPatientProfile#patientID>
        sendMsg("QueryPatientProfile#" + patientID);

    }

    protected void onQueryPatientProfileAck(String content)
            throws ProtocolErrorException {

        System.out.println("QueryPatientProfileAck received. content = <"
                + content + ">");

        if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
                || !isExpectingMsg(DoctorServerMsg.QUERY_PATIENT_PROFILE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryPatientProfileAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.QUERY_PATIENT_PROFILE_ACK);

        // display patient profile
        PatientInfo pi = new PatientInfo(content);

        System.out.println("\t" + pi.toString());

        //dmf.showPatientInfo(pi);
        dmf.allPatientInfo[dmf.getSizeOfAllPatientInfo()]=pi;

    }

    public void sendQueryAllCaseIDOfOnePatient(int patientID)
            throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryAllCaseIDOfOnePatient message before login!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.QUERY_ALL_CASE_ID_OF_ONE_PATIENT_ACK);

        // <QueryAllCaseIDOfOnePatient#patientID>
        sendMsg("QueryAllCaseIDOfOnePatient#" + patientID);

    }

    protected void onQueryAllCaseIDOfOnePatientAck(String content) throws InvalidProtocolStateException {

        System.out.println("QueryAllCaseIDOfOnePatientAck received.");

        if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
                || !isExpectingMsg(DoctorServerMsg.QUERY_ALL_CASE_ID_OF_ONE_PATIENT_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryAllCaseIDOfOnePatientAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.QUERY_ALL_CASE_ID_OF_ONE_PATIENT_ACK);

        // content = <[caseID$symptom$dateTime]+> or <null>
        // We trust the server and do no sanity check here
        System.out.println(content.toString());
        String[] strs = content.split("#");

        for (String str : strs) {

            String[] cs = str.split("[$]");

            System.out.println("\tID = " + cs[0] + "; symptom: " + cs[1]
                    + "; date-time: " + cs[2]);

        }
        dmf.showAllCaseOfPatient(strs);

    }

    public void sendQueryCase(int caseID) throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryCase message before login!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.QUERY_CASE_ACK);

        // <QueryCase#CaseID>
        sendMsg("QueryCase#" + caseID);

    }

    protected void onQueryCaseAck(String content) throws ProtocolErrorException {

        System.out.println("QueryCaseAck received.");

        if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
                || !isExpectingMsg(DoctorServerMsg.QUERY_CASE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryCaseAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.QUERY_CASE_ACK);

        // content = <CaseInfo.toString()>
        CaseInfo c = new CaseInfo(content);

        System.out.println("\t" + c);

        dmf.showCaseDetail(c);
        dmf.allCaseContents[dmf.getSizeOfallCaseContents()] = content;
    }

    public void sendQueryAllLabMeasurementIDOfOnePatient(int patientID)
            throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryAllLabMeasurementIDOfOnePatient message before login!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.QUERY_ALL_LAB_MEASUREMENT_ID_OF_ONE_PATIENT_ACK);

        // <QueryAllLabMeasurementIDOfOnePatient#patientID>
        sendMsg("QueryAllLabMeasurementIDOfOnePatient#" + patientID);

    }

    protected void onQueryAllLabMeasurementIDOfOnePatientAck(String content) throws InvalidProtocolStateException {

        System.out.println("QueryAllLabMeasurementIDOfOnePatientAck received.");

        if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
                || !isExpectingMsg(DoctorServerMsg.QUERY_ALL_LAB_MEASUREMENT_ID_OF_ONE_PATIENT_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryCaseAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.QUERY_ALL_LAB_MEASUREMENT_ID_OF_ONE_PATIENT_ACK);

        // content = <[labID$type$dateTime]+>
        // We trust the server and do no sanity check here
        String[] strs = content.split("#");

        for (String str : strs) {

            String[] ls = str.split("[$]");

            System.out.println("ID = " + ls[0] + "; symptom: " + ls[1]
                    + "; date-time: " + ls[2]);
        }

    }

    public void sendQueryLabMeasurement(int labID) throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryLabMeasurement message before login!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.QUERY_LAB_MEASUREMENT_ACK);

        // <QueryLabMeasurement#LabID>
        sendMsg("QueryLabMeasurement#" + labID);

    }

    protected void onQueryLabMeasurementAck(String content)
            throws ProtocolErrorException {

        System.out.println("QueryLabMeasurementAck received.");

        if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
                || !isExpectingMsg(DoctorServerMsg.QUERY_LAB_MEASUREMENT_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryLabMeasurementAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.QUERY_LAB_MEASUREMENT_ACK);

        // content = <LabMeasurementInfo.toString()>
        LabMeasurementInfo li = new LabMeasurementInfo(content);

        System.out.println("\t" + li);
    }

    public void sendQueryChatHistory(int caseID) throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send QueryChatHistory message before login!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.QUERY_CHAT_HISTORY_ACK);

        // <QueryChatHistory#caseID>
        sendMsg("QueryChatHistory#" + caseID);

    }

    protected void onQueryChatHistoryAck(String content)
            throws InvalidProtocolStateException, InvalidDataRecordException {

        System.out.println("QueryChatHistoryAck received.");

        if ((state != ConnectionState.ONLINE && state != ConnectionState.IN_CASE)
                || !isExpectingMsg(DoctorServerMsg.QUERY_CHAT_HISTORY_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious QueryChatHistoryAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.QUERY_CHAT_HISTORY_ACK);

        if (state == ConnectionState.IN_CASE) {
            setExpectedMsgInCaseProcessing();
        }

        if (content.equals("null")) {

        } else {

            String[] contents = content.split("#");

            List<ChatInfo> chatList = new ArrayList<ChatInfo>();

            for (int i = 0; i < contents.length; i++) {
                ChatInfo ci = new ChatInfo(contents[i]);
                chatList.add(ci);
            }

            // display chat history
            for (ChatInfo ci : chatList) {
                System.out.println("\t" + ci);
            }
        }
    }

    // part 3: handle case
    protected void onForwardHandleCase(String content)
            throws ProtocolErrorException, IOException {

        System.out.println("ForwardHandleCase received.");

        if (state != ConnectionState.ONLINE
                || !isExpectingMsg(DoctorServerMsg.FORWARD_HANDLE_CASE)) {
            throw new InvalidProtocolStateException(
                    "Spurious ForwardHandleCase message received in the middle!!!");
        }

        CaseInfo ci = new CaseInfo(content);

        currentCaseInfo = ci;

        state = ConnectionState.IN_CASE;
        setExpectedMsgInCaseProcessing();

        sendForwardHandleCaseAck();

    }

    // TODO: might need to query a bunch of information here
    protected void sendForwardHandleCaseAck() throws IOException {

        try {
            sendQueryPatientProfile(currentCaseInfo.getPatientID());
            dmf.showNewPatient(currentCaseInfo.getPatientID());
            dmf.currentPID = currentCaseInfo.getPatientID();
            dmf.showPatientInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // <HandleCaseAck#>
        sendMsg("ForwardHandleCaseAck#");

    }

    public void sendLinkCaseToCurrentCase(int linkedCaseID)
            throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send LinkCaseToCase message before login or just online!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.LINK_CASE_TO_CURRENT_CASE_ACK);

        // <LinkCaseToCurrentCase#LinkedCaseID>
        sendMsg("LinkCaseToCurrentCase#" + linkedCaseID);

        // TODO: modify currentCaseInfo here
    }

    protected void onLinkCaseToCurrentCaseAck(String content) throws InvalidProtocolStateException {

        System.out.println("LinkCaseToCurrentCaseAck received. content = <"
                + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(DoctorServerMsg.LINK_CASE_TO_CURRENT_CASE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious LinkCaseToCurrentCaseAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.LINK_CASE_TO_CURRENT_CASE_ACK);

        if (content.equals("OK!")) {

        } else {

        }

    }

    public void sendLinkLabMeasurementToCurrentCase(int linkedLabID)
            throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send LinkCaseToCase message before login or just online!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.LINK_LAB_MEASUREMENT_TO_CURRENT_CASE_ACK);

        // <LinkLabMeasurementToCurrentCase#linkedLabID>
        sendMsg("LinkLabMeasurementToCurrentCase#" + linkedLabID);

        // TODO: modify currentCaseInfo here
    }

    protected void onLinkLabMeasurementToCurrentCaseAck(String content) throws InvalidProtocolStateException {

        System.out.println("LinkLabMeasurementToCurrentCaseAck received. "
                + "content = <" + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(DoctorServerMsg.LINK_LAB_MEASUREMENT_TO_CURRENT_CASE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious LinkLabMeasurementToCurrentCaseAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.LINK_LAB_MEASUREMENT_TO_CURRENT_CASE_ACK);

        if (content.equals("OK!")) {

        } else {

        }

    }

    // part 4: chat message
    public void sendChatMessage(String cmsg) throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send ChatMessage message when not processing case or before login!!!");
        }

        // <ChatMessage#Message>
        sendMsg("ChatMessage#" + cmsg);

        // no change for expected message
    }

    protected void onForwardChatMessage(String content) throws InvalidProtocolStateException {

        System.out.println("ForwardChatMessage received." + " content = <"
                + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(DoctorServerMsg.FORWARD_CHAT_MESSAGE)) {
            throw new InvalidProtocolStateException(
                    "Spurious ForwardChatMessage message received in the middle!!!");
        }

        dmf.showChatReceived(content);

        // No change on expected message
        // display the chat content
    }

    // part 5: suspend and finish case
    protected void onForwardSuspendCaseByPatient(String content)
            throws InvalidProtocolStateException, IOException {

        System.out.println("ForwardSuspendCaseByPatient received. content = <"
                + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(DoctorServerMsg.FORWARD_SUSPEND_CASE_BY_PATIENT)) {
            throw new InvalidProtocolStateException(
                    "Spurious ForwardSuspendCaseByPatient message received in the middle!!!");
        }

        state = ConnectionState.ONLINE;
        setExpectedMsgOnline();

        // No change on expected message here
        sendForwardSuspendCaseByPatientAck();

    }

    protected void sendForwardSuspendCaseByPatientAck() throws IOException,
            InvalidProtocolStateException {

        // <SuspendCaseAck#>
        sendMsg("ForwardSuspendCaseByPatientAck#");

    }

    public void sendSuspendCaseByDoctor(String reason) throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send SuspendCaseByDoctor message when not processing case!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.SUSPEND_CASE_BY_DOCTOR_ACK);

        // <SuspendCase#reason>
        sendMsg("SuspendCaseByDoctor#" + reason);

    }

    protected void onSuspendCaseByDoctorAck(String content)
            throws InvalidProtocolStateException {

        System.out.println("SuspendCaseByDoctorAck received. content = <"
                + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(DoctorServerMsg.SUSPEND_CASE_BY_DOCTOR_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious SuspendCaseByDoctorAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.SUSPEND_CASE_BY_DOCTOR_ACK);

        state = ConnectionState.ONLINE;
        setExpectedMsgOnline();

        // Temporary quit by the patient
    }

    public void sendFinishCase(String finalDiagnose) throws IOException, InvalidProtocolStateException {

        if (state != ConnectionState.IN_CASE) {
            throw new InvalidProtocolStateException(
                    "Can not send FinishCase message when not processing case!!!");
        }

        addSpecificExpectedMsg(DoctorServerMsg.FINISH_CASE_ACK);

        // <FinishCase#reason>
        sendMsg("FinishCase#" + finalDiagnose);

    }

    protected void onFinishCaseAck(String content) throws IOException,
            InvalidProtocolStateException {

        System.out.println("FinishCaseAck received. content = <" + content + ">");

        if (state != ConnectionState.IN_CASE
                || !isExpectingMsg(DoctorServerMsg.FINISH_CASE_ACK)) {
            throw new InvalidProtocolStateException(
                    "Spurious FinishCaseAck message received in the middle!!!");
        }

        removeSpecificExpectedMsg(DoctorServerMsg.FINISH_CASE_ACK);

        state = ConnectionState.ONLINE;
        
        setExpectedMsgOnline();

        // TODO: change currentCaseInfo here
    }
}
