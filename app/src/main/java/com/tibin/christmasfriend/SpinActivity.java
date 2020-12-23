package com.tibin.christmasfriend;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.tibin.christmasfriend.javaClass.DBHelper;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpinActivity extends AppCompatActivity {

    DBHelper myDb = new DBHelper(this);
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    String name;
    String friend;
    static int shakeCount = 0;
    Boolean isShakeDone = Boolean.FALSE;

    final Handler handler = new Handler();
    final int delay = 1000;
    final int threshold = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        name = getNameFromIntent();

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

    private List<String> getNames() {
        return myDb.getAllNamesWithIsSelectedByFalse();
    }

    private void enableIsSelected(String name) {
        myDb.updateIsSelectedInNames(name, Boolean.TRUE);
    }

    private void enableIsSelectedBy(String name) {
        myDb.updateIsSelectedByInNames(name, Boolean.TRUE);
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
        List<String> names = getNames();
        int index = new Random().nextInt(names.size());
        if (!name.equalsIgnoreCase(names.get(index))) {
            friend = names.get(index);
            setFriendText();
            enableIsSelected(name);
            enableIsSelectedBy(friend);
            addToFriendsList(name, friend);
        } else {
            generateFriend();
        }
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
                if (shakeCount > 0 && !isShakeDone) {
                    setIsShakeDone(Boolean.TRUE);
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

    private void addToFriendsList(String name, String friend) {
        myDb.insertFriendTable(name, friend);
    }

    private void postActivity() {
        setIsShakeDone(Boolean.FALSE);
        mAccel = 0;

        disableProgressBar();

        TextView shake = findViewById(R.id.shake);
        TextView friend = findViewById(R.id.friend_name);
        TextView hurray = findViewById(R.id.hurray);
        TextView congrats = findViewById(R.id.congrats);
        TextView isYourFiend = findViewById(R.id.is_your_friend);

        shake.setVisibility(View.VISIBLE);
        friend.setVisibility(View.INVISIBLE);
        hurray.setVisibility(View.INVISIBLE);
        congrats.setVisibility(View.INVISIBLE);
        isYourFiend.setVisibility(View.INVISIBLE);
    }
}