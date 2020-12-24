package com.tibin.christmasfriend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpinActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    ProgressDialog dialog;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    String name;
    String friend;

    static int shakeCount = 0;
    Boolean isShakeDone = Boolean.FALSE;
    Boolean isGetFriend = Boolean.FALSE;

    final Handler handler = new Handler();
    final int delay = 1000;
    final int threshold = 12;

    List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        name = getNameFromIntent();

        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

        checkShake();
        showText(name);
        initSensor();
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > threshold) {
                Toast.makeText(getApplicationContext(), "Keep Shaking your phone to get your friend", Toast.LENGTH_SHORT).show();
                shakeActivity();
                setShakeCount();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        setIsShakeDone(Boolean.FALSE);
        mAccel = 0f;
        super.onResume();
        getFriends();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private String getNameFromIntent() {
        Intent intent = getIntent();
        return intent.getStringExtra("name");
    }

    private void showText(String name) {
        TextView nameText = findViewById(R.id.viewName);
        nameText.setText("Hi, " + name);
    }

    private void shakeActivity() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(100);
        }
    }


    private void generateFriend() {
        int index = new Random().nextInt(names.size());
        if (!name.equalsIgnoreCase(names.get(index))) {
            friend = names.get(index);
            setFriendText();
            updateIsSelectedByValue(friend);
            updateIsSelectedValue(name);
            updateFriendsTable(name, friend);
        } else {
            generateFriend();
        }
    }

    private void getFriends() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("names");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getValues(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getValues(DataSnapshot snapshot) {
        names = new ArrayList<>();
        int length = (int) snapshot.getChildrenCount();
        for (int i = 0; i < length; i++) {
            String name = (String) snapshot.child(String.valueOf(i)).child("name").getValue();
            Boolean isSelectedBy = (snapshot.child(String.valueOf(i)).child("isSelectedBy").getValue().toString().equals(String.valueOf(0))) ? Boolean.FALSE : Boolean.TRUE;
            if (!isSelectedBy) {
                names.add(name);
            }
        }
        setGetFriend(Boolean.TRUE);
        dialog.dismiss();
    }

    private void setFriendText() {
        TextView friendText = findViewById(R.id.friend_name);
        friendText.setText(friend);
    }

    private void setShakeCount() {
        shakeCount++;
    }

    private void checkShake() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (shakeCount > 0 && !getIsShakeDone() && getGetFriend()) {
                    setIsShakeDone(Boolean.TRUE);
                    setGetFriend(Boolean.FALSE);
                    shakeCount = 0;
                    generateFriend();
                    setVisibility();
                } else {
                    disableProgressBar();
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void setIsShakeDone(Boolean isShakeDone) {
        this.isShakeDone = isShakeDone;
    }

    private Boolean getIsShakeDone() {
        return this.isShakeDone;
    }

    public void setGetFriend(Boolean getFriend) {
        isGetFriend = getFriend;
    }

    public Boolean getGetFriend() {
        return isGetFriend;
    }

    private void setVisibility() {
        TextView shake = findViewById(R.id.shake);
        TextView friend = findViewById(R.id.friend_name);
        TextView hurray = findViewById(R.id.hurray);
        TextView congrats = findViewById(R.id.congrats);
        TextView isYourFiend = findViewById(R.id.is_your_friend);

        shake.setVisibility(View.INVISIBLE);
        friend.setVisibility(View.VISIBLE);
        hurray.setVisibility(View.VISIBLE);
        congrats.setVisibility(View.VISIBLE);
        isYourFiend.setVisibility(View.VISIBLE);

        disableProgressBar();
    }

    private void disableProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void updateIsSelectedByValue(String name) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("names");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer length = (int) snapshot.getChildrenCount();
                for (int i = 0; i < length; i++) {
                    if ((snapshot.child(String.valueOf(i)).child("name").getValue()).equals(name)) {
                        snapshot.child(String.valueOf(i)).child("isSelectedBy").getRef().setValue(1);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateIsSelectedValue(String name) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("names");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer length = (int) snapshot.getChildrenCount();
                for (int i = 0; i < length; i++) {
                    if ((snapshot.child(String.valueOf(i)).child("name").getValue()).equals(name)) {
                        snapshot.child(String.valueOf(i)).child("isSelected").getRef().setValue(1);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateFriendsTable(String name, String friend) {
        FirebaseDatabase.getInstance().getReference("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child(name).getRef().setValue(friend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}