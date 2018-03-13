package com.example.dpbird.dpweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;


import com.example.dpbird.dpweather.gson.Weather;
import com.example.dpbird.dpweather.util.HttpUtils;
import com.example.dpbird.dpweather.util.JsonUtils;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updatePic();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 60 * 60 * 1000 * 1;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i  = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCotent = preferences.getString("weather", null);
        if (weatherCotent != null) {
            final Weather weather = JsonUtils.handleWeatherResponce(weatherCotent);
            String location = weather.basic.cityName;
            String address = "https://free-api.heweather.com/s6/weather?location=" + location + "&key=98ac3205a50948ffb0622d91d693a94d";
            HttpUtils.sendOkhttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resopnseText = response.body().string();
                    Weather weather = JsonUtils.handleWeatherResponce(resopnseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", resopnseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void updatePic() {
        String address = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responceText = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bcg_pic", responceText);
                editor.apply();
            }
        });
    }
}
