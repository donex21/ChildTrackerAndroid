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

public class LoginParentActivity extends AppCompatActivity {

    EditText et_parent_login_email, et_parent_login_pwd;
    Button btn_parent_login;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_parent);

        et_parent_login_email = findViewById(R.id.et_parent_login_email);
        et_parent_login_pwd = findViewById(R.id.et_parent_login_pwd);
        btn_parent_login = findViewById(R.id.btn_parent_login);
        mAuth = FirebaseAuth.getInstance();

        btn_parent_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_parent_login_email.getText().toString().equals("") || et_parent_login_pwd.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please Complete Details", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(et_parent_login_email.getText().toString(), et_parent_login_pwd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(LoginParentActivity.this, ParentHomeActivity.class);
                                        intent.putExtra("email", et_parent_login_email.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Invalid Email/Password. Please try again!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void signupParent(View view) {
        Intent intent = new Intent(LoginParentActivity.this, ParentSignupActivity.class);
        startActivity(intent);
        finish();
    }

}