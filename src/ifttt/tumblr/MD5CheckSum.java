/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ifttt.tumblr;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5CheckSum {

    public static byte[] createChecksum(String filename) throws Exception {
        MessageDigest complete;
        try (InputStream fis = new FileInputStream(filename)) {
            byte[] buffer = new byte[1024];
            complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
        }
        return complete.digest();
    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
