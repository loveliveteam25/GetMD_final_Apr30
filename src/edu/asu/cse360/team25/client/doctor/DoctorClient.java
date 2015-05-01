package edu.asu.cse360.team25.client.doctor;

import java.io.IOException;

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
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DoctorClient extends Client {

     protected DoctorMainFrame dmf;

    protected DoctorServerConnection dsc;

    protected int state = 0; // 0 - login, 1 - signup, 2 - main

    protected int whichButtonOnLogin = 0; // 0 - cancel, 1 - login, 2 - signup

    protected int whichButtonOnSignUp = 0; // 0 - cancel, 1 - signup
    
    // for protocol
    protected boolean loginOK = false;
    protected boolean signupOK = false;

    // model
    // for login
    protected int doctorID = -1; // also used for register
    protected String password = null;

    
    // for signup
    protected String usernameSU;
    protected String passwordSU;
    protected String genderSU;
    protected String heightSU;
    protected String weightSU;
    protected String birthdaySU;

    //
    protected DoctorInfo di;
    protected DoctorInfo diUpdate;

    protected List<CaseInfo> cases;
    
    protected List<DoctorInfo> doctors;
    
    protected List<ChatInfo> chatList;
    
    public DoctorClient() {
        super();
        
        dmf = new DoctorMainFrame(this);
        
        dsc = new DoctorServerConnection(this);
        
        dmf.dsc = dsc;
        dsc.dmf = dmf;
        
        dmf.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent arg0) {
            
            //System.out.println("window closing handler called!!!");
            
            try {
                dsc.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(DoctorClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    });

    }

    public DoctorServerConnection getDsc() {
        return dsc;
    }

    protected void doLogin() throws IOException, ProtocolErrorException, InterruptedException {
        // Login
        loginOK = false;
        whichButtonOnLogin = 0;

        DoctorLoginDialog ld = new DoctorLoginDialog(null, this, doctorID, password);
        ld.setLocationRelativeTo(null);
        ld.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ld.setVisible(true);

        if(whichButtonOnLogin == 1) {
        
            
            dsc.sendLogin(doctorID, password);
            // wait for login ack

            synchronized (this) {
               
                wait();//Stuck here
              
                 System.out.println("start checking if statement");
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

       System.out.println("NO SIGNUP FOR DOCTORS!!!!");

    }

    protected void doMain() throws IOException, InterruptedException, InvalidProtocolStateException {
        
                // Query profile
        //dsc.sendQueryPatientProfile(0);// getting patient's profile
        

       // synchronized (this) {
        //        wait();
        //    }
       // dsc.sendQueryAllCaseIDOfOnePatient(0);// getting patient's case.
       //
         //synchronized (this) {
         //       wait();
        //    }
      //  dsc.sendQueryCase(0);
        

        System.out.println("doing MAin");
        dmf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dmf.getAllPatientInfo();
        synchronized (this) {
                wait(10);
            }
        dmf.getAllCaseContents();
        synchronized (this) {
                wait(13);
            }
       dmf.showAllCases(dmf.allCaseContent());
        
        dmf.setVisible(true);
        
         //dsc.sendQueryPatientProfile(0);// getting patient's profile
        

        synchronized (this) {
                wait();
            }
       
        //pmf.showPatientInfo();
       // pmf.showCaseList();


        dsc.waitForReceivingThread();
        
        state = 3;
        
    }
    
    public void start() throws InterruptedException, IOException, ProtocolErrorException {

        dsc.connect();
        dsc.startReceiving();

        while(true) {
            
            if(state == 0) {
                System.out.println("Login starting");
                doLogin();
                System.out.println(state);
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

        dsc.disconnect();
    }

    public void setLoginInfo(int doctorID, String password, boolean login) {

        this.doctorID = doctorID;
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


    public void setDoctorInfo(DoctorInfo pi) {

        this.di = di;
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
            java.util.logging.Logger.getLogger(DoctorMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DoctorMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DoctorMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DoctorMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {

            DoctorClient dc = new DoctorClient();

            dc.start();

            dc.stop();

            dc.dsc.waitForReceivingThread();


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

        

