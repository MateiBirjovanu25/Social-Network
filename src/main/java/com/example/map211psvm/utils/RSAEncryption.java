package com.example.map211psvm.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryption {
    String publicKey;
    String privateKey;
    KeyPair pair;
    KeyPairGenerator keyPairGenerator;
    SecureRandom secureRandom;

    public RSAEncryption() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        secureRandom = new SecureRandom();
        keyPairGenerator.initialize(2048,secureRandom);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public KeyPair getPair() {
        return pair;
    }

    public void generateKeys() throws NoSuchAlgorithmException {
        this.pair = keyPairGenerator.generateKeyPair();
        this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        this.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
    }

    public String encode(String inputString, String privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {

        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes("utf-8"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey key = fact.generatePrivate(keySpec);

        Cipher encryptionCipher = Cipher.getInstance("RSA");
        encryptionCipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] output = encryptionCipher.doFinal(inputString.getBytes());
        String outputString = Base64.getEncoder().encodeToString(output);
        return outputString;
    }

    public String decode(String inputString, String publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {

        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes("utf-8"));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(spec);

        Cipher encryptionCipher = Cipher.getInstance("RSA");
        encryptionCipher.init(Cipher.DECRYPT_MODE,key);
        byte[] output = encryptionCipher.doFinal(Base64.getDecoder().decode(inputString));
        String outputString = new String(output);
        return outputString;
    }
}
