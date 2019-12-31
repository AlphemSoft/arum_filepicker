package com.omensoft.arum.filepicker.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Duration {
    private long hour = 0;
    private long minutes = 0;
    private long seconds = 0;

    public Duration(long longDuration) {
        longDuration /= 1000;
        if (longDuration>0){
            seconds = longDuration % 60;
            longDuration/=60;
        }
        if (longDuration>0){
            minutes = longDuration % 60;
            longDuration /= 60;
        }
        if (longDuration>0){
            hour = longDuration;
        }
    }

    public long getHour() {
        return hour;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    @NonNull
    @Override
    public String toString() {
        String duration;
        if (hour == 0){
            duration = String.format("%02d:%02d", minutes, seconds);
        }else if(hour == 0 && minutes == 0){
            duration = String.format("%02d", minutes);
        }else {
            duration = String.format("%02d:%02d:%02d", hour, minutes, seconds);
        }
        return duration;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Duration other = null;
        if (obj instanceof Duration){
            other = (Duration) obj;
        }else {
            return false;
        }
        return other.hour == hour && other.minutes == minutes && other.seconds == seconds;
    }
}