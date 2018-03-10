package com.example.dpbird.dpweather.util;

import android.text.TextUtils;

import com.example.dpbird.dpweather.db.City;
import com.example.dpbird.dpweather.db.County;
import com.example.dpbird.dpweather.db.Province;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by dpBird on 2018/3/6.
 */

public class JsonUtils {

    public static Boolean handleProvinceResponce(String responce) {
        if (!TextUtils.isEmpty(responce)) {
            try {
                JSONArray jsonArray = new JSONArray(responce);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean handleCityResponce(String responce, int provinceId) {
        if (!TextUtils.isEmpty(responce)) {
            try {
                JSONArray jsonArray = new JSONArray(responce);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean handleCountyResponce(String responce, int cityId) {
        if (!TextUtils.isEmpty(responce)) {
            try {
                JSONArray jsonArray = new JSONArray(responce);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
