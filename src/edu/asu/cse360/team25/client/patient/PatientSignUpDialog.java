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

public class PatientSignUpDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = -7754480387257955405L;

    PatientClient pc;
    // Variables declaration - do not modify                     

    // End of variables declaration           
    /* Duo's Code
     private JTextField tfUsername;
     private JTextField tfGender;
     private JTextField tfHeight;
     private JTextField tfWeight;
     private JTextField tfBirthday;
     private JPasswordField pfPassword;
     private JPasswordField pfPasswordCheck;
     private JLabel lbUsername;
     private JLabel lbPassword;
     private JLabel lbPasswordCheck;
     private JButton btnSignUp;
     private JButton btnCancel;
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txt_name = new javax.swing.JTextField();
        lbName = new javax.swing.JLabel();
        txt_password2 = new javax.swing.JPasswordField();
        lbPword = new javax.swing.JLabel();
        lbPword2 = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        createButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lbHeight = new javax.swing.JLabel();
        lbWeight = new javax.swing.JLabel();
        lbBirthdate = new javax.swing.JLabel();
        txt_height = new javax.swing.JTextField();
        txt_weight = new javax.swing.JTextField();
        txt_birthdate = new javax.swing.JTextField();
        lbGender = new javax.swing.JLabel();
        txt_gender = new javax.swing.JTextField();

        setTitle("Welcome to GetMD Experience");

        lbName.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        lbName.setText("Name: ");

        lbPword.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        lbPword.setText("Please enter a password: ");

        lbPword2.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        lbPword2.setText("Confirm your password: ");

        createButton.setText("Create Account");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        lbHeight.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        lbHeight.setText("Height: ");

        lbWeight.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        lbWeight.setText("Weight:");

        lbBirthdate.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        lbBirthdate.setText("Birthday:");

        lbGender.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        lbGender.setText("Gender:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(66, 66, 66)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel1)))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lbBirthdate)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lbPword)
                                                                        .addComponent(lbName)
                                                                        .addComponent(lbPword2)
                                                                        .addComponent(lbHeight))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(lbWeight)
                                                                        .addGap(120, 120, 120))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(lbGender)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txt_password2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txt_height, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txt_weight, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txt_birthdate, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txt_gender, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(137, 137, 137)
                                        .addComponent(createButton)))
                        .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbPword, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbPword2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_password2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_birthdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbBirthdate, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>      

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String pass1 = new String(txt_password.getPassword());
        String pass2 = new String(txt_password2.getPassword());

        if (pass1.isEmpty() || pass2.isEmpty() || txt_name.getText().isEmpty() || txt_birthdate.getText().isEmpty() || txt_weight.getText().isEmpty() || txt_height.getText().isEmpty() || txt_gender.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Oops! It seems you forgot to fill out something ;)");

        } else if (pass1.equals(pass2)) {
            PatientSignUpDialog.this.pc.whichButtonOnSignUp = 1;
            PatientSignUpDialog.this.pc.usernameSU = txt_name.getText();
            PatientSignUpDialog.this.pc.passwordSU = pass1;
            PatientSignUpDialog.this.pc.genderSU = txt_gender.getText();
            PatientSignUpDialog.this.pc.heightSU = txt_height.getText();
            PatientSignUpDialog.this.pc.weightSU = txt_weight.getText();
            PatientSignUpDialog.this.pc.birthdaySU = txt_birthdate.getText();
            JOptionPane.showMessageDialog(null, "Account has been successfully created");
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Passwords didn't match");
        }

    }

    public PatientSignUpDialog(Frame parent, PatientClient pc) {
        super(parent, "Sign Up", true);
        this.pc = pc;
        initComponents();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton createButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lbBirthdate;
    private javax.swing.JLabel lbGender;
    private javax.swing.JLabel lbHeight;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbPword;
    private javax.swing.JLabel lbPword2;
    private javax.swing.JLabel lbWeight;
    private javax.swing.JTextField txt_birthdate;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_gender;
    private javax.swing.JTextField txt_height;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JPasswordField txt_password2;
    private javax.swing.JTextField txt_weight;
    // End of variables declaration               
}
