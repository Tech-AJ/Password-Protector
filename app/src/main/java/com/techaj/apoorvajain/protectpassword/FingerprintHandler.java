package com.techaj.apoorvajain.protectpassword;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import static android.support.v4.content.ContextCompat.startActivity;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private TextView tv;
    private Intent intent;
    private Context context;
    private int count;

    public FingerprintHandler(TextView tv,Intent intent,Context context) {

        this.tv = tv;
       this.context=context;
        this.intent=intent;
        count=0;

    }

    @Override

    public void onAuthenticationError(int errorCode, CharSequence errString) {

        super.onAuthenticationError(errorCode, errString);

        tv.setText("Auth error");
        Log.e("AJ","Auth error");

    }

    @Override

    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        super.onAuthenticationHelp(helpCode, helpString);
        Log.e("AJ","Auth help");

    }

    @Override

    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        super.onAuthenticationSucceeded(result);

        tv.setText("auth ok");
        Log.e("AJ","Auth ok");
        startActivity(context,intent,null);


      //  tv.setTextColor(tv.getContext().getResources().getColor(android.R.color.holo_green_light));

    }

    @Override

    public void onAuthenticationFailed() {

        super.onAuthenticationFailed();
        tv.setText("auth failed");
        Log.e("AJ","Auth failed");
        count++;
        if(count>=3){
            Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
            }else{
                //deprecated in API 26
                v.vibrate(3000);
            }


        }

    }

    public void doAuth(FingerprintManager manager,

                       FingerprintManager.CryptoObject obj) {

        CancellationSignal signal = new CancellationSignal();

        try {

            manager.authenticate(obj, signal, 0, this, null);
            tv.setTextColor(tv.getContext().getResources().getColor(android.R.color.holo_green_light));
        }

        catch(SecurityException sce) {}

    }

}
