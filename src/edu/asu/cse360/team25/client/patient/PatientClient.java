package edu.asu.cse360.team25.client.patient;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.asu.cse360.team25.client.Client;
import edu.asu.cse360.team25.client.patient.PatientLoginDialog;
import edu.asu.cse360.team25.client.patient.PatientMainFrame;
import edu.asu.cse360.team25.client.patient.PatientServerConnection;
import edu.asu.cse360.team25.client.patient.PatientSignUpDialog;
import edu.asu.cse360.team25.protocol.CaseInfo;
import edu.asu.cse360.team25.protocol.ChatInfo;
import edu.asu.cse360.team25.protocol.DoctorInfo;
import edu.asu.cse360.team25.protocol.PatientInfo;
import edu.asu.cse360.team25.protocol.exception.InvalidProtocolStateException;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientClient extends Client {

    protected PatientMainFrame pmf;

    protected PatientServerConnection psc;

    protected int state = 0; // 0 - login, 1 - signup, 2 - main

    protected int whichButtonOnLogin = 0; // 0 - cancel, 1 - login, 2 - signup

    protected int whichButtonOnSignUp = 0; // 0 - cancel, 1 - signup
    
    // for protocol
    protected boolean loginOK = false;
    protected boolean signupOK = false;

    // model
    // for login
    protected int patientID = -1; // also used for register
    protected String password = null;

    
    // for signup
    protected String usernameSU;
    protected String passwordSU;
    protected String genderSU;
    protected String heightSU;
    protected String weightSU;
    protected String birthdaySU;

    //
    protected PatientInfo pi;
    protected PatientInfo piUpdate;

    protected List<CaseInfo> cases;
    
    protected List<DoctorInfo> doctors;
    
    protected List<ChatInfo> chatList;
    
    public PatientClient() {
        super();
        
        pmf = new PatientMainFrame(this);
        
        psc = new PatientServerConnection(this);
        
        pmf.psc = psc;
        psc.pmf = pmf;
        
        pmf.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent arg0) {
            
            //System.out.println("window closing handler called!!!");
            
            try {
                psc.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(PatientClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    });

    }

    public PatientServerConnection getPsc() {
        return psc;
    }

    protected void doLogin() throws IOException, ProtocolErrorException, InterruptedException {
        // Login
        loginOK = false;
        whichButtonOnLogin = 0;

        PatientLoginDialog ld = new PatientLoginDialog(null, this, patientID, password);
        ld.setLocationRelativeTo(null);
        ld.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ld.setVisible(true);

        if(whichButtonOnLogin == 1) {
        
            psc.sendLogin(patientID, password);
            // wait for login ack

            synchronized (this) {
                wait();
            }
            
            if (!loginOK) {
                JOptionPane.showMessageDialog(null, "User ID and password does not match!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                state = 2; // goto main
            }
            
        } else if(whichButtonOnLogin == 0) {
            
            state = 3; // exit application
            
        } else if(whichButtonOnLogin == 2) {
            
            state = 1; // go to signup
        }
       

    }

    protected void doSignUp() throws IOException, InterruptedException, ProtocolErrorException {

        signupOK = false;
        whichButtonOnSignUp = 0;

        PatientSignUpDialog sd = new PatientSignUpDialog(null, this);
        sd.setLocationRelativeTo(null);
        sd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        sd.setVisible(true);

        if (whichButtonOnSignUp == 1) {

            psc.sendRegister(passwordSU, usernameSU, genderSU, heightSU, weightSU, birthdaySU);

            synchronized (this) {
                wait();
            }

            JOptionPane.showMessageDialog(null,
                    "Register succeeded. Your ID is " + patientID + ".");

            state = 0;

        } else {
            
            state = 0;
            
        }

    }

    protected void doMain() throws IOException, InterruptedException, InvalidProtocolStateException {
        
                // Query profile
        psc.sendQueryPatientProfile();

        synchronized (this) {
                wait();
            }
        
    psc.sendQueryCaseList();
        
        synchronized (this) {
                wait();
            }
        
        pmf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pmf.setVisible(true);
        pmf.showPatientInfo();
        pmf.showCaseList();



        psc.waitForReceivingThread();
        
        state = 3;
        
    }
    
    public void start() throws InterruptedException, IOException, ProtocolErrorException {

        psc.connect();
        psc.startReceiving();

        while(true) {
            
            if(state == 0) {
                doLogin();
            } else if(state == 1) {
                doSignUp();
            } else if(state == 2 ) {
                doMain();
            } else {
                break;
            }
            
            
        }


    }

    public void stop() throws IOException {

        psc.disconnect();
    }

    public void setLoginInfo(int patientID, String password, boolean login) {

        this.patientID = patientID;
        this.password = password;
    }

    public void setSignUpInfo(String username, String password, String gender, String height, String weight, String birthday) {
        
				usernameSU = username;
				passwordSU = password;
				genderSU = gender;
				heightSU = height;
				weightSU = weight;
				birthdaySU = birthday;

    }


    public void setPatientInfo(PatientInfo pi) {

        this.pi = pi;
    }

    public static void main(String[] argv) {

        /* Set the Aqua look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Aqua".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
//            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PatientMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PatientMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PatientMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PatientMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {

            PatientClient pc = new PatientClient();

            pc.start();

            pc.stop();

            pc.psc.waitForReceivingThread();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
