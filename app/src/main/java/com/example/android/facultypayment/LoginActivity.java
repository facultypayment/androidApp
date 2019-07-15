package com.example.android.facultypayment;

import android.content.Intent;
/*import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    private TextView forgotPass;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Login");

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.reg_password);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);
        forgotPass = findViewById(R.id.forgot);
        //RelativeLayout layout = findViewById(LoginActivity.this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setEnabled(false);
                register.setEnabled(false);
                forgotPass.setEnabled(false);
                final String emailId = email.getText().toString();
                String pass = password.getText().toString();

                if(!TextUtils.isEmpty(emailId)&& !TextUtils.isEmpty(pass)){

                    mAuth.signInWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                                sendToMain(emailId);

                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error:"+message,Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }
                else
                    Toast.makeText(LoginActivity.this,"Error:Please enter above fields",Toast.LENGTH_LONG).show();

                buttonsEnabled();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                register.setEnabled(false);
                forgotPass.setEnabled(false);
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                register.setEnabled(false);
                forgotPass.setEnabled(false);
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                finish();
            }
        });

    }

    private void buttonsEnabled() {
        login.setEnabled(true);
        register.setEnabled(true);
        forgotPass.setEnabled(true);
    }

    private void sendToMain(String emailId) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email",emailId);
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
