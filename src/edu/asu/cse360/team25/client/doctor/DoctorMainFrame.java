package edu.asu.cse360.team25.client.doctor;

import edu.asu.cse360.team25.client.doctor.DoctorServerConnection.ConnectionState;
import edu.asu.cse360.team25.protocol.CaseInfo;
import edu.asu.cse360.team25.protocol.DoctorInfo;
import edu.asu.cse360.team25.protocol.PatientInfo;
import edu.asu.cse360.team25.server.Doctor;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author FanjieLin
 */
public class DoctorMainFrame extends javax.swing.JFrame {

    protected DoctorClient dc;
    protected DoctorServerConnection dsc;
    private DoctorInfo[] theDoctorList = new DoctorInfo[9];
    public String[] allCaseContents = new String[100];
    public PatientInfo[] allPatientInfo = new PatientInfo[100];
    public int currentPID;

    /**
     * Creates new form PatientMainForm
     */
    public DoctorMainFrame(DoctorClient dc) {
        initComponents();

        this.dc = dc;
        

    }

    public void close() {
        WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
    }

    public void setupDoctor() {

        theDoctorList[0] = new Doctor(0, "Kousaka Honoka", "GAME", "GTA5", "123456");
        theDoctorList[1] = new Doctor(1, "Minami Kotori", "GAME", "Naruto", "123456");
        theDoctorList[2] = new Doctor(2, "Sonoda Umi", "GAME", "LoveLive", "123456");
        theDoctorList[3] = new Doctor(3, "Koizumi Hanayo", "Cook", "meat ball", "123456");
        theDoctorList[4] = new Doctor(4, "Hoshizora Rin", "Cook", "yogurt", "123456");
        theDoctorList[5] = new Doctor(5, "Nishikino Maki", "Cook", "sandwith", "123456");
        theDoctorList[6] = new Doctor(6, "Ayase Eli", "Cook", "burger", "123456");
        theDoctorList[7] = new Doctor(7, "Toujyou Nozomi", "Cook", "BBQ", "123456");
        theDoctorList[8] = new Doctor(8, "Yazawa Niko", "Cook", "pizza", "123456");

    }
    
    protected void showTheDoctor(){
        DefaultTableModel model = (DefaultTableModel) doctorRefList.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (DoctorInfo di : theDoctorList) {
            Object[] os = new Object[model.getColumnCount()];
            if(di.getDoctorID()!=dsc.doctorID)
            {
                 os[0] = di.getDoctorID();
            os[1] = di.getName();
            os[2] = di.getDepartment();
            os[3] = di.getExpertise();
            os[4] = di.getState();
            os[5] = di.getRate();

            model.addRow(os);
            doctorRefList.setModel(model);
            }
           
    }
    }

    protected void getAllCaseContents() {
        for (int i = 0; i < 6; i++) {
            try {

                synchronized (this) {
                    dsc.sendQueryCase(i);
                    wait(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    protected String[] allCaseContent() {
        String[] acc = new String[getSizeOfallCaseContents()];
        for (int i = 0; i < getSizeOfallCaseContents(); i++) {
            acc[i] = allCaseContents[i];
        }
        return acc;
    }

    protected void getAllPatientInfo() {
        for (int i = 0; i < 10; i++) {
            try {

                synchronized (this) {
                    dsc.sendQueryPatientProfile(i);
                    wait(10);
                }
            } catch (Exception e) {
                System.out.println("OOB");
                i++;
            }

        }
    }

    protected int getSizeOfAllPatientInfo() {
        int count = 0;
        for (PatientInfo j : allPatientInfo) {
            if (j == null) {
            } else {
                count++;
            }
        }
        return count;
    }

    protected int getSizeOfallCaseContents() {
        int count = 0;
        for (String j : allCaseContents) {
            if (j == null) {
            } else {
                count++;
            }
        }
        return count;
    }

    protected void showPatientInfo() {
        for (int i = 0; i < getSizeOfAllPatientInfo(); i++) {
            if (allPatientInfo[i].getPatientID() == currentPID) {
                txt_pID.setText("ID: " + allPatientInfo[i].getPatientID());
                txt_gender.setText(allPatientInfo[i].getGender());
                txt_height.setText(allPatientInfo[i].getHeight());
                txt_pDoB.setText(allPatientInfo[i].getBirthday());
                txt_pName.setText(allPatientInfo[i].getName());
                txt_weight.setText(allPatientInfo[i].getWeight());

            }
        }
        // txt_pID.setText("ID: " + pi.getPatientID());
        // txt_gender.setText(pi.getGender());
        //txt_height.setText(pi.getHeight());
        // txt_pDoB.setText(pi.getBirthday());
        // txt_pName.setText(pi.getName());
        //txt_weight.setText(pi.getWeight());
    }

    protected void showNewPatient(int patientID) {
        jl_welcomeP.setText("PatientID# " + patientID + " is connected");
        jd_newPatient.setVisible(true);

    }

    protected void showChatReceived(String content) {
        txt_toRecv.setText(content);
    }

    protected void showCaseDetail(CaseInfo ci) {
        // txt_finalDiag.setText(ci.toString());
    }
    

    protected void showAllCaseOfPatient(String[] strs) {
        DefaultTableModel model = (DefaultTableModel) historyList.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (String str : strs) {
            Object[] os = new Object[model.getColumnCount()];
            String[] cs = str.split("[$]");
            os[0] = cs[0];
            os[1] = cs[1];
            os[2] = cs[2];

            model.addRow(os);

        }
    }
    /* protected void getAllCases(){
     for (int i=0;i<2;i++) {
     try{
     dsc.sendQueryCase(i);
     wait(10);
     }catch(Exception e){
     e.printStackTrace();
     }
     }
     }*/

    protected void showAllCases(String[] strs) {

        DefaultTableModel model = (DefaultTableModel) historyList.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        for (String str : strs) {
            Object[] os = new Object[model.getColumnCount()];
            System.out.println(str);
            String[] cs = str.split("[$]");
            os[0] = cs[0];
            os[1] = cs[1];
            os[2] = cs[2];
            os[3] = cs[3];
            os[4] = cs[4];
            os[5] = cs[5];
            os[6] = cs[8];
            os[7] = cs[9];
            

            model.addRow(os);
        }

        System.out.println(strs.toString());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jd_newPatient = new javax.swing.JDialog();
        jl_welcomeP = new javax.swing.JLabel();
        jb_closeJd = new javax.swing.JButton();
        jd_refer = new javax.swing.JDialog();
        jScrollPane5 = new javax.swing.JScrollPane();
        doctorRefList = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        patientName = new javax.swing.JLabel();
        DOB = new javax.swing.JLabel();
        patientID = new javax.swing.JLabel();
        txt_pID = new java.awt.TextField();
        txt_pName = new java.awt.TextField();
        txt_pDoB = new java.awt.TextField();
        jLabel5 = new javax.swing.JLabel();
        txt_gender = new java.awt.TextField();
        jLabel6 = new javax.swing.JLabel();
        txt_height = new java.awt.TextField();
        jLabel7 = new javax.swing.JLabel();
        txt_weight = new java.awt.TextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_toSend = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_toRecv = new javax.swing.JTextArea();
        refer = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        jb_logout = new javax.swing.JButton();
        sendButton = new javax.swing.JButton();
        jp_caseHistory = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        historyList = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_finalDiag = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();

        jd_newPatient.setAlwaysOnTop(true);
        jd_newPatient.setMinimumSize(new java.awt.Dimension(420, 375));

        jl_welcomeP.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N
        jl_welcomeP.setText("Welcome new patient!");

        jb_closeJd.setText("Close");
        jb_closeJd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_closeJdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_newPatientLayout = new javax.swing.GroupLayout(jd_newPatient.getContentPane());
        jd_newPatient.getContentPane().setLayout(jd_newPatientLayout);
        jd_newPatientLayout.setHorizontalGroup(
            jd_newPatientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_newPatientLayout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jb_closeJd, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_newPatientLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(jl_welcomeP, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jd_newPatientLayout.setVerticalGroup(
            jd_newPatientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_newPatientLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jl_welcomeP, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jb_closeJd)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        jd_refer.setMinimumSize(new java.awt.Dimension(875, 463));

        doctorRefList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Department", "Expertise", "State", "Rate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        doctorRefList.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                doctorRefListComponentAdded(evt);
            }
        });
        doctorRefList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doctorRefListMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(doctorRefList);

        jButton1.setText("Apply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_referLayout = new javax.swing.GroupLayout(jd_refer.getContentPane());
        jd_refer.getContentPane().setLayout(jd_referLayout);
        jd_referLayout.setHorizontalGroup(
            jd_referLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_referLayout.createSequentialGroup()
                .addGap(374, 374, 374)
                .addComponent(jButton1)
                .addContainerGap(422, Short.MAX_VALUE))
            .addGroup(jd_referLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jd_referLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 839, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jd_referLayout.setVerticalGroup(
            jd_referLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_referLayout.createSequentialGroup()
                .addContainerGap(371, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(31, 31, 31))
            .addGroup(jd_referLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jd_referLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(93, Short.MAX_VALUE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GetMD Experience");
        setBackground(new java.awt.Color(204, 255, 255));

        patientName.setText("Patient Name:");

        DOB.setText("Date of Birth:");

        patientID.setText("Patient ID:");

        txt_pID.setEditable(false);

        txt_pName.setEditable(false);

        txt_pDoB.setEditable(false);
        txt_pDoB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pDoBActionPerformed(evt);
            }
        });

        jLabel5.setText("Patient Gender");

        txt_gender.setEditable(false);

        jLabel6.setText("Patient Height");

        txt_height.setEditable(false);

        jLabel7.setText("Patient Weight");

        txt_weight.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(patientID)
                            .addComponent(patientName))
                        .addGap(56, 56, 56)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_pName, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(txt_pID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DOB)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_pDoB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_gender, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_height, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_weight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(patientID)
                    .addComponent(txt_pID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(patientName)
                    .addComponent(txt_pName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DOB)
                    .addComponent(txt_pDoB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txt_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(txt_weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Live-Chat", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Abadi MT Condensed Extra Bold", 1, 14))); // NOI18N

        txt_toSend.setColumns(20);
        txt_toSend.setRows(5);
        jScrollPane2.setViewportView(txt_toSend);

        txt_toRecv.setColumns(20);
        txt_toRecv.setRows(5);
        jScrollPane1.setViewportView(txt_toRecv);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        refer.setText("Refer to Other Doctor");
        refer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("Doctor Management");

        saveButton.setText("Finish The Case and Log Out");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jb_logout.setText("Logout");
        jb_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_logoutActionPerformed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        jp_caseHistory.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Visit History", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Abadi MT Condensed Extra Bold", 1, 14))); // NOI18N

        historyList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CaseID", "PainLevel", "Symptom", "PatientID", "DoctorID", "DateTime", "FinalDiagnose", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        historyList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historyListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(historyList);

        jLabel1.setText("Final Diagnose");

        txt_finalDiag.setColumns(20);
        txt_finalDiag.setRows(5);
        jScrollPane4.setViewportView(txt_finalDiag);

        javax.swing.GroupLayout jp_caseHistoryLayout = new javax.swing.GroupLayout(jp_caseHistory);
        jp_caseHistory.setLayout(jp_caseHistoryLayout);
        jp_caseHistoryLayout.setHorizontalGroup(
            jp_caseHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
            .addGroup(jp_caseHistoryLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
        );
        jp_caseHistoryLayout.setVerticalGroup(
            jp_caseHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_caseHistoryLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/asu/cse360/team25/client/doctor/dricon.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jb_logout)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(refer, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(253, 253, 253)
                                .addComponent(sendButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(84, 84, 84)))
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jp_caseHistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel2)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jp_caseHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jb_logout)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed

        if(dsc.state!=ConnectionState.IN_CASE){
            JOptionPane.showMessageDialog(this,
                    "No patient is connected with you");
        }
        else{
             try {
            dsc.sendChatMessage(txt_toSend.getText());
            // TODO add your handling code here:
        } catch (Exception e) {
            e.printStackTrace();
        }   // TODO add your handling code here:
        }
       
    }//GEN-LAST:event_sendButtonActionPerformed

    private void referActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_referActionPerformed
      
        if(dsc.state!=ConnectionState.IN_CASE){
            JOptionPane.showMessageDialog(this,
                    "No patient is connected with you");
        }
        else{
                  setupDoctor();
        showTheDoctor();
        jd_refer.setVisible(true);
        }
  
    }//GEN-LAST:event_referActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if(dsc.state!=ConnectionState.IN_CASE){
            JOptionPane.showMessageDialog(this,
                    "No patient is connected with you");
        }
        else{
             try {
                 
            dsc.sendFinishCase(txt_finalDiag.getText());
            close();
            System.out.println("The current case state is: "+dsc.currentCaseInfo.getState());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
       


    }//GEN-LAST:event_saveButtonActionPerformed

    private void txt_pDoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pDoBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pDoBActionPerformed

    private void historyListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historyListMouseClicked
        /*
         int row = historyList.getSelectedRow();
         try {
         dsc.sendQueryCase(Integer.valueOf(historyList.getValueAt(row, 0).toString()));
         } catch (Exception e) {
         e.printStackTrace();
         }
         */
        // evt. // TODO add your handling code here:
    }//GEN-LAST:event_historyListMouseClicked

    private void jb_closeJdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_closeJdActionPerformed
        jd_newPatient.setVisible(false);// TODO add your handling code here:
    }//GEN-LAST:event_jb_closeJdActionPerformed

    private void doctorRefListComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_doctorRefListComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_doctorRefListComponentAdded

    private void doctorRefListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doctorRefListMouseClicked
       
    }//GEN-LAST:event_doctorRefListMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jd_refer.setVisible(false);
        close();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jb_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_logoutActionPerformed
       
        close();// TODO add your handling code here:
    }//GEN-LAST:event_jb_logoutActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DOB;
    private javax.swing.JTable doctorRefList;
    private javax.swing.JTable historyList;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jb_closeJd;
    private javax.swing.JButton jb_logout;
    private javax.swing.JDialog jd_newPatient;
    private javax.swing.JDialog jd_refer;
    private javax.swing.JLabel jl_welcomeP;
    private javax.swing.JPanel jp_caseHistory;
    private javax.swing.JLabel patientID;
    private javax.swing.JLabel patientName;
    private javax.swing.JButton refer;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextArea txt_finalDiag;
    private java.awt.TextField txt_gender;
    private java.awt.TextField txt_height;
    private java.awt.TextField txt_pDoB;
    private java.awt.TextField txt_pID;
    private java.awt.TextField txt_pName;
    private javax.swing.JTextArea txt_toRecv;
    private javax.swing.JTextArea txt_toSend;
    private java.awt.TextField txt_weight;
    // End of variables declaration//GEN-END:variables
}
