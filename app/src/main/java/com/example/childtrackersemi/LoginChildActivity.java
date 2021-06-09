package com.example.childtrackersemi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginChildActivity extends AppCompatActivity {
    EditText et_child_login_email, et_child_login_pwd;
    Button btn_child_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_child);

        et_child_login_email = findViewById(R.id.et_child_login_email);
        et_child_login_pwd = findViewById(R.id.et_child_login_pwd);
        btn_child_login = findViewById(R.id.btn_child_login);

        mAuth = FirebaseAuth.getInstance();
        btn_child_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_child_login_email.getText().toString().equals("") || et_child_login_pwd.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please Complete Details", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(et_child_login_email.getText().toString(), et_child_login_pwd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(LoginChildActivity.this, ChildHomeActivity.class);
                                        intent.putExtra("email", et_child_login_email.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Invalid Email/Password Try Again!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    public void signupChild(View view) {
        Intent intent = new Intent(LoginChildActivity.this, ChildSignupActivity.class);
        startActivity(intent);
        finish();
    }
}