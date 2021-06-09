package com.example.childtrackersemi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iamparent(View view) {
        Intent intent = new Intent(MainActivity.this, LoginParentActivity.class);
        startActivity(intent);
    }

    public void iamchild(View view) {
        Intent intent = new Intent(MainActivity.this, LoginChildActivity.class);
        startActivity(intent);
    }
}