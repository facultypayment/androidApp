package com.example.android.facultypayment;

import android.content.Intent;
/*mport android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText confirm_pass;
    private Button register;
    private FirebaseAuth mAuth;
    private TextView login;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Register");
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.reg_email_id);
        name = findViewById(R.id.user_name);
        password = findViewById(R.id.reg_password);
        register = findViewById(R.id.register_btn);
        confirm_pass = findViewById(R.id.confirm_pass);
        login = findViewById(R.id.login_btn);
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                register.setEnabled(false);
                login.setEnabled(false);
                final String emailId = email.getText().toString();
                final String username = name.getText().toString();
                String pass = password.getText().toString();
                String confirm = confirm_pass.getText().toString();
                if(!TextUtils.isEmpty(emailId) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm) && !TextUtils.isEmpty(username)) {
                    if (pass.equals(confirm)) {

                        mAuth.createUserWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Map<String, String> map = new HashMap<>();
                                                map.put("email",emailId);
                                                map.put("name",username);
                                                db.collection("users").document(mAuth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "registered Successfully. Please verify your email address", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else
                                                            Toast.makeText(RegisterActivity.this, "unable to register", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }

                                            else {

                                                Toast.makeText(RegisterActivity.this,
                                                        task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else

                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"Error:"+message,Toast.LENGTH_LONG).show();
                                }
                            }

                        });
                    } else
                        Toast.makeText(RegisterActivity.this, "entered passwords do not match", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(RegisterActivity.this,"please complete above fields",Toast.LENGTH_LONG).show();
                register.setEnabled(true);
                login.setEnabled(true);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register.setEnabled(false);
                login.setEnabled(false);
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
