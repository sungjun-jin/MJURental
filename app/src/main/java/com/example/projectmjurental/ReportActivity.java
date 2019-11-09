package com.example.projectmjurental;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectmjurental.data.Rent;

public class ReportActivity extends AppCompatActivity {

    /*
    고장신고 액티비티

    step1. QR코드 스캔

    step2. 전화번호, 고장내용 입력

    step3. 고장신고 문자발송 (대여물품 종류, 사용자 이메일, 학번, 전화번호)

     */

    Button btnReportQR, btnReport;
    EditText editReport;
    Rent rent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        init();

        btnReportQR.setOnClickListener(view -> {

            //QR 스캔

            Intent intent = new Intent(getApplicationContext(), QRActivity.class);
            startActivityForResult(intent,101);
            finish();

        });

           getRentInfo();

    }

    private void init() {

        btnReport = findViewById(R.id.btnReport);
        btnReportQR = findViewById(R.id.btnReportQR);
        editReport = findViewById(R.id.editReport);
    }

    private void getRentInfo() {

        //1.대여한 물품의 정보를 가져옴 (이전 RentalActivity에서)

        //2.대여가 확정된 경우 대여한 물품의 데이터를 세팅

        Intent intent = getIntent();

        //RentalActivity에서 넘어온 Rent객체를 받는다
        if (intent.getSerializableExtra("Rent") != null) {

            rent = (Rent) intent.getSerializableExtra("Rent");

            Log.i("DEBUG_CODE","대여 정보" );
            Log.i("DEBUG_CODE","대여 물품"+ rent.object);
            Log.i("DEBUG_CODE","대여 모델명" + rent.modelName);
        }
    }



}
