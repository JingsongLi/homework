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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;

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
    String trueCipher = DBConnecter.getKV("cipher");
    private File file;

    public LoginPanel() throws FileNotFoundException {

        this.setLocation(400, 300);
        this.setSize(333, 185);
        this.setTitle("��¼");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lblUsername = new JLabel("�û���:");
        lblUsername.setBounds(24, 28, 70, 15);
        getContentPane().add(lblUsername);

        tfUsername = new JTextField(12);
        tfUsername.setBounds(88, 22, 200, 27);
        getContentPane().add(tfUsername);

        lblPassword = new JLabel("\u5BC6  \u7801:");
        lblPassword.setBounds(24, 70, 70, 15);
        getContentPane().add(lblPassword);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(88, 64, 200, 27);
        getContentPane().add(tfPassword);

        btnOK = new JButton("��¼");
        btnOK.addActionListener(this);
        btnOK.setBounds(110, 110, 80, 25);
        getContentPane().add(btnOK);

        btnExit = new JButton("ȡ��");
        btnExit.setBounds(210, 110, 80, 25);
        btnExit.addActionListener(this);
        getContentPane().add(btnExit);


        file = new File("loginFile");

        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            int i = 0;
            String[] logInfo = new String[2];
            while (scanner.hasNext()) {
                logInfo[i++] = scanner.next();
            }
            scanner.close();
            tfUsername.setText(logInfo[0]);
            tfPassword.setText(logInfo[1]);
        }


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
            //��һ����֤
            if(user == null || !user.getPassword().equals(password)){
                JOptionPane.showMessageDialog(this, "�û������������");
                return;
            }
            String text = SecurityEncode.getFromBASE64(user.getPassword());
            System.out.println("HEHE" + name + " " + text);
            String[] strs = text.split("_");
            System.out.println(strs);
            //�ڶ��غ͵�������֤�����������Կ
            if(!strs[strs.length-2].equals(UserVerify.getCDiskNum()) ||
                    !strs[strs.length-1].equals(trueCipher)){
                JOptionPane.showMessageDialog(this, "����ƥ�����Կ����");
                System.out.println(strs[strs.length-2] + " " + strs[strs.length-1]);
                return;
            }else {
                try {
                    file.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    PrintWriter pw = new PrintWriter(file);
                    pw.println(name);
                    pw.println(password);
                    pw.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                MainFrame frame = new MainFrame(user);
                String title = DBConnecter.getKV("title");
                if(title == null)
                    frame.setTitle("��ҵϵͳ");
                else
                    frame.setTitle(title);
                frame.setVisible(true);
                this.dispose();
            }
        } else if (e.getActionCommand().equals("ȡ��")) {
            System.exit(0);
        }
    }
}
