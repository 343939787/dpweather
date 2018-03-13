package com.example.dpbird.dpweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dpBird on 2018/3/10.
 */

public class Weather {

    public String status;
    public Basic basic;
    public Update update;
    public Now now;

    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;

    @SerializedName("daily_forecast")
    public List<DailyForecast> forecasties;

    public class Basic {
        @SerializedName("location")
        public String cityName;

        @SerializedName("cid")
        public String weatherId;
    }

    public class Update {
        @SerializedName("loc")
        public String updataTime;
    }

    public class Now {
        @SerializedName("tmp")
        public String temperature;

        @SerializedName("cond_txt")
        public String info;
    }

    public class Lifestyle {

        public String brf;
        public String txt;

    }

    public class DailyForecast {

        public String date;

        @SerializedName("cond_txt_d")
        public String weatherInfo;

        public String tmp_max;
        public String tmp_min;

    }
}



