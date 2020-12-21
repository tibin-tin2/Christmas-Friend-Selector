package com.tibin.christmasfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.tibin.christmasfriend.javaClass.DBHelper;
import com.tibin.christmasfriend.javaClass.FriendsList;

import java.util.List;

public class SpinActivity extends AppCompatActivity {

    DBHelper myDb = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        TextView nameText = findViewById(R.id.viewName);
        List<String> names = getNames();
        nameText.setText(name);
        enableIsSelected(name);
    }

    private List<String> getNames() {
        FriendsList friendsList = new FriendsList();
        return friendsList.getNames();
    }

    private void enableIsSelected(String name) {
        myDb.updateIsSelectedInNames(name, Boolean.TRUE);
    }
}