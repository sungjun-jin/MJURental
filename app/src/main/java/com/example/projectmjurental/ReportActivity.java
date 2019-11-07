package com.example.projectmjurental;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ReportActivity extends AppCompatActivity {

    /*
    고장신고 액티비티

    step1. QR코드 스캔

    step2. 전화번호, 고장내용 입력

    step3. 고장신고 문자발송 (대여물품 종류, 사용자 이메일, 학번, 전화번호)
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

    }

}
