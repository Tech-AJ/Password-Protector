package com.techaj.apoorvajain.protectpassword;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import static android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_CANCELED;
import static android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT;
import static android.support.v4.content.ContextCompat.startActivity;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private TextView tv;
    private Intent intent;
    private Context context;


    FingerprintHandler(TextView tv, Intent intent, Context context) {

        this.tv = tv;
        this.context = context;
        this.intent = intent;


    }

    @Override

    public void onAuthenticationError(int errorCode, CharSequence errString) {

        super.onAuthenticationError(errorCode, errString);


        Log.e("AJ", "Auth error" + errorCode);
        if (errorCode == FINGERPRINT_ERROR_LOCKOUT) {
            CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv.setText("App locked due to 5 failed attempts. Please try after " + millisUntilFinished / 1000 + " sec.");
                }

                @Override
                public void onFinish() {
                    tv.setText("The operation was canceled due to security reasons !!! Try closing app and restarting it ");
                }
            }.start();


        } else if (errorCode == FINGERPRINT_ERROR_CANCELED)
            tv.setText("The operation was canceled due to security reasons !!! This might happen if the device was recently locked and unlocked .Try closing app and restarting it ");

    }

    @Override

    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        super.onAuthenticationHelp(helpCode, helpString);
        Log.e("AJ", "Auth help");


    }

    @Override

    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        super.onAuthenticationSucceeded(result);

        // tv.setText("Fingerprint Matched successfully.");
        Log.e("AJ", "Auth ok");
        startActivity(context, intent, null);
        ((MainActivity)context).finish();


    }

    @Override

    public void onAuthenticationFailed() {

        super.onAuthenticationFailed();
        tv.setText("Figerprint not identified !!!");
        Log.e("AJ", "Auth failed");


    }

    public void doAuth(FingerprintManager manager,

                       FingerprintManager.CryptoObject obj) {

        CancellationSignal signal = new CancellationSignal();

        try {

            manager.authenticate(obj, signal, 0, this, null);
            tv.setTextColor(tv.getContext().getResources().getColor(android.R.color.white));
        } catch (SecurityException sce) {
            sce.printStackTrace();
        }

    }

}
