package com.wubydax.romcontrol.v2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;


public class Splash extends AppCompatActivity {

    //------------------------------------------------------------------------------------------------------
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmXGWKtA2coc5fAyvCAIFgybulNChkXz/GR6O1aFMlzwPkU7SR/p12knvLrNALgUVUp+V2iaxTfBDgPWcaBFPBb07TWcS17Eo7qZpdMh8Wm/8Eae2LIlLdV5vA2HPBpV7Yn1HseE8vOHu8j+Wvf5P/LUDgTLsxibskkur7q/R7Yq0n9I1vBcdmpEOPYNGfiz3oOHep7VL6K93iWsnLv5enq6MC1qWWXK5eyeJm+p6wrFbBtcGzUEx7ofJc3yn1hiV5ocW1s9ow4+awiOPYGqRN1JH13+eDixAW5sJa0Z16MsgI6cKeIDWY4xHT6NilM3Y1tDqvrQ4lM5ZSU+Sb2lG3QIDAQAB";
    // Generate your own 20 random bytes, and put them here.
    private static final byte[] SALT = new byte[] {
            -21, 15, 30, -108, -123, -52, 74, -34, 51, 88, -75, -95, 71, -117, -36, -103, -11, 38, -14,
            89
    };
    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;
    // A handler on the UI thread.
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // License ->
        mHandler = new Handler();
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Library calls this when it's done.
        mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        // Construct the LicenseChecker with a policy.
        mChecker = new LicenseChecker(
                this, new ServerManagedPolicy(this,
                new AESObfuscator(SALT, getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY);
        mChecker.checkAccess(mLicenseCheckerCallback);

    }

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            Log.w("MinoLIC", "Allow");
            Intent i = new Intent(Splash.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        public void dontAllow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            Log.d("MinoLIC", "Don't Allow for reason: "+ policyReason);
            showDialog(0);
        }

        public void applicationError(int errorCode) {

            Log.w("MinoLIC", "Application error code: "+errorCode);

            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
    // We have only one dialog.
        return new AlertDialog.Builder(this)
                .setTitle(R.string.LTitle)
                .setCancelable(false)
                .setMessage(
                        R.string.LMessage)
                .setPositiveButton(R.string.LPButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent marketIntent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + getPackageName()));
                                startActivity(marketIntent);
                                finish();
                            }
                        })
                .setNegativeButton(R.string.LNButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                            }
                        }).create();
    }
    public void toast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChecker.onDestroy();
    }

}
