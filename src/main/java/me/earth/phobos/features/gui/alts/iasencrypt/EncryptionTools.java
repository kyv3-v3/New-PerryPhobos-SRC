



package me.earth.phobos.features.gui.alts.iasencrypt;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import java.io.*;

public final class EncryptionTools
{
    public static final String DEFAULT_ENCODING = "UTF-8";
    private static final Base64.Encoder encoder;
    private static final Base64.Decoder decoder;
    private static final MessageDigest sha512;
    private static final KeyGenerator keyGen;
    private static final String secretSalt = "${secretSalt}";
    
    public static String decodeOld(final String text) {
        try {
            return new String(EncryptionTools.decoder.decode(text),  "UTF-8");
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static String encode(final String text) {
        try {
            final byte[] data = text.getBytes("UTF-8");
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1,  getSecretKey());
            return new String(EncryptionTools.encoder.encode(cipher.doFinal(data)));
        }
        catch (BadPaddingException e) {
            throw new RuntimeException("The password does not match",  e);
        }
        catch (IllegalBlockSizeException | InvalidKeyException | IOException | NoSuchAlgorithmException | NoSuchPaddingException ex2) {
            final Exception ex;
            final Exception e2 = ex;
            throw new RuntimeException(e2);
        }
    }
    
    public static String decode(final String text) {
        try {
            final byte[] data = EncryptionTools.decoder.decode(text);
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2,  getSecretKey());
            return new String(cipher.doFinal(data),  "UTF-8");
        }
        catch (BadPaddingException e) {
            throw new RuntimeException("The password does not match",  e);
        }
        catch (IllegalBlockSizeException | InvalidKeyException | IOException | NoSuchAlgorithmException | NoSuchPaddingException ex2) {
            final Exception ex;
            final Exception e2 = ex;
            throw new RuntimeException(e2);
        }
    }
    
    public static String generatePassword() {
        EncryptionTools.keyGen.init(256);
        return new String(EncryptionTools.encoder.encode(EncryptionTools.keyGen.generateKey().getEncoded()));
    }
    
    private static MessageDigest getSha512Hasher() {
        try {
            return MessageDigest.getInstance("SHA-512");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static KeyGenerator getAESGenerator() {
        try {
            return KeyGenerator.getInstance("AES");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static SecretKeySpec getSecretKey() {
        try {
            final String password = "${secretSalt}" + Standards.getPassword() + "${secretSalt}";
            final byte[] key = Arrays.copyOf(EncryptionTools.sha512.digest(password.getBytes("UTF-8")),  16);
            return new SecretKeySpec(key,  "AES");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();
        sha512 = getSha512Hasher();
        keyGen = getAESGenerator();
    }
}
