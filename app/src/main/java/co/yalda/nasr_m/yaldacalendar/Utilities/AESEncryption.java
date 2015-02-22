package co.yalda.nasr_m.yaldacalendar.Utilities;

/**
 * Created by Nasr_M on 2/22/2015.
 */

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {

    private static final String SecretKey = "YaldaBahamCalend";
    private static final String Salt = "03xy9z52twq8r4s1uv67";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final String SECRET_KEY_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final int PBE_ITERATION_COUNT = 100;
    private static final int PBE_KEY_LENGTH = 256;
    private static final int IV_LENGTH = 16;
    private Cipher cipher;
    private Random randomInt;

    public AESEncryption() {
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            cipher = null;
        }
        catch (NoSuchPaddingException e) {
            cipher = null;
        }
        randomInt = new Random();
    }

    public SecretKey getSecretKey(String password) {
        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), Salt.getBytes("UTF-8"), PBE_ITERATION_COUNT, PBE_KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            SecretKey tmp = factory.generateSecret(pbeKeySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
            return secret;
        }
        catch (Exception e) {
            return null;
        }
    }

    public String encrypt(String text){
        try {
            Thread.sleep(randomInt.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (text == null || text.length() == 0) {
            return null;
        }
        byte[] encrypted = null;
        byte[] iv = generateIV();
        try {
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(SecretKey), ivspec);
            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        }
        catch (Exception e) {
            return null;
        }
        byte[] res = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, res, 0, iv.length);
        System.arraycopy(encrypted, 0, res, iv.length, encrypted.length);
        return Base64.encodeToString(iv, Base64.DEFAULT) + Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public String decrypt(String code){
        try {
            Thread.sleep(randomInt.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(code == null || code.length() == 0) {
            return null;
        }
        byte[] decrypted = null;
        try {
            String iv64 = code.substring(0, 24);
            String encrypted64 = code.substring(25);
            byte[] iv = Base64.decode(iv64, Base64.DEFAULT);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(SecretKey), ivspec);
            decrypted = cipher.doFinal(Base64.decode(encrypted64, Base64.DEFAULT));
        }
        catch (Exception e) {
            return null;
        }
        try {
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "exception";
    }

    private byte[] generateIV() {
        try {
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);
            return iv;
        }
        catch (Exception e) {
            return null;
        }
    }

    /*public static String bytesToHex(byte[] data) {
        String HEXES = "0123456789ABCDEF";
        if (data == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2*data.length);
        for (final byte b : data) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }*/


    /*public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        }
        else if (str.length() < 2) {
            return null;
        }
        else {
            int len = str.length()/2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }*/

}