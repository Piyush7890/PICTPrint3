package com.piyush.pictprint;

import android.os.Build;
import android.support.annotation.RequiresApi;
//
//import javax.crypto.Cipher;
//import java.security.*;
//import android.util.Base64;
//import android.util.Log;
//
//import java.security.spec.*;
//public class EncryptDecrypt {
//
//    private static KeyFactory kf;
//    private static Cipher cipher;
//
//    public static String encrypt(String plainText) throws Exception {
//        if(kf==null) {
//            kf = KeyFactory.getInstance("RSA");
//            cipher = Cipher.getInstance("RSA");
//        }
//        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
//        return Base64.encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")),Base64.NO_WRAP);
//    }
//
//    public static PublicKey getPublicKey() throws Exception {
//        X509EncodedKeySpec pubKey = new X509EncodedKeySpec(Base64.decode(BuildConfig.ENCRYPTION_KEY, Base64.DEFAULT));
//        Log.d(kf.generatePublic(pubKey).toString(),"TAG");
//        return kf.generatePublic(pubKey);
//    }
//
//}


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt)
    {
        try
        {
            if(secretKey==null)
            setKey(BuildConfig.ENCRYPTION_KEY);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")),Base64.DEFAULT);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}

//import android.os.Build;
//import android.support.annotation.RequiresApi;
//
//import javax.crypto.Cipher;
//
//import java.nio.charset.StandardCharsets;
//import java.security.*;
//import java.util.Base64;
//import java.security.spec.*;
//public class EncryptDecrypt {
//    private String privateKey;
//    private String publicKey;
//    KeyFactory kf;
//    public EncryptDecrypt() throws Exception{
//        privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKlEG8zTLrCPnexs4hviKLgDBU+hj2pNkN/kT17WD+UqDWoKdybuGP8tebnhp2cs4bgpX76HyiG8ESq0BJnxyUpT2KFbCNVoGpqZazdFaOYMxVw9dXiPYLZXD1A7GZdT0vEEzRni/MQFTGwdQheMHzZorF4UwXuqER5pDEUiP8QJAgMBAAECgYB49fUv8JLjFh7zP/RtnLcgmPgIqhoFC51Ggq+9xBfj2qNvbj8sEObIJMVicsHg15AOvXJwhDWM2gsiizbmlIUVcUAxgYzJsSGMK/ysXHGKrDdX/qSwww6etGMXrlO0+sWDH05Z7onVMNTPU52KlY3A+XKs/zr1UUXughGRvltzyQJBANJwo4zMccRrg2f0ntauk5N72Rt1IkMQ5ygVmMGphljYgIHAXnGSsxaw0BZj89z9wxcBOMEMNgqbVlEU8P/SqCMCQQDN6XUqQwSut/5FMPojgPGX/BmX2NTOpi+nqE3UF1VN3+siXjQNrk584T81ur1f05D+Urk3+/TuS+bE+qn8VO/jAkAFZqKr8GtTETdUKbER7XUYKeuOitkQQ5TLMJK1Fba+/P9/VQKXg55ZDLyqdnJBz7l8E6nzOm8Aoq/6cMEmr69lAkEAwNkMXadkxEoYPy8upn/GRSTbx2ZRcLUeXrR3J9Muln/HlWmK8OE2Kyk+bRDW+d+IpVhOlgtIisHVgNiOyX+3JwJAerDDjIjjBC1X9YfB8wN8pM5N70wVZpAAQP/Vt7BVtYBkU6YJQbEaO88gxgD80QS4qV1M9vBxNX7wVaRmRQQdfw==";
//        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpRBvM0y6wj53sbOIb4ii4AwVPoY9qTZDf5E9e1g/lKg1qCncm7hj/LXm54adnLOG4KV++h8ohvBEqtASZ8clKU9ihWwjVaBqamWs3RWjmDMVcPXV4j2C2Vw9QOxmXU9LxBM0Z4vzEBUxsHUIXjB82aKxeFMF7qhEeaQxFIj/ECQIDAQAB";
//        kf = KeyFactory.getInstance("RSA");
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public String encrypt(String plainText) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
//
//        return Base64.getMimeEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public String decrypt(String cipherText) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
//        return new String(cipher.doFinal(Base64.getMimeDecoder().decode(cipherText)),StandardCharsets.UTF_8);
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public PublicKey getPublicKey() throws Exception {
//        X509EncodedKeySpec pubKey = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKey));
//
//        return kf.generatePublic(pubKey);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public PrivateKey getPrivateKey() throws Exception {
//        PKCS8EncodedKeySpec priKey = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
//        return kf.generatePrivate(priKey);
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void main(String[] args) {
//
//        try {
//            EncryptDecrypt myCrypt = new EncryptDecrypt();
//            String cipherText = myCrypt.encrypt("Kshitij Patil");
//            System.out.println(cipherText);
//
//            String plainText = myCrypt.decrypt(cipherText);
//            System.out.println(plainText);
//        }catch (Exception e) {
//            System.out.println(e);
//        }
//    }
