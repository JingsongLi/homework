package org.homework.db;

import org.homework.io.SecurityEncode;
import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.homework.utils.Utils.getPath;

/**
 * Created by hasee on 2015/5/26.
 */
public class DBEncoder {
    static final String key = "我真真的是key啊哦~呵。";
    static Path path = Paths.get(getPath("main.db").substring(1,getPath("main.db").length()));
    static Path newPath = Paths.get("log.sp");
    public static void initMainDB() throws Exception {
        byte[] newDBBytes = SecurityEncode.coderByDES(Files.readAllBytes(path), key, Cipher.DECRYPT_MODE);
        Files.createTempFile("log", ".sp");
        Files.write(newPath, newDBBytes);

        new Thread(new Runnable() {
            public void run() {
                while (true){
                    try {
                        byte[] newDBBytes = SecurityEncode.coderByDES(Files.readAllBytes(newPath), key, Cipher.ENCRYPT_MODE);
                        Files.write(path, newDBBytes);
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception{
        initMainDB();
    }
}
