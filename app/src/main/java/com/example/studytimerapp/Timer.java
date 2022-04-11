package com.example.studytimerapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Timer extends Chronometer {
    private long time;
    private long nextTime;
    private OnTimeCompleteListener clistener;
    private SimpleDateFormat format;

    public Timer(Context context, AttributeSet attrs) {
        super(context, attrs);
        format = new SimpleDateFormat("mm:ss");
        this.setOnChronometerTickListener(listener);
    }

    public void setTimeFormat(String pattern) {
        format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
    }

    public void setOnTimeCompleteListener(OnTimeCompleteListener l) {
        clistener = l;
    }

    OnChronometerTickListener listener = new OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {

            if (nextTime <= 0) {
                if (nextTime == 0) {
                    Timer.this.stop();
                    if (null != clistener) {
                        clistener.onTimeComplete();
                    }
                }
                nextTime = 0;
                updateTimeNext();
                return;
            }
            nextTime--;
            updateTimeNext();
        }
    };

    public void initTime(long _time_s) {
        time = nextTime = _time_s;
        updateTimeNext();
    }

    public void updateTimeNext() {

        this.setText(format.format(new Date(nextTime * 1000)));
    }

    public void reStart(long _time_s) {
        if (_time_s == -1) {
            nextTime = time;
        } else {
            time = nextTime = _time_s;
        }
        this.start();
    }

    public void reStart() {
        reStart(-1);
    }

    public void onResume() {
        this.start();
    }

    public void onPause() {
        this.stop();
    }

    public interface OnTimeCompleteListener {
        void onTimeComplete();
    }
}

