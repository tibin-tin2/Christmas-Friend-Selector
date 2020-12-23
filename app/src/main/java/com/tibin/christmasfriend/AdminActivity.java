package com.tibin.christmasfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.tibin.christmasfriend.javaClass.DBHelper;

public class AdminActivity extends AppCompatActivity {

    DBHelper myDb = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button clearIsSelected = findViewById(R.id.clear_is_selected);
        Button clearIsSelectedBy = findViewById(R.id.clear_is_selected_by);
        Button clearAll = findViewById(R.id.clear_all);

        EditText name = findViewById(R.id.name);
        Button add = findViewById(R.id.add);
        Button delete = findViewById(R.id.delete);
        Button clearSelected = findViewById(R.id.clear_is_selected_specific);
        Button clearSelectedBy = findViewById(R.id.clear_is_selected_by_specific);
        Button friendsList = findViewById(R.id.showFriendTable);

        clearIsSelected.setOnClickListener(v -> clearAllIsSelected());
        clearIsSelectedBy.setOnClickListener(v -> clearAllIsSelectedBy());
        clearAll.setOnClickListener(v -> {
            clearAllIsSelected();
            clearAllIsSelectedBy();
            clearFriendsTable();
        });

        add.setOnClickListener(v -> addNew(name.getText().toString()));
        delete.setOnClickListener(v -> deleteByName(name.getText().toString()));
        clearSelected.setOnClickListener(v -> clearIsSelected(name.getText().toString()));
        clearSelectedBy.setOnClickListener(v -> clearIsSelectedBy(name.getText().toString()));
        friendsList.setOnClickListener(v -> showFriendsTableActivity());
    }

    private void showFriendsTableActivity() {
        Intent intent = new Intent(this, FriendsViewTable.class);
        startActivity(intent);
    }

    private void clearAllIsSelected() {
        myDb.clearAllIsSelected();
    }

    private void clearAllIsSelectedBy() {
        myDb.clearAllIsSelectedBy();
    }

    private void addNew(String name) {
        myDb.addNew(name);
    }

    private void deleteByName(String name) {
        myDb.deleteName(name);
    }

    private void clearIsSelected(String name) {
        myDb.clearIsSelected(name);
    }

    private void clearIsSelectedBy(String name) {
        myDb.clearIsSelectedBy(name);
    }

    private void clearFriendsTable() {
        myDb.clearFriendsTable();
    }

}