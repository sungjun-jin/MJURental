package com.example.projectmjurental.kakao;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.projectmjurental.R;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class KakaoActivity extends Activity {

    WebView webview;
    private final String APP_SCHEME = "iamportkakao://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);

        setTitle("kakao");
        webview = findViewById(R.id.webView);
        webview.setWebViewClient(new KakaoWebViewClient(this));
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        webview.loadUrl("file:///android_asset/www/kakao.html");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent != null) {
            Uri intentData = intent.getData();

            Log.d("DEBUG_CODE", intentData + "");

            if (intentData != null) {
                //카카오페이 인증 후 복귀했을 때 결제 후속조치
                String url = intentData.toString();

                Log.d("DEBUG_CODE", "결제 성공");

                if (url.startsWith(APP_SCHEME)) {
                    String path = url.substring(APP_SCHEME.length());
                    if ("process".equalsIgnoreCase(path)) {
                        webview.loadUrl("javascript:IMP.communicate({result:'process'})");

                    } else {
                        webview.loadUrl("javascript:IMP.communicate({result:'cancel'})");
                    }
                }
            }
        }


    }

    private void getKakaoHash() {

        //카카오톡 keyhash 받기
        try {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //카카오톡 keyhash 받기


    }


}





