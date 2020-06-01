package com.ixingji.agent.guarder.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtils.class);

    private static final String AES = "AES";

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private static String defaultKey;

    static {
        try {
            defaultKey = generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        return Hex.encodeHexString(keyBytes);
    }

    public static String encode(String data) throws Exception {
        return encode(data, defaultKey);
    }

    public static String encode(String data, String specifiedKey) throws Exception {
        Key key = new SecretKeySpec(Hex.decodeHex(specifiedKey), AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(data.getBytes());
        return Hex.encodeHexString(result);
    }

    public static String decode(String data) throws Exception {
        return decode(data, defaultKey);
    }

    public static String decode(String data, String specifiedKey) throws Exception {
        Key key = new SecretKeySpec(Hex.decodeHex(specifiedKey), AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(data.getBytes());
        return Hex.encodeHexString(result);
    }

}
