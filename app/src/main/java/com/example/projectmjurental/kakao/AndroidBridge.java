package com.example.projectmjurental.kakao;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.projectmjurental.RentalActivity;
import com.example.projectmjurental.data.Const;

import com.example.projectmjurental.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AndroidBridge {

    //webView와 javascript통신을 위해 사용하는 Bridge

    private String TAG = "AndroidBridge";
    private WebView webView;
    private Context context;
    private Activity activity;

    FirebaseAuth mAuth; //파이어베이스 연결
    FirebaseUser currentUser;
    DatabaseReference userRef;
    FirebaseDatabase database;

    User user; //현재 사용자

    Boolean result;

    public AndroidBridge(WebView webView, Context context, Activity activity, User user, FirebaseAuth mAuth, FirebaseUser currentUser, FirebaseDatabase database) {

        this.webView = webView;
        this.context = context;
        this.activity = activity;
        this.user = user;

        this.mAuth = mAuth;
        this.currentUser = currentUser;
        this.database = database;
        userRef = database.getReference(Const.FB_REFER);

    }

    @JavascriptInterface
    public void call_log(final String message) {

        //결제 Javascript에서 bridge를 통해 넘어온 결제 결과를 앱에서 처리



        //WebView에서 처리된 카카오톡 결과 메세지(String message)에 따라 결과 분기처리

        if (message.equals("success")) {

            user.rent = true; //사용자 정보 변경
            result = true;
            KakaoActivity.kakaoPayResult = true;

        } else if (message.equals("failure")) {

            user.rent = false; //사용자 정보 변경
            result = false;
        }
        activity.finish();

    }


}
