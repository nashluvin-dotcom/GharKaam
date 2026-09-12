package com.gharkaam.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends Activity {

    private FirebaseAuth auth;
    private EditText phoneInput;
    private EditText otpInput;
    private Button sendOtp;
    private Button verifyOtp;

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(35), dp(24), dp(24));
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.rgb(248, 247, 252));

        TextView title = text("GharKaam", 30, Color.rgb(108, 77, 255), true);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(55)));

        TextView subtitle = text(
                "Create account / Login",
                18,
                Color.rgb(45, 45, 55),
                true
        );
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle, new LinearLayout.LayoutParams(-1, dp(45)));

        TextView info = text(
                "Enter your mobile number to receive an OTP.",
                14,
                Color.GRAY,
                false
        );
        info.setGravity(Gravity.CENTER);
        root.addView(info, new LinearLayout.LayoutParams(-1, dp(45)));

        phoneInput = new EditText(this);
        phoneInput.setHint("Mobile number");
        phoneInput.setInputType(2);
        phoneInput.setText("+91 ");
        root.addView(phoneInput,
                new LinearLayout.LayoutParams(-1, dp(55)));

        sendOtp = new Button(this);
        sendOtp.setText("Send OTP");
        root.addView(sendOtp,
                new LinearLayout.LayoutParams(-1, dp(55)));

        otpInput = new EditText(this);
        otpInput.setHint("Enter 6-digit OTP");
        otpInput.setInputType(2);
        otpInput.setVisibility(View.GONE);
        root.addView(otpInput,
                new LinearLayout.LayoutParams(-1, dp(55)));

        verifyOtp = new Button(this);
        verifyOtp.setText("Verify OTP");
        verifyOtp.setVisibility(View.GONE);
        root.addView(verifyOtp,
                new LinearLayout.LayoutParams(-1, dp(55)));

        sendOtp.setOnClickListener(v -> sendCode());

        verifyOtp.setOnClickListener(v -> verifyCode());

        setContentView(root);
    }

    private void sendCode() {

        String phone = phoneInput.getText().toString().trim();

        if (!phone.startsWith("+91")) {
            phone = "+91" + phone;
        }

        if (phone.replace("+91", "").trim().length() != 10) {
            Toast.makeText(
                    this,
                    "Enter a valid 10-digit mobile number.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        sendOtp.setEnabled(false);
        sendOtp.setText("Sending OTP...");

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(
                                    PhoneAuthCredential credential) {

                                signInWithCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(
                                    FirebaseException e) {

                                sendOtp.setEnabled(true);
                                sendOtp.setText("Send OTP");

                                Toast.makeText(
                                        PhoneAuthActivity.this,
                                        "OTP error: " + e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            @Override
                            public void onCodeSent(
                                    String id,
                                    PhoneAuthProvider.ForceResendingToken token) {

                                verificationId = id;
                                resendToken = token;

                                sendOtp.setVisibility(View.GONE);
                                otpInput.setVisibility(View.VISIBLE);
                                verifyOtp.setVisibility(View.VISIBLE);

                                Toast.makeText(
                                        PhoneAuthActivity.this,
                                        "OTP sent successfully.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode() {

        String code = otpInput.getText().toString().trim();

        if (code.length() != 6) {
            Toast.makeText(
                    this,
                    "Enter the 6-digit OTP.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (verificationId == null) {
            Toast.makeText(
                    this,
                    "Please request OTP first.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(
                        verificationId,
                        code
                );

        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        verifyOtp.setEnabled(false);
        verifyOtp.setText("Verifying...");

        auth.signInWithCredential(credential)
                .addOnSuccessListener(result -> {

                    Toast.makeText(
                            this,
                            "Account verified successfully!",
                            Toast.LENGTH_LONG
                    ).show();

                    finish();

                })
                .addOnFailureListener(e -> {

                    verifyOtp.setEnabled(true);
                    verifyOtp.setText("Verify OTP");

                    Toast.makeText(
                            this,
                            "Verification failed: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}
