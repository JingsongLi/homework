package org.homework.login;

import org.homework.db.DBConnecter;
import org.homework.db.model.User;
import org.homework.main.MainFrame;

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
        lblUsername = new JLabel("用户名:");
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
        btnOK = new JButton("登录");
        btnOK.addActionListener(this);
        btnExit = new JButton("取消");
        btnExit.addActionListener(this);
        p3.add(btnOK);
        p3.add(btnExit);
        getContentPane().add(p1, BorderLayout.NORTH);
        getContentPane().add(p2, BorderLayout.CENTER);
        getContentPane().add(p3, BorderLayout.SOUTH);
        this.setLocation(400, 300);
        this.setSize(294, 175);
        this.setTitle("登录");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("登录")) {
            String name = tfUsername.getText();
            String password = new String(tfPassword.getPassword());
            System.out.println(name + " " + password);
            if(name.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(this, "用户名和密码不能为空！");
                return;
            }
            User user = DBConnecter.getUser(name);
            if(user == null || !user.getPassword().equals(password)){
                JOptionPane.showMessageDialog(this, "用户名或密码错误！");
                return;
            }
            if(user.getFinger().equals(UserVerify.getCDiskNum())){
                MainFrame frame = new MainFrame(user);
                frame.setTitle("作业系统");
                frame.setVisible(true);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "用户与机器匹配错误！");
                return;
            }
        } else if (e.getActionCommand().equals("取消")) {
            System.exit(0);
        }
    }
}
