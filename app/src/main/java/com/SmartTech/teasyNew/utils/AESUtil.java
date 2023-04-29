package com.SmartTech.teasyNew.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class AESUtil {

    public static String generateKey() {
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);

        return Base64.encodeToString(keyBytes, Base64.DEFAULT);
    }

    public static String decrypt(String key, String data) throws Exception {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        byte[] decrypted = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
        return new String(decrypted);
    }

    public static String encrypt(String key, String data) throws Exception {
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    private static Cipher getCipher(String key, int mode) throws Exception {
        byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);

        byte[] aesKey = Arrays.copyOfRange(keyBytes, 0, 16);
        byte[] iv = Arrays.copyOfRange(keyBytes, 16, 32);

        SecretKey secretKey = new SecretKeySpec(aesKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, new IvParameterSpec(iv));

        return cipher;
    }

}
