package com.tibin.christmasfriend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        clearAllSelections();
        clearFriendsInstance();
    }

    private void clearAllSelections() {
        FirebaseDatabase.getInstance().getReference("names").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer length = (int) snapshot.getChildrenCount();
                for (int i = 0; i < length; i++) {
                    snapshot.child(String.valueOf(i)).child("isSelected").getRef().setValue(0);
                    snapshot.child(String.valueOf(i)).child("isSelectedBy").getRef().setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearFriendsInstance() {
        FirebaseDatabase.getInstance().getReference("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}