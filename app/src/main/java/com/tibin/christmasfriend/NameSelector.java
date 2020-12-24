package com.tibin.christmasfriend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NameSelector extends AppCompatActivity {

    LinearLayout linearLayout;
    List<Integer> buttonId = new ArrayList<>();
    ProgressDialog dialog;

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_name_selector);

        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("names").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (Integer id : buttonId) {
                    removeButtonsById(id);
                }
                getValues(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void removeButtonsById(Integer id) {
        linearLayout.removeView(findViewById(id));
    }

    private void getValues(DataSnapshot snapshot) {
        int length = (int) snapshot.getChildrenCount();
        for (int i = 0; i < length; i++) {
            String name = (String) snapshot.child(String.valueOf(i)).child("name").getValue();
            Boolean isSelected = (snapshot.child(String.valueOf(i)).child("isSelected").getValue().toString().equals(String.valueOf(0))) ? Boolean.FALSE : Boolean.TRUE;
            generateButtons(i, name, isSelected);
            dialog.dismiss();
        }
    }

    private void generateButtons(Integer id, String name, Boolean isSelected) {
        Button button = new Button(this);
        button.setText(name);
        button.setId(id);
        button.setTag(name);
        button.setEnabled(!isSelected);
        buttonId.add(button.getId());
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.addView(button);
        setButtonClickEvent(button);
    }

    private void setButtonClickEvent(Button button) {
        button.setOnClickListener(v -> {
            getSpinActivity(button.getText().toString());
        });
    }

    private void getSpinActivity(String name) {
        Intent intent = new Intent(this, SpinActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}