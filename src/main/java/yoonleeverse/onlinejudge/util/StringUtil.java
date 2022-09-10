package yoonleeverse.onlinejudge.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {

    public static String encryptMD5(String input) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            result = bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
