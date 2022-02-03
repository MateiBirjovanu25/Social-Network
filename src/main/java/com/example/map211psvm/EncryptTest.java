package com.example.map211psvm;

import com.example.map211psvm.utils.RSAEncryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EncryptTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException {
        RSAEncryption coder = new RSAEncryption();
        coder.generateKeys();
//        System.out.println(coder.encode("caine",coder.getPrivateKey()));
//        System.out.println(coder.decode(coder.encode("caine",coder.getPrivateKey()),coder.getPublicKey()));

        System.out.println(coder.getPrivateKey());
        coder.generateKeys();
        System.out.println(coder.getPrivateKey());
    }
}
