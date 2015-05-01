package edu.asu.cse360.team25.client.patient;

import edu.asu.cse360.team25.client.doctor.DoctorServerConnection;
import edu.asu.cse360.team25.client.patient.PatientServerConnection;
import edu.asu.cse360.team25.client.patient.PatientServerConnection.ConnectionState;
import edu.asu.cse360.team25.protocol.CaseInfo;
import edu.asu.cse360.team25.protocol.ChatInfo;
import edu.asu.cse360.team25.protocol.DoctorInfo;
import edu.asu.cse360.team25.protocol.PatientInfo;
import edu.asu.cse360.team25.protocol.exception.InvalidProtocolStateException;
import edu.asu.cse360.team25.protocol.exception.ProtocolErrorException;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
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
public class PatientMainFrame extends javax.swing.JFrame {

    protected PatientClient pc;
    protected PatientServerConnection psc;
    protected int selectedDoctorID = -1;
    protected String selectedDoctorState=null;
    protected boolean finishable=false;

    public PatientMainFrame(PatientClient pc) {
        initComponents();

        this.pc = pc;

    }

    /**
     * Creates new form patient
     */
    public void showPatientInfo() {
        PatientInfo pi = pc.pi;
        if (pi == null) {
            return;
        }
        this.txt_name.setText(pi.getName());
        this.txt_DoB.setText(pi.getBirthday());
        this.txt_uID.setText("id=<" + String.valueOf(pi.getPatientID()) + ">");
        this.txt_weight.setText(pi.getWeight());
        this.txt_height.setText(pi.getHeight());
        this.txt_gender.setText(pi.getGender());
        repaint();
    }

    public void showDoctorList() {
        List<DoctorInfo> doctors = pc.doctors;
        if (doctors == null) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) doctorList.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (DoctorInfo di : doctors) {
            Object[] os = new Object[model.getColumnCount()];
            os[0] = di.getDoctorID();
            os[1] = di.getName();
            os[2] = di.getDepartment();
            os[3] = di.getExpertise();
            os[4] = di.getState();
            os[5] = di.getRate();

            model.addRow(os);
            doctorList.setModel(model);
        } //
    }

    protected void showChatReceived(String content) {
        ta_chatRev.setText(content);
    }

    protected void showCaseList() {

        List<CaseInfo> cases = pc.cases;

        if (cases == null) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) historyList.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (CaseInfo ci : cases) {
            Object[] os = new Object[model.getColumnCount()];
            os[0] = ci.getCaseID();
            os[1] = ci.getDoctorID();
            os[2] = ci.getDateTime();
            os[3] = ci.getState();
            os[4] = ci.getSymptom();
            model.addRow(os);
        }

    }

    protected void createCase() {
        try {
            psc.sendCreateCase(Integer.toString(Integer.parseInt(cb_Nausea.getSelectedItem().toString()) + Integer.parseInt(cb_anxiety.getSelectedItem().toString()) + Integer.parseInt(cb_depression.getSelectedItem().toString()) + Integer.parseInt(cb_fatigue.getSelectedItem().toString()) + Integer.parseInt(cb_pain.getSelectedItem().toString())), txt_describe.getText(), 10);
            JOptionPane.showMessageDialog(this,
                    "Case has been successfully created.");
        } catch (IOException e) {
        } catch (InvalidProtocolStateException e) {
        }
    }

    protected void showChatHistory() {

        List<ChatInfo> chatList = pc.chatList;

        if (chatList == null) {
            return;
        }

        Collections.sort(chatList, new Comparator<ChatInfo>() {

            @Override
            public int compare(ChatInfo t, ChatInfo t1) {
                return t.getChatID() - t1.getChatID();
            }
        });

        ta_chatRev.setText("");
        for (ChatInfo ci : chatList) {

            ta_chatRev.append(ci.getMessage() + "\n");
        }

    }

    protected void updatePatientInfo() {
        try {
            psc.sendUpdatePatientProfile(txt_name.getText(), txt_gender.getText(), txt_height.getText(), txt_weight.getText(), txt_DoB.getText());
            JOptionPane.showMessageDialog(this,
                    "Patient profile update successfully.");
        } catch (IOException e) {
            return;
        } catch (InvalidProtocolStateException e) {
            System.out.println("Can not send UpdatePatientProfile message when processing case or before login!!!");
            return;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jd_rate = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jb_wf = new javax.swing.JButton();
        jb_ok = new javax.swing.JButton();
        jb_as = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ta_chatSent = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        ta_chatRev = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        jb_finish = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        doctorList = new javax.swing.JTable();
        lb_department = new javax.swing.JLabel();
        lb_searchDoc = new javax.swing.JLabel();
        txt_docDep = new javax.swing.JTextField();
        lb_department1 = new javax.swing.JLabel();
        txt_docExp = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        historyList = new javax.swing.JTable();
        lb_myVisiHist = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        txt_describe = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        lb_symptDesc = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_detSymptDesc = new javax.swing.JLabel();
        lb_detSympt = new javax.swing.JLabel();
        cb_Nausea = new javax.swing.JComboBox();
        cb_depression = new javax.swing.JComboBox();
        cb_fatigue = new javax.swing.JComboBox();
        cb_pain = new javax.swing.JComboBox();
        cb_anxiety = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lb_uID = new javax.swing.JLabel();
        lb_name = new javax.swing.JLabel();
        lb_DoB = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_uID = new java.awt.TextField();
        txt_name = new java.awt.TextField();
        txt_DoB = new java.awt.TextField();
        txt_gender = new java.awt.TextField();
        txt_height = new java.awt.TextField();
        txt_weight = new java.awt.TextField();
        editProfile = new javax.swing.JToggleButton();
        jb_appoit = new javax.swing.JButton();
        jb_searchD = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();

        jd_rate.setMinimumSize(new java.awt.Dimension(501, 216));

        jLabel6.setFont(new java.awt.Font("宋体", 3, 18)); // NOI18N
        jLabel6.setText("        How do you like your experience?");

        jb_wf.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
        jb_wf.setText("Awful");
        jb_wf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_wfActionPerformed(evt);
            }
        });

        jb_ok.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
        jb_ok.setText("Okay");
        jb_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_okActionPerformed(evt);
            }
        });

        jb_as.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
        jb_as.setText("Awsome;)");
        jb_as.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_asActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_rateLayout = new javax.swing.GroupLayout(jd_rate.getContentPane());
        jd_rate.getContentPane().setLayout(jd_rateLayout);
        jd_rateLayout.setHorizontalGroup(
            jd_rateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_rateLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jb_wf, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jb_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jb_as, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addGroup(jd_rateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_rateLayout.setVerticalGroup(
            jd_rateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_rateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(31, 31, 31)
                .addGroup(jd_rateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_wf, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_as, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("GetMD Experience");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1335, 693));
        getContentPane().setLayout(null);

        jLabel4.setFont(new java.awt.Font("Malayalam MN", 3, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Patient Management");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 10, 390, 50);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Live-Chat", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Abadi MT Condensed Extra Bold", 1, 14))); // NOI18N

        ta_chatSent.setColumns(20);
        ta_chatSent.setRows(5);
        jScrollPane3.setViewportView(ta_chatSent);

        ta_chatRev.setColumns(20);
        ta_chatRev.setRows(5);
        jScrollPane4.setViewportView(ta_chatRev);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        jb_finish.setText("Rate the Doctor and Finish the Case");
        jb_finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_finishActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jb_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sendButton)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(jb_finish)))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(840, 230, 464, 400);

        doctorList.setModel(new javax.swing.table.DefaultTableModel(
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
        doctorList.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                doctorListComponentAdded(evt);
            }
        });
        doctorList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doctorListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(doctorList);

        lb_department.setFont(new java.awt.Font("Lucida Grande", 2, 14)); // NOI18N
        lb_department.setText("Department:");

        lb_searchDoc.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_searchDoc.setText("Search for Doctor");

        lb_department1.setFont(new java.awt.Font("Lucida Grande", 2, 14)); // NOI18N
        lb_department1.setText("Expertise:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_searchDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lb_department, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_docDep, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lb_department1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_docExp, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 57, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lb_searchDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_department)
                    .addComponent(txt_docDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_department1)
                    .addComponent(txt_docExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(30, 270, 440, 360);

        historyList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "by Doctor", "Date/Time", "State", "Symptom"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(historyList);

        lb_myVisiHist.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_myVisiHist.setText("My Visit History");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_myVisiHist, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lb_myVisiHist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel4);
        jPanel4.setBounds(840, 90, 456, 135);

        saveButton.setText("Create a case with the selected doctor");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        txt_describe.setColumns(20);
        txt_describe.setRows(5);
        jScrollPane6.setViewportView(txt_describe);

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel10.setText("Depression");

        lb_symptDesc.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_symptDesc.setText("Detailed Description for Symptoms");

        jLabel13.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel13.setText("Anxiety");

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel9.setText("Nausea");

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel11.setText("Fatigue");

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel12.setText("Pain");

        lb_detSymptDesc.setText("Please input symptoms on the scale with 1(least severe) ");

        lb_detSympt.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_detSympt.setText("Determine Symptoms");

        cb_Nausea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        cb_depression.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        cb_fatigue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        cb_pain.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        cb_anxiety.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        jLabel5.setText("to 10(most severe)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_detSympt, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_detSymptDesc)
                            .addComponent(lb_symptDesc)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel13))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cb_anxiety, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_Nausea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_depression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_fatigue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_pain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(saveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_detSympt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_detSymptDesc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cb_Nausea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cb_depression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cb_fatigue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cb_pain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cb_anxiety, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_symptDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addContainerGap())
        );

        getContentPane().add(jPanel5);
        jPanel5.setBounds(470, 90, 350, 540);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "My Profile", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Abadi MT Condensed Extra Bold", 1, 14), new java.awt.Color(0, 0, 204))); // NOI18N

        lb_uID.setText("UserID: ");

        lb_name.setText("Name:");

        lb_DoB.setText("Date of Birth:");

        jLabel1.setText("Gender:");

        jLabel2.setText("Height:");

        jLabel3.setText("Weight:");

        txt_uID.setEditable(false);
        txt_uID.setText("               ");
        txt_uID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_uIDActionPerformed(evt);
            }
        });

        txt_name.setEditable(false);

        txt_DoB.setEditable(false);

        txt_gender.setEditable(false);
        txt_gender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_genderActionPerformed(evt);
            }
        });

        txt_height.setEditable(false);

        txt_weight.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_uID)
                    .addComponent(lb_DoB)
                    .addComponent(lb_name)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 223, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_DoB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_gender, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_height, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_weight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_uID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(lb_uID)
                                                    .addComponent(txt_uID, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lb_name))
                                            .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lb_DoB))
                                    .addComponent(txt_DoB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1))
                            .addComponent(txt_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(txt_height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel3))
                    .addComponent(txt_weight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(30, 80, 430, 190);

        editProfile.setText("Edit Profile");
        editProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfileActionPerformed(evt);
            }
        });
        getContentPane().add(editProfile);
        editProfile.setBounds(340, 60, 117, 23);

        jb_appoit.setText("Make appointment");
        jb_appoit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_appoitActionPerformed(evt);
            }
        });
        getContentPane().add(jb_appoit);
        jb_appoit.setBounds(40, 630, 140, 23);

        jb_searchD.setText("Search Doctor");
        jb_searchD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_searchDActionPerformed(evt);
            }
        });
        getContentPane().add(jb_searchD);
        jb_searchD.setBounds(300, 630, 160, 23);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(exitButton);
        exitButton.setBounds(210, 630, 80, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        if (psc.state != ConnectionState.IN_CASE) {
            JOptionPane.showMessageDialog(this, "Please create a case with a doctor first!");
        } else {
            try {
                psc.sendChatMessage(ta_chatSent.getText());
                // TODO add your handling code here:
            } catch (Exception e) {
                e.printStackTrace();
            }   // TODO add your handling code here:// TODO add your handling code here:
        }

    }//GEN-LAST:event_sendButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        dispose();

    }//GEN-LAST:event_exitButtonActionPerformed

    private void editProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfileActionPerformed
        if (this.editProfile.isSelected()) {

            this.txt_gender.setEditable(true);
            this.txt_name.setEditable(true);
            this.txt_height.setEditable(true);
            this.txt_weight.setEditable(true);
            this.txt_DoB.setEditable(true);
        } else {
            this.txt_gender.setEditable(false);
            this.txt_name.setEditable(false);
            this.txt_height.setEditable(false);
            this.txt_weight.setEditable(false);
            this.txt_DoB.setEditable(false);
            updatePatientInfo();

        } // TODO add your handling code here:
    }//GEN-LAST:event_editProfileActionPerformed

    private void txt_uIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_uIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_uIDActionPerformed

    private void txt_genderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_genderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_genderActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (selectedDoctorID == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor first!");
        } else {
            try {

                if ((Integer.parseInt(cb_Nausea.getSelectedItem().toString()) + Integer.parseInt(cb_anxiety.getSelectedItem().toString()) + Integer.parseInt(cb_depression.getSelectedItem().toString()) + Integer.parseInt(cb_fatigue.getSelectedItem().toString()) + Integer.parseInt(cb_pain.getSelectedItem().toString())) > 20
                        && selectedDoctorState=="FREE" ) {
                    psc.sendCreateCase(Integer.toString(Integer.parseInt(cb_Nausea.getSelectedItem().toString()) + Integer.parseInt(cb_anxiety.getSelectedItem().toString()) + Integer.parseInt(cb_depression.getSelectedItem().toString()) + Integer.parseInt(cb_fatigue.getSelectedItem().toString()) + Integer.parseInt(cb_pain.getSelectedItem().toString())), txt_describe.getText(), selectedDoctorID);
                    JOptionPane.showMessageDialog(this,
                            "Case has been successfully created.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error! Your painess is too low to talk to a doctor OR the doctor you selected is not available at this moment");
                }

            } catch (IOException e) {
            } catch (InvalidProtocolStateException e) {
            }
        }


    }//GEN-LAST:event_saveButtonActionPerformed

    private void doctorListComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_doctorListComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_doctorListComponentAdded

    private void jb_searchDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_searchDActionPerformed
        try {
            String department = this.txt_docDep.getText().trim();
            String expertise = this.txt_docExp.getText().trim();

            if (department.isEmpty()) {
                department = "*";
            }
            if (expertise.isEmpty()) {
                expertise = "*";
            }

            psc.sendQueryDoctorList(department, expertise);
        } catch (IOException ex) {
            Logger.getLogger(PatientMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolErrorException ex) {
            Logger.getLogger(PatientMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jb_searchDActionPerformed

    private void jb_appoitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_appoitActionPerformed
        Appointment ap = new Appointment();
        ap.setVisible(true);
        dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_appoitActionPerformed

    private void doctorListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doctorListMouseClicked
        int row = doctorList.getSelectedRow();
        try {
            selectedDoctorID = Integer.valueOf(doctorList.getValueAt(row, 0).toString());
            selectedDoctorState=doctorList.getValueAt(row, 4).toString();
            System.out.println(selectedDoctorID);
        } catch (Exception e) {
            e.printStackTrace();
        }      // TODO add your handling code here:
    }//GEN-LAST:event_doctorListMouseClicked

    private void jb_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_finishActionPerformed
        if (psc.state != ConnectionState.IN_CASE) {
            JOptionPane.showMessageDialog(this, "Please create a case with a doctor first!");
        } else if(finishable) {
            dispose();
            jd_rate.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this, "Please let the doctor finish the case first!");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jb_finishActionPerformed

    private void jb_wfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_wfActionPerformed
        try {
            jd_rate.setVisible(false);
            psc.sendForwardFinishCaseAck(1);
            synchronized (this) {
                wait(10);
            }
            psc.sendLogout();
           
        } catch (Exception e) {
            e.printStackTrace();
        }// TODO add your handling code here:
    }//GEN-LAST:event_jb_wfActionPerformed

    private void jb_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_okActionPerformed
        try {
            jd_rate.setVisible(false);
            psc.sendForwardFinishCaseAck(5);
            synchronized (this) {
                wait(10);
            }
            psc.sendLogout();
           
        } catch (Exception e) {
            e.printStackTrace();
        }// TODO add your handling code here:
    }//GEN-LAST:event_jb_okActionPerformed

    private void jb_asActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_asActionPerformed
        try {
            jd_rate.setVisible(false);
            psc.sendForwardFinishCaseAck(10);
            synchronized (this) {
                wait(10);
            }
            psc.sendLogout();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }// TODO add your handling code here:
        
    }//GEN-LAST:event_jb_asActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cb_Nausea;
    private javax.swing.JComboBox cb_anxiety;
    private javax.swing.JComboBox cb_depression;
    private javax.swing.JComboBox cb_fatigue;
    private javax.swing.JComboBox cb_pain;
    private javax.swing.JTable doctorList;
    private javax.swing.JToggleButton editProfile;
    private javax.swing.JButton exitButton;
    private javax.swing.JTable historyList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton jb_appoit;
    private javax.swing.JButton jb_as;
    private javax.swing.JButton jb_finish;
    private javax.swing.JButton jb_ok;
    private javax.swing.JButton jb_searchD;
    private javax.swing.JButton jb_wf;
    private javax.swing.JDialog jd_rate;
    private javax.swing.JLabel lb_DoB;
    private javax.swing.JLabel lb_department;
    private javax.swing.JLabel lb_department1;
    private javax.swing.JLabel lb_detSympt;
    private javax.swing.JLabel lb_detSymptDesc;
    private javax.swing.JLabel lb_myVisiHist;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_searchDoc;
    private javax.swing.JLabel lb_symptDesc;
    private javax.swing.JLabel lb_uID;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextArea ta_chatRev;
    private javax.swing.JTextArea ta_chatSent;
    private java.awt.TextField txt_DoB;
    private javax.swing.JTextArea txt_describe;
    private javax.swing.JTextField txt_docDep;
    private javax.swing.JTextField txt_docExp;
    private java.awt.TextField txt_gender;
    private java.awt.TextField txt_height;
    private java.awt.TextField txt_name;
    private java.awt.TextField txt_uID;
    private java.awt.TextField txt_weight;
    // End of variables declaration//GEN-END:variables
}
