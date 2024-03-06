package com.example.lab1_ph31749;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;

    private EditText edtPhoneNumber;
    private EditText edtOTP;
    private Button btnGetOTP;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtOTP = findViewById(R.id.edtOTP);
        btnGetOTP = findViewById(R.id.btnGetOTP);
        btnLogin = findViewById(R.id.btnLogin);

        // Set click event for Get OTP button
        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edtPhoneNumber.getText().toString();
                getOTP(phoneNumber);
            }
        });

        // Set click event for Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOTP.getText().toString();
                verifyOTP(otp);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // Auto-fill OTP into EditText
                edtOTP.setText(credential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginOtpActivity.this, "Xác minh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                Toast.makeText(LoginOtpActivity.this, "OTP đã được gửi", Toast.LENGTH_SHORT).show();
            }
        };

        // Set click event for "Login with Email and Password" TextView
        TextView tvLoginEmailPw = findViewById(R.id.tvLoginEmailPw);
        tvLoginEmailPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginOtpActivity.this, LoginActivity.class));
            }
        });

    }

    private void getOTP(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber) // Phone number format
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String otp) {
        if (otp.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Successful login
                            Toast.makeText(LoginOtpActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            // Chuyển sang MainActivity
                            startActivity(new Intent(LoginOtpActivity.this, MainActivity.class));
                            finish(); // Đóng Activity hiện tại để ngăn người dùng quay lại màn hình OTP
                        } else {
                            // Failed login
                            Toast.makeText(LoginOtpActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
