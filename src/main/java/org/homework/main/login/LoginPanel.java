package org.homework.main.login;

import org.homework.db.DBConnecter;
import org.homework.db.model.User;
import org.homework.main.MainFrame;
import org.homework.manager.SecurityEncode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hasee on 2015/5/30.
 */
public class LoginPanel  extends JFrame implements ActionListener {
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnOK;
    private JButton btnExit;

    public LoginPanel(){
        JPanel p1 = new JPanel();
        p1.setBorder(new EmptyBorder(20, 30, 10, 30));
        p1.setLayout(new BorderLayout());
        lblUsername = new JLabel("�û���:");
        tfUsername = new JTextField(12);
        p1.add(lblUsername, BorderLayout.WEST);
        p1.add(tfUsername, BorderLayout.CENTER);
        JPanel p2 = new JPanel();
        p2.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        p2.setLayout(new BorderLayout());
        lblPassword = new JLabel("\u5BC6  \u7801:");
        tfPassword = new JPasswordField(12);
        p2.add(lblPassword, BorderLayout.WEST);
        p2.add(tfPassword, BorderLayout.CENTER);
        JPanel p3 = new JPanel();
        p3.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnOK = new JButton("��¼");
        btnOK.addActionListener(this);
        btnExit = new JButton("ȡ��");
        btnExit.addActionListener(this);
        p3.add(btnOK);
        p3.add(btnExit);
        getContentPane().add(p1, BorderLayout.NORTH);
        getContentPane().add(p2, BorderLayout.CENTER);
        getContentPane().add(p3, BorderLayout.SOUTH);
        this.setLocation(400, 300);
        this.setSize(294, 175);
        this.setTitle("��¼");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("��¼")) {
            String name = tfUsername.getText();
            String password = new String(tfPassword.getPassword());
            System.out.println(name + " " + password);
            if(name.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(this, "�û��������벻��Ϊ�գ�");
                return;
            }
            User user = DBConnecter.getUser(name);
            if(user == null || !user.getPassword().equals(password)){
                JOptionPane.showMessageDialog(this, "�û������������");
                return;
            }
            String text = SecurityEncode.getFromBASE64( user.getPassword());
            String[] strs = text.split("_");
            if(strs[strs.length-1] .equals(UserVerify.getCDiskNum())){
                MainFrame frame = new MainFrame(user);
                String title = DBConnecter.getKV("title");
                if(title == null)
                    frame.setTitle("��ҵϵͳ");
                else
                    frame.setTitle(title);
                frame.setVisible(true);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "�û������ƥ�����");
                return;
            }
        } else if (e.getActionCommand().equals("ȡ��")) {
            System.exit(0);
        }
    }
}