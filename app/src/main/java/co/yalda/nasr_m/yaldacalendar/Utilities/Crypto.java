package co.yalda.nasr_m.yaldacalendar.Utilities;

/**
 * Created by Nasr_M on 2/22/2015.
 */

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Usage:
 * <pre>
 * String crypto = SimpleCrypto.encrypt(masterpassword, cleartext)
 * ...
 * String cleartext = SimpleCrypto.decrypt(masterpassword, crypto)
 * </pre>
 * @author ferenc.hechler
 */
public class Crypto {

    public static byte[] myEncrypt(String key, String plainText) throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        keyGenerator.init(128,secureRandom);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyGenerator.generateKey());
        return cipher.doFinal(plainText.getBytes());
    }

    public static byte[] myDecrypt(String key, byte[] cipherText) throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        keyGenerator.init(128,secureRandom);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keyGenerator.generateKey(), new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(cipherText);
    }

    public static byte[] encrypt(String seed, String cleartext) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return result;
    }

    public static String decrypt(String seed, byte[] encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
//        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, encrypted);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

//    public static String encrypt(final String plainMessage,
//                                 final String symKeyHex) throws GeneralSecurityException {
//        final byte[] encodedMessage = plainMessage.getBytes();
//        final byte[] symKeyData = symKeyHex.getBytes();
//
//        try {
//            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            final int blockSize = cipher.getBlockSize();
//
//            // create the key
//            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");
//
//            // generate random IV using block size (possibly create a method for
//            // this)
//            final byte[] ivData = new byte[blockSize];
//            final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
//            rnd.nextBytes(ivData);
//            final IvParameterSpec iv = new IvParameterSpec(ivData);
//
//            cipher.init(Cipher.ENCRYPT_MODE, symKey, iv);
//
//            final byte[] encryptedMessage = cipher.doFinal(encodedMessage);
//
//            // concatenate IV and encrypted message
//            final byte[] ivAndEncryptedMessage = new byte[ivData.length
//                    + encryptedMessage.length];
//            System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
//            System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage,
//                    blockSize, encryptedMessage.length);
//
//            return ivAndEncryptedMessage.toString();
//        } catch (InvalidKeyException e) {
//            throw new IllegalArgumentException(
//                    "key argument does not contain a valid AES key");
//        } catch (GeneralSecurityException e) {
//            throw new IllegalStateException(
//                    "Unexpected exception during encryption", e);
//        }
//    }
//
//    public static String decrypt(final String ivAndEncryptedMessage,
//                                 final String symKeyHex) {
//        try {
//            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            final int blockSize = cipher.getBlockSize();
//            final byte[] symKeyData = symKeyHex.getBytes();
//
//            // create the key
//            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");
//
//            // retrieve random IV from start of the received message
//            final byte[] ivData = new byte[blockSize];
//            System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
//            final IvParameterSpec iv = new IvParameterSpec(ivData);
//
//            // retrieve the encrypted message itself
//            final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length()
//                    - blockSize];
//            System.arraycopy(ivAndEncryptedMessage, blockSize,
//                    encryptedMessage, 0, encryptedMessage.length);
//
//            cipher.init(Cipher.DECRYPT_MODE, symKey, iv);
//
//            final byte[] encodedMessage = cipher.doFinal(encryptedMessage);
//
//            // concatenate IV and encrypted message
//            final String message = encodedMessage.toString();
//
//            return message;
//        } catch (InvalidKeyException e) {
//            throw new IllegalArgumentException(
//                    "key argument does not contain a valid AES key");
//        } catch (BadPaddingException e) {
//            // you'd better know about padding oracle attacks
//            return null;
//        } catch (GeneralSecurityException e) {
//            throw new IllegalStateException(
//                    "Unexpected exception during decryption", e);
//        }
//    }

}
