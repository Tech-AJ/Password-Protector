package com.techaj.apoorvajain.protectpassword.Utils;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final String KEY_ALIAS = "Key@@$$123AS#@$%";
    private static final String FIXED_IV = "#$%12sadew@*";
    KeyStore keyStore;

    public AESCrypt() throws KeyStoreException, InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException, IOException, CertificateException {
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

    }

    public String encrypt(String input, Context context) throws Exception {


        Cipher c = Cipher.getInstance(AES_MODE);
        Log.e("AJ",keyStore.getKey(KEY_ALIAS, null)+"");

        c.init(Cipher.ENCRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, FIXED_IV.getBytes()));
        byte[] encodedBytes = c.doFinal(input.getBytes());
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    public String decrypt(String encrypted, Context context) throws Exception {



        Cipher c = Cipher.getInstance(AES_MODE);

        c.init(Cipher.DECRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, FIXED_IV.getBytes()));

        byte[] decoded = Base64.decode(encrypted,Base64.DEFAULT);

        byte[] decodedBytes = c.doFinal(decoded);

        return new String(decodedBytes, "UTF8");
    }

    private java.security.Key getSecretKey(Context context) throws Exception {

        return keyStore.getKey(KEY_ALIAS, null);

    }

}

