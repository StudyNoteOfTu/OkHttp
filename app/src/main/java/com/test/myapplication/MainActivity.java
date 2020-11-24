package com.test.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test.myapplication.net.Call;
import com.test.myapplication.net.Callback;
import com.test.myapplication.net.HttpClient;
import com.test.myapplication.net.Request;
import com.test.myapplication.net.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpClient dnHttpClient = new HttpClient();
        String url = "http://www.baidu.com";
        Request request = new Request.Builder()
                .url(url)
                .get()  //默认为GET请求，可以不写
                .build();
        Call call = dnHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {

            }

            @Override
            public void onResponse(Call call, Response response) {

            }
        });

    }
}
