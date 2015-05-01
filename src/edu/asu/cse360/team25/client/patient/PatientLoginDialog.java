package edu.asu.cse360.team25.client.patient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class PatientLoginDialog extends JDialog {

    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txt_password = new javax.swing.JPasswordField();
        userID = new javax.swing.JLabel();
        txt_uID = new javax.swing.JTextField();
        password = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        signupButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        sign_up = new javax.swing.JLabel();

        setTitle("GetMD Experience");
        setBackground(new java.awt.Color(204, 204, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("American Typewriter", 1, 18), new java.awt.Color(51, 0, 204))); // NOI18N

        txt_password.setText("jPasswordField1");

        userID.setText("UserID");

        password.setText("Password");

        loginButton.setText("Login");

        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(password)
                                .addComponent(userID))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                                .addComponent(txt_uID))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loginButton)
                        .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(userID)
                                .addComponent(txt_uID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(password)
                                .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loginButton)
                        .addGap(0, 6, Short.MAX_VALUE))
        );

        signupButton.setText("Sign Up");
        signupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        sign_up.setText("New to GetMD Experience?");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sign_up)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(signupButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitButton))
                .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jLabel1))
                .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGap(18, 18, 18)
                )
                .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                               )
                        .addGap(26, 26, 26)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(signupButton)
                                .addComponent(exitButton)
                                .addComponent(sign_up))
                        .addGap(0, 21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>  

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int id = 0;
        try {
            id = Integer.parseInt(txt_uID.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid userID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PatientLoginDialog.this.pc.whichButtonOnLogin = 1;
        PatientLoginDialog.this.pc.setLoginInfo(id,
                new String(txt_password.getPassword()), true);
        dispose();
        //JOptionPane.showMessageDialog(null, "error occured");

    }

    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        PatientLoginDialog.this.pc.whichButtonOnLogin = 2;
        dispose();
    }

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        PatientLoginDialog.this.pc.whichButtonOnLogin = 0;
        dispose();
    }
    /**
     *
     */
    private static final long serialVersionUID = 7969631039381989668L;

    PatientClient pc;

    public PatientLoginDialog(Frame parent, PatientClient pc, int id, String password) {

        super(parent, "Login", true);
        this.pc = pc;
        initComponents();

    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel password;
    private javax.swing.JLabel sign_up;
    private javax.swing.JButton signupButton;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_uID;
    private javax.swing.JLabel userID;
    // End of variables declaration                   
}
