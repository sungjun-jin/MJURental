package com.example.projectmjurental.kakao;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectmjurental.R;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Rent;
import com.example.projectmjurental.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.projectmjurental.kakao.AndroidBridge.kakaoPayResult;


public class KakaoActivity extends Activity {

    WebView webview;
    private final String APP_SCHEME = "iamportkakao://";

    User loginUser; //현재 사용자
    AndroidBridge androidBridge;

    //파이어베이스 연결
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference userRef;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);

        init();
        androidBridge = new AndroidBridge(webview,this,this,loginUser,mAuth,currentUser,database);
        webview.setWebViewClient(new KakaoWebViewClient(this));
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/www/kakao.html");
        webview.addJavascriptInterface(androidBridge,"Android");

        androidBridge.getMsg();


        //카카오 페이 결제에 대한 결과를 RentalActivity에 넘겨준다
        Intent kakaoIntent = new Intent();
        kakaoIntent.putExtra("boolean",kakaoPayResult);
        setResult(Activity.RESULT_OK,kakaoIntent);

        Intent intent = getIntent();

    }

    private void init() {

        webview = findViewById(R.id.webView);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userRef = database.getReference(Const.FB_REFER);
        getUserInfo(); //사용자의 정보를 가져옴
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d("DEBUG_CODE","onNewIntent");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("DEBUG_CODE","onResume1");

        Intent intent = getIntent();
        if (intent != null) {
            Uri intentData = intent.getData();
            Log.d("DEBUG_CODE","onResume2");

            if (intentData != null) {
                //카카오페이 인증 후 복귀했을 때 결제 후속조치
                String url = intentData.toString();
                Log.d("DEBUG_CODE","onResume3");

                if (url.startsWith(APP_SCHEME)) {
                    String path = url.substring(APP_SCHEME.length());
                    if ("process".equalsIgnoreCase(path)) {
                        webview.loadUrl("javascript:IMP.communicate({result:'process'})");
                        Log.d("DEBUG_CODE","onResume4");


                    } else {
                        webview.loadUrl("javascript:IMP.communicate({result:'cancel'})");
                        Log.d("DEBUG_CODE","onResume5");

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

    public void getUserInfo() {

        //현재 사용자의 정보를 받아옴

        {

            //현재 로그인한 회원 정보를 가져와 navigation header에 띄우는 메소드

            userRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        User user;
                        user = child.getValue(User.class);

                        if (user.email.equals(mAuth.getCurrentUser().getEmail())) {

                            loginUser = user;

                            //로그인이 성공하면 메인엑테비티에 navigationview header에 사용자에 대한 정보를 표시

                            Log.d("DEBUG_CODE","Kakao User email : " + loginUser.email);
                            Log.d("DEBUG_CODE","Kakao User num : " + loginUser.num);

                            kakaoPayResult = loginUser.rent;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.d("DEBUG_CODE", "회원정보 가져오기 실패");
                    Toast.makeText(getApplicationContext(), "회원정보를 가져오는데 실패했습니다." + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }



    }

}





