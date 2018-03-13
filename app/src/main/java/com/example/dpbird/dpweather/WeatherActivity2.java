package com.example.dpbird.dpweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dpbird.dpweather.gson.Weather;
import com.example.dpbird.dpweather.service.AutoUpdateService;
import com.example.dpbird.dpweather.util.HttpUtils;
import com.example.dpbird.dpweather.util.JsonUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity2 extends AppCompatActivity{

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleTime;
    private TextView temperature;
    private TextView weatherInfo;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comforText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bcgView;
    private String weatherName;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button navButton;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View dercorView = getWindow().getDecorView();
            dercorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleTime = (TextView) findViewById(R.id.title_update_time);
        temperature = (TextView) findViewById(R.id.temp_text);
        weatherInfo = (TextView) findViewById(R.id.weather_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comforText = (TextView) findViewById(R.id.comfort_text);
        bcgView = (ImageView) findViewById(R.id.bcg_pic);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
       /* carWashText = (TextView) findViewById(R.id.washcar_text);
        sportText = (TextView) findViewById(R.id.sport_text);*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherContent = preferences.getString("weather", null);
        if (weatherContent != null) {
            Weather weather = JsonUtils.handleWeatherResponce(weatherContent);
            weatherName = weather.basic.cityName;
            showWeatherInfo(weather);
        } else
        {
            weatherName = getIntent().getStringExtra("location");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherName);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherName);
            }
        });
        String bcgPic = preferences.getString("bcg_pic", null);
        if (bcgPic != null) {
            Glide.with(this).load(bcgPic).into(bcgView);
        } else {
            loadBcgPic();
        }
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    public void requestWeather(String location) {

        String address = "https://free-api.heweather.com/s6/weather?location=" + location + "&key=98ac3205a50948ffb0622d91d693a94d";
        HttpUtils.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity2.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responceText = response.body().string();
                final Weather weather = JsonUtils.handleWeatherResponce(responceText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity2.this).edit();
                            editor.putString("weather", responceText);
                            editor.apply();
                            weatherName = weather.basic.cityName;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity2.this, "不能获取天气信息", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBcgPic();
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.updataTime.split(" ")[1];
        String noWtemperature = weather.now.temperature + "℃";
        String noWWeather = weather.now.info;
        titleCity.setText(cityName);
        titleTime.setText(updateTime);
        temperature.setText(noWtemperature);
        weatherInfo.setText(noWWeather);
       forecastLayout.removeAllViews();
        for (Weather.DailyForecast forecast: weather.forecasties) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dataText = (TextView) view.findViewById(R.id.data_text);
            TextView weathers = (TextView) view.findViewById(R.id.forecast_weather);
            TextView max = (TextView) view.findViewById(R.id.max);
            TextView min = (TextView) view.findViewById(R.id.min);
            dataText.setText(forecast.date);
            weathers.setText(forecast.weatherInfo);
            max.setText(forecast.tmp_max);
            min.setText(forecast.tmp_min);
            forecastLayout.addView(view);
        }
        for (Weather.Lifestyle lifestyle : weather.lifestyleList){
            String brf = "舒适度:" + lifestyle.brf;
            String txt =  lifestyle.txt;
            comforText.setText(brf + " , " + txt);
        }
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void loadBcgPic() {
        String address = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String pic = response.body().string();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity2.this).edit();
                edit.putString("bcg_pic", pic);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity2.this).load(pic).into(bcgView);
                    }
                });
            }
        });
    }

}