package org.homework.main.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by hasee on 2015/5/30.
 */
public class UserVerify {

    public static String getCDiskNum(){
        return getSerialNumber("C");
    }
    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("damn", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\""
                    + drive
                    + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber"; // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;

            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    public static String getMac() {
        try {
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements()) {
                byte[] mac = el.nextElement().getHardwareAddress();
                if (mac == null)
                    continue;

                StringBuilder builder = new StringBuilder();
                for (byte b : mac) {
                    char hex[] = {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'a', 'b', 'c', 'd', 'e', 'f'
                    };
                    builder.append("" + hex[(b >> 4) & 0x0f] + hex[b & 0x0f]);
                    builder.append("-");
                }
                builder.deleteCharAt(builder.length() - 1);
                return builder.toString();

            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println(getSerialNumber("C"));
    }
}
