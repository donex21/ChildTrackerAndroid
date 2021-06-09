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

public class ChildSignupActivity extends AppCompatActivity {
    EditText et_child_email, et_child_pwd, et_child_name;
    Button btn_child_signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_signup);

        et_child_email = findViewById(R.id.et_child_email);
        et_child_pwd = findViewById(R.id.et_child_pwd);
        et_child_name = findViewById(R.id.et_child_name);
        btn_child_signup = findViewById(R.id.btn_child_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btn_child_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_child_email.getText().toString().equals("") || et_child_name.getText().toString().equals("") || et_child_pwd.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Complete Details",Toast.LENGTH_SHORT).show();
                }else{
                   mAuth.createUserWithEmailAndPassword(et_child_email.getText().toString(), et_child_pwd.getText().toString())
                           .addOnCompleteListener(ChildSignupActivity.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> child = new HashMap<>();
                                        child.put("email", et_child_email.getText().toString());
                                        child.put("password", et_child_pwd.getText().toString());
                                        child.put("name", et_child_name.getText().toString());
                                        child.put("lat", "NA");
                                        child.put("Long", "NA");
                                        db.collection("children").document(et_child_email.getText().toString())
                                                .set(child)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(),"Successfully Saved",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ChildSignupActivity.this, ChildHomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
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

    public void gobackChildActivity(View view) {
        Intent intent = new Intent(ChildSignupActivity.this, LoginChildActivity.class);
        startActivity(intent);
        finish();
    }
}