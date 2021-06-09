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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ParentSignupActivity extends AppCompatActivity {

    EditText et_parent_name, et_parent_pwd, et_parent_email;
    Button btn_parent_signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup);
        et_parent_name = findViewById(R.id.et_parent_name);
        et_parent_pwd = findViewById(R.id.et_parent_pwd);
        et_parent_email = findViewById(R.id.et_parent_email);
        btn_parent_signup = findViewById(R.id.btn_parent_signup);
        mAuth = FirebaseAuth.getInstance();

        btn_parent_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_parent_email.getText().toString().equals("") || et_parent_pwd.getText().toString().equals("") || et_parent_name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please Complete Details",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(et_parent_email.getText().toString(), et_parent_pwd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> parent = new HashMap<>();
                                        parent.put("email", et_parent_email.getText().toString());
                                        parent.put("password", et_parent_pwd.getText().toString());
                                        parent.put("name", et_parent_name.getText().toString());
                                        db.collection("Parents").document(et_parent_email.getText().toString())
                                                .set(parent)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(),"Successfully Saved",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ParentSignupActivity.this, ParentHomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(),"Error/Invalid",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Provide Another Email and Password",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void gobackParentLogin(View view) {
        Intent intent = new Intent(ParentSignupActivity.this, LoginParentActivity.class);
        startActivity(intent);
        finish();
    }
}