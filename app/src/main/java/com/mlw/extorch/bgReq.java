package com.mlw.extorch;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class bgReq  extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... urls) {
        String url = urls[0];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

}
