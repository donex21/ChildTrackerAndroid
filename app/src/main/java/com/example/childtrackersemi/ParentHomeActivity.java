package com.example.childtrackersemi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentHomeActivity extends AppCompatActivity {

    EditText et_child_search;
    Button btn_child_search, Reload;
    ListView lv_children_display;
    ArrayList<String> children;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String parentEmail = "";
    ArrayAdapter<String> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        getChildrenList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        et_child_search = findViewById(R.id.et_child_search);
        btn_child_search = findViewById(R.id.btn_child_search);
        Reload = findViewById(R.id.Reload);
        lv_children_display = findViewById(R.id.lv_children_display);
        parentEmail = getIntent().getExtras().getString("email");
        children = new ArrayList<String>();


        Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getChildrenList();
            }
        });

        lv_children_display.setClickable(true);
        lv_children_display.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object obj = lv_children_display.getItemAtPosition(position);
                String str=(String)obj;//As you are using Default String Adapter
               // Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                FirebaseFirestore  db = FirebaseFirestore.getInstance();
                db.collection("children").document(str).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot.exists()){
                                        String latitude = (String)documentSnapshot.get("lat");
                                        String longitude = (String)documentSnapshot.get("Long");
                                        String address = (String)documentSnapshot.get("address");
                                        Intent intent = new Intent(ParentHomeActivity.this, ParentMonitoringMapActivity.class);
                                        intent.putExtra("latitude", latitude);
                                        intent.putExtra("longitude", longitude);
                                        intent.putExtra("address", address);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Document not Exist", Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    Toast.makeText(getApplicationContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        btn_child_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_child_search.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Input Email", Toast.LENGTH_SHORT).show();
                }else{
                    db.collection("children").document(et_child_search.getText().toString())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot.exists()){
                                    Map<String, Object> child = new HashMap<>();
                                    child.put("email", et_child_search.getText().toString());
                                    db.collection("Parents").document(parentEmail).collection("children")
                                            .document(et_child_search.getText().toString())
                                            .set(child).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Child Successfully Add", Toast.LENGTH_SHORT).show();

                                                et_child_search.setText("");
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Child Already Exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(), "Child Email Does not Exist", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }//end of onCreate Method

    private void getChildrenList(){
        try{
            children.clear();
            //get the children from the parent
            db.collection("Parents").document(parentEmail).collection("children")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            String id = document.getId();
                            children.add(id);
                        }
                        adapter = new ArrayAdapter<String>(ParentHomeActivity.this, R.layout.act_list, children);
                        lv_children_display.setAdapter(adapter);
                    }
                }
            });
        }catch(Exception e){
            children = null;
        }
    }


}