package com.tibin.christmasfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tibin.christmasfriend.javaClass.DBHelper;

public class AdminActivity extends AppCompatActivity {

    DBHelper myDb = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button clearIsSelected = findViewById(R.id.clearIsSelected);
        clearIsSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllIsSelected();
            }
        });
    }


    private void clearAllIsSelected() {
        myDb.clearAllIsSelected();
    }

}