package com.tibin.christmasfriend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.start);

        if (!isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "No Network Connected", Toast.LENGTH_LONG).show();
            showAlert();

        }

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
        Intent intent = new Intent(this, NameSelector.class);
        startActivity(intent);
    }

    private void getAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    private Boolean isNetworkAvailable(Context context) {
        boolean value = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("No Network Connected. Please Connect to a Working Network");

        builder.setPositiveButton("I Turned On My Network", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isNetworkAvailable(getBaseContext())) {
                    Toast.makeText(getApplicationContext(), "No Network Connected", Toast.LENGTH_LONG).show();
                    showAlert();

                }
            }
        });
        builder.show();
    }
}