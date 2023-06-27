package com.example.appimop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;


/**
 * DeepLink 先调用 onCreate 如果 finish（） 了就不会调用 onStart
 * 这个流程是正常的。
 */
public class DeepLinkJavaActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData(); // app44://www.example44.com/p/ath2?a=123&b=432432 参数 b 没有被传递

        Log.d("deeplink", "j oncreate: action: " + action + " data: " + data.toString());

//        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData(); // app44://www.example44.com/p/ath2?a=123&b=432432 参数 b 没有被传递

        Log.d("deeplink", "j onstart: action: " + action + " data: " + data.toString());

        finish();
    }
}
