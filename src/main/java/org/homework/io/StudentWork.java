package org.homework.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.homework.manager.ManagerMain;
import org.homework.manager.SecurityEncode;

import javax.crypto.Cipher;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by hasee on 2015/5/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentWork implements Serializable{
    String name;
    String course;
    int chapter;
    Object data;

    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser("F:\\");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            //file½âÎö
            byte[] newBytes = SecurityEncode.coderByDES(bytes, ManagerMain.key, Cipher.DECRYPT_MODE);
            ByteArrayInputStream in = new ByteArrayInputStream(newBytes);
            ObjectInputStream oin = new ObjectInputStream(in);
            StudentWork studentWork = (StudentWork) oin.readObject();
            System.out.println(studentWork);
        }

    }
}
