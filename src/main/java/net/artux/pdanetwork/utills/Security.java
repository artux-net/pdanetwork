package net.artux.pdanetwork.utills;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class Security {

    private static final String password = "f8a9a5t";
    private static final String salt = "065af4bea08b1d0a";
    private static TextEncryptor encryptor = Encryptors.text(password, salt);

    public static String encrypt(String textToEncrypt){
        return encryptor.encrypt(textToEncrypt);
    }

    public static String decrypt(String textToDecrypt){
        return encryptor.decrypt(textToDecrypt);
    }

}
