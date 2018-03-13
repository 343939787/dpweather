package com.example.dpbird.dpweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dpBird on 2018/3/6.
 */

public class HttpUtils {

    public static void sendOkhttpRequest(String address, okhttp3.Callback callback) {

            OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
