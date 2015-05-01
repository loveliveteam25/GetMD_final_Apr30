package edu.asu.cse360.team25.test;

import java.io.IOException;

import edu.asu.cse360.team25.client.doctor.DoctorClient;
import edu.asu.cse360.team25.client.doctor.DoctorServerConnection;
import edu.asu.cse360.team25.client.patient.PatientClient;
import edu.asu.cse360.team25.client.patient.PatientServerConnection;
import edu.asu.cse360.team25.protocol.exception.InvalidProtocolStateException;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;
import edu.asu.cse360.team25.server.Server;

public class TestSystemInCommandLine {

    public static void main(String[] args) throws IOException {

        PatientClient pc = new PatientClient();

        PatientServerConnection psc = pc.getPsc();

        DoctorClient dc = new DoctorClient();

        DoctorServerConnection dsc = dc.getDsc();

        try {

            // remember to start the server first
            psc.connect();

            // start patient client
            psc.startReceiving();

            // start doctor client
            dsc.connect();

            dsc.startReceiving();

            // patient register
//			psc.sendRegister("654321", "XXX", "male", "0", "0", "Unknown");
//			
//			Thread.sleep(1000);
            // ---------------------- patient alone ---------------------------
            // login
            psc.sendLogin(0, "123456");

            Thread.sleep(1000);

//			// profile
//			
            psc.sendQueryPatientProfile();
//			
            Thread.sleep(1000);
//
            psc.sendUpdatePatientProfile("Amami Harukakka", "female", "140", "40", "some day");
//			
            Thread.sleep(1000);
//
//			
//			// doctor list, case list, chat history
//			
            psc.sendQueryCaseList();
//			
            Thread.sleep(1000);
//			
            psc.sendQueryDoctorList("*", "*");
//			
            Thread.sleep(1000);
//			
            psc.sendQueryChatHistory(0);
//			
            Thread.sleep(1000);

            // ---------------------- doctor alone ---------------------------
            dsc.sendLogin(0, "123456");
            Thread.sleep(1000);
            dsc.sendQueryPatientProfile(0);
//			
            Thread.sleep(1000);
//
            dsc.sendQueryAllCaseIDOfOnePatient(0);
//			
            Thread.sleep(1000);
//
//			
            //        dsc.sendQueryCase(0);
//			
            //	Thread.sleep(1000);
//
//			
//			dsc.sendQueryAllLabMeasurementIDOfOnePatient(0);
//			
////			Thread.sleep(1000);
//
//			dsc.sendQueryLabMeasurement(0);
//			
//			Thread.sleep(1000);
//			
//			dsc.sendQueryChatHistory(0);
//			
//			Thread.sleep(1000);
            // ---------------------- patient doctor together ---------------------------
            psc.sendCreateCase("Unknown", "Manyache", 0);
            Thread.sleep(1000);
          
            //	dsc.sendSuspendCaseByDoctor("some reason");
            //	Thread.sleep(1000);
            // ------------ ----------- chat ---------------------------------
            //	psc.sendResumeCase(2);
            //	Thread.sleep(1000);
//            psc.sendChatMessage("dsakhfiosfsajhfklsdjlkdfsjlkj");
//            Thread.sleep(1000);
 //           dsc.sendChatMessage("03948025237150843501805415094-43084-13");
 //           Thread.sleep(1000);
            dsc.sendFinishCase("finish it");
            Thread.sleep(1000);
            psc.sendForwardFinishCaseAck(10);
            Thread.sleep(1000);
            // logout
            psc.sendLogout();

            dsc.sendLogout();
            psc.waitForReceivingThread();

            dsc.waitForReceivingThread();
        } catch (IOException e) {
            // Unable to create server listening socket.
            e.printStackTrace();
        } catch (InterruptedException e) {
            // Waiting for listening thread is interrupted. 
            e.printStackTrace();
        } catch (InvalidProtocolStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
