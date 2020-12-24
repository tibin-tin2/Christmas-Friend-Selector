package com.tibin.christmasfriend;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    ProgressDialog dialog;
    String passwordInput;
    final static String PASSWORD = "8989";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getAccess();
        clearAllSelections();
        clearFriendsInstance();
    }

    private void clearAllSelections() {
        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
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
        dialog.dismiss();
    }

    private void clearFriendsInstance() {
        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        FirebaseDatabase.getInstance().getReference("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog.dismiss();
    }

    private void getAccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter passphrase to access admin console");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.requestFocus();
        builder.setView(input);

        builder.setPositiveButton("Check", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passwordInput = input.getText().toString();
                if (passwordInput == "" | passwordInput.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter a passphrase", Toast.LENGTH_SHORT).show();
                }
                if (passwordInput.equalsIgnoreCase(PASSWORD)) {
                    findViewById(R.id.clearAll).setEnabled(Boolean.TRUE);
                    dialog.cancel();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid passphrase", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}