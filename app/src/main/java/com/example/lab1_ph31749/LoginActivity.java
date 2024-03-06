package com.example.lab1_ph31749;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtEmail;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        TextView tvForgotPw = findViewById(R.id.tvForgotPw);
        tvForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

        // Trong onCreate của LoginActivity.java
        TextView tvLoginWithPhone = findViewById(R.id.tvLoginWithPhone);
        tvLoginWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginOtpActivity.class));
            }
        });

    }

    private void signIn() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập email và password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void forgotPassword() {
        String email = edtEmail.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email của bạn.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Đã gửi email đặt lại mật khẩu.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Không gửi được email đặt lại mật khẩu.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
