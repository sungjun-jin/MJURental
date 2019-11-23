package com.example.projectmjurental.kakao;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class AndroidBridge {

    //webView와 javascript통신을 위해 사용하는 Brdige

    private String TAG = "AndroidBridge";
    private WebView webView;
    private Context context;

    public AndroidBridge(WebView webView, Context context) {

        this.webView = webView;
        this.context = context;

    }

    @JavascriptInterface
    public void call_log (final String message) {

        Log.d("DEBUG_CODE","TAG : " + message);
        Toast.makeText(context,"결제 결과 : " + message, Toast.LENGTH_LONG).show();

        //메세지에 따라 intent 분기처리 해야함

    }
}
