package com.tibin.christmasfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tibin.christmasfriend.javaClass.DBHelper;
import com.tibin.christmasfriend.javaClass.FriendsList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initializeDb();

        Button start = findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNameSelectorActivity();
            }
        });

        start.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getAdminActivity();
                return true;
            }
        });

    }

    private void getNameSelectorActivity() {
        Intent intent = new Intent(this, nameSelector.class);
        startActivity(intent);
    }

    private void getAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}