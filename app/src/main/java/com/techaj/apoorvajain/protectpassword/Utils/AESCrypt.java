package com.techaj.apoorvajain.protectpassword.Utils;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {


    private static AESCrypt INSTANCE;
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final String KEY_ALIAS = "Key@@$$123AS#@$o";
    private static final String FIXED_IV = "@#CDG^&(kl45";
    private static String IV;
    private static KeyStore keyStore;
    private static boolean isFirstLogin;

    private AESCrypt() {
    }

    public AESCrypt(String IV) throws KeyStoreException, InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException, IOException, CertificateException {
        keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            .build());
            keyGenerator.generateKey();
        }
        this.IV = IV;

    }

    public static AESCrypt getInstance() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException {
        //Double check locking pattern

        if (INSTANCE == null) { //Check for the first time

            synchronized (AESCrypt.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (INSTANCE == null) {
                    INSTANCE = new AESCrypt();

                    keyStore = KeyStore.getInstance(AndroidKeyStore);
                    keyStore.load(null);
                    isFirstLogin=!keyStore.containsAlias(KEY_ALIAS);


                    if (!keyStore.containsAlias(KEY_ALIAS)) {
                        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
                        keyGenerator.init(
                                new KeyGenParameterSpec.Builder(KEY_ALIAS,
                                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                                        .setRandomizedEncryptionRequired(false)
                                        .build());
                        keyGenerator.generateKey();
                    }
                }
            }
        }

        return (INSTANCE);
    }

    public boolean firstLogIn() throws KeyStoreException {

        return isFirstLogin;

    }

    public String encrypt(String input, Context context) throws Exception {


        Cipher c = Cipher.getInstance(AES_MODE);
        Log.e("AJ", keyStore.getKey(KEY_ALIAS, null) + "");

        c.init(Cipher.ENCRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, IV.getBytes()));
        byte[] encodedBytes = c.doFinal(input.getBytes());
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    public String decrypt(String encrypted, Context context) throws Exception {


        Cipher c = Cipher.getInstance(AES_MODE);

        c.init(Cipher.DECRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, IV.getBytes()));

        byte[] decoded = Base64.decode(encrypted, Base64.DEFAULT);

        byte[] decodedBytes = c.doFinal(decoded);

        return new String(decodedBytes, "UTF8");
    }

    private java.security.Key getSecretKey(Context context) throws Exception {

        return keyStore.getKey(KEY_ALIAS, null);

    }

    public static String getIV() {
        Log.e("AJ",IV.length()+IV);
        return IV;
    }

    public static void setIV(String IV) {
        Log.e("AJ",IV);
        byte[] a = IV.getBytes(StandardCharsets.UTF_8);
        byte[] b = FIXED_IV.getBytes(StandardCharsets.UTF_8);
        byte[] c = new byte[12];
        for( int i = 0; i < a.length; i++ )
            c[i] = (byte) (a[i] ^ b[i]);
        Log.e("AJ",a.length+"");

        System.arraycopy(b, a.length, c, a.length, 12 - a.length);
        Log.e("AJ",c.length+"");
        AESCrypt.IV = new String(c,StandardCharsets.UTF_8);
        Log.e("AJ",AESCrypt.IV + AESCrypt.IV.length());
       // Log.e("AJ","IV"+ AESCrypt.IV);
    }

}

