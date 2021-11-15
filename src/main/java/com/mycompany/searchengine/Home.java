package com.mycompany.searchengine;

import java.awt.Font;
import static java.awt.event.KeyEvent.CHAR_UNDEFINED;
import java.util.ArrayList;
import javax.swing.*;

public class Home extends javax.swing.JFrame {

    public Main obj;
    public ArrayList<ArrayList<String>> searchResults;

    public Home() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAutoscrolls(true);
        jPanel1.setPreferredSize(new java.awt.Dimension(706, 495));
        jPanel1.setLayout(null);

        jButton1.setBackground(new java.awt.Color(255, 102, 102));
        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Search");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(470, 240, 93, 35);

        jTextField1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(51, 51, 51));
        jTextField1.setText("   Search your content here");
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextField1MouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextField1MouseExited(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        jPanel1.add(jTextField1);
        jTextField1.setBounds(120, 240, 336, 35);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui5.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 710, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void searchAction() {
        String s = jTextField1.getText();
        if (s.length() < 5) {
            JOptionPane.showMessageDialog(this, "Word can not be less than 5 letters", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
        } else if (!s.matches("^[a-zA-Z]*$")) {
            JOptionPane.showMessageDialog(this, "Enter Only Alphabets", "Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            searchResults = obj.search(s);
            if (searchResults == null) {
                JOptionPane.showMessageDialog(this, "Word does not exist!", "Dialog", JOptionPane.ERROR_MESSAGE);
            } else {
                SearchResults searchResultsObj = new SearchResults(obj, searchResults, s);
                searchResultsObj.setVisible(true);
                this.dispose();
            }
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        searchAction();
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == 10) {
            searchAction();
        }
        char s = evt.getKeyChar();
        if (s != CHAR_UNDEFINED) {
            if (jTextField1.getText().contains("Search your content here")) {
                jTextField1.setText("");
            }
        }
    }// GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTextField1MouseClicked
        if (jTextField1.getText().contains("Search your content here")) {
            jTextField1.setText("");
        }
    }// GEN-LAST:event_jTextField1MouseClicked

    private void jTextField1MouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTextField1MouseEntered
        if (jTextField1.getText().contains("Search your content here")) {
            jTextField1.setText("");
        }

    }// GEN-LAST:event_jTextField1MouseEntered

    private void jTextField1MouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTextField1MouseExited
        if (jTextField1.getText().equals("")) {
            jTextField1.setText("Search your content here");
        }

    }// GEN-LAST:event_jTextField1MouseExited

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
