package com.example.studytimerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private EditText taskType;
    private String types, content;
    private Button bt_into;
    private Chronometer timer;
    private ImageView stop;
    private boolean isTimerStart = false;
    private boolean isStop = false;
    private long mRecordTime;
    private RelativeLayout relativeLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        types = sharedPreferences.getString("types", " ");
        content = sharedPreferences.getString("time", " ");
        taskType = findViewById(R.id.taskType);
        result = findViewById(R.id.result);

        result.setText("You spent " + content + " on " + types + " last time");
        timer = (Chronometer) findViewById(R.id.chronometer);
        stop = (ImageView) findViewById(R.id.stop);
    }

    public void onStart(View view) {
        if(isTimerStart){
            Toast.makeText(MainActivity.this, "time starting", Toast.LENGTH_LONG).show();
            return;
        }
        isTimerStart = true;
        timer.setBase(SystemClock.elapsedRealtime());
        int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0" + String.valueOf(hour) + ": %s");
        timer.start();
    }

    public void onStop(View view) {
        if (!isTimerStart) {
            Toast.makeText(MainActivity.this, "Please click start button", Toast.LENGTH_LONG).show();
            return;
        }
        if (isStop){ // Paused, click Resume.
            isStop = false;
        timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
        timer.start();
        } else { // Paused.
            mRecordTime = SystemClock.elapsedRealtime();
            isStop = true;
            timer.stop();
        }
    }

    public void onRestart(View view) {
        types = taskType.getText().toString().trim();
        editor.putString("types", types);
        editor.putString("time", timer.getText().toString().trim());
        editor.commit();
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime()); //Timer reset.
        isTimerStart = false;
        isStop = false;
        mRecordTime = 0;
    }
}