package com.example.dpbird.dpweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dpBird on 2018/3/10.
 */

public class Weather {

    public String status;
    public Basic basic;
    public Aqi aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecasties;

    public class Basic {
        @SerializedName("city")
        public String cityName;

        @SerializedName("id")
        public String weatherId;

        public Update update;

        public class Update {
            @SerializedName("loc")
            public String updataTime;
        }
    }

    public class Aqi {

        public City city;

        public class City{
            public String aqi;
            public String pm25;
        }
    }

    public class Now {
        @SerializedName("tmp")
        public String temperature;

        public Cond cond;

        public class Cond {
            @SerializedName("txt")
            public String info;
        }
    }

    public class Suggestion {

        @SerializedName("comf")
        public Comfort comfort;

        public class Comfort {

            @SerializedName("txt")
            public String info;
        }

        @SerializedName("cw")
        public CarWash carwash;

        public class CarWash {
            @SerializedName("txt")
            public String info;
        }

        public Sport sport;

        public class Sport {
            @SerializedName("txt")
            public String info;
        }
    }

    public class Forecast {

        public String date;

        public Cond cond;

        public class Cond {
            @SerializedName("txt_d")
            public String info;
        }

        @SerializedName("tmp")
        public temperature temperature;

        public class temperature {
            public String max;
            public String min;
        }

    }
}

