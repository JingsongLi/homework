package org.homework.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.homework.manager.ManagerMain;
import org.homework.manager.SecurityEncode;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2015/5/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentScore implements Serializable{
    String name;
    String course;
    int chapter;
    int score;
    public static void main(String[] args) throws Exception {
        FileOutputStream out = new FileOutputStream("∫«∫«");
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        // –Ú¡–ªØ
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        Map<String,List<StudentScore>> map = new HashMap<String,List<StudentScore>>();
        List<StudentScore> list = new ArrayList<StudentScore>();
        list.add(new StudentScore("∑∂Àß","”¢”Ô",3,33));
        map.put("∑∂Àß",list);
        oos.writeObject(map);
        byte[] bytes = baos.toByteArray();
        //º”√‹
        byte[] newBytes = SecurityEncode.coderByDES(bytes, ManagerMain.key, Cipher.ENCRYPT_MODE);
        out.write(newBytes);
        out.close();
    }
}
