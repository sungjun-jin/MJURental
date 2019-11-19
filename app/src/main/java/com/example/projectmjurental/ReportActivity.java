package com.example.projectmjurental;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmjurental.data.Battery;
import com.example.projectmjurental.data.Calculator;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Notebook;
import com.example.projectmjurental.data.Rent;
import com.example.projectmjurental.data.Report;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    /*
    고장신고 액티비티

    step1. QR코드 스캔

    step2. 전화번호, 고장내용 입력

    step3. 고장신고 문자발송 (대여물품 종류, 사용자 이메일,  전화번호)

     */

    Button btnReportQR, btnReport;
    EditText editReport, editPhoneNum;
    Rent rent; //고장 신고 물품
    String reportObject;
    String intentString;
    String objectInfo;

    FirebaseAuth mAuth; //파이어베이스 연결
    FirebaseUser currentUser;
    DatabaseReference reportRef;
    FirebaseDatabase database;

    TextView textRentInfo;


    boolean isQRScan = false; //QR 코드 스캔여부 검사


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        init();

        Intent rentIntent = getIntent();
        intentString = rentIntent.getStringExtra(Const.REPORT);

        if (intentString.equals(Const.MAIN)) {

            //메인 액티비티 초기 호출
            Log.i("DEBUG_CODE", "메인 액티비티 초기 호출");


        } else if (intentString.equals(Const.calculator) || intentString.equals(Const.battery) || intentString.equals(Const.notebook)) {

            isQRScan = true; //QR 스캔여부를 true로 바꿈

            //QR코드로 제품 인식 후 고장신고 액티비티 재 호출.

            Log.i("DEBUG_CODE", "QR 코드로 물체 인식 후 액티비티 재 호출");
            //QR 코드로 물체 인식 후 액티비티 재 호출
            rent = getReportObject(rentIntent);

            getReportInfo();


        }

        btnReportQR.setOnClickListener(view -> {

            //QR 스캔

            Intent QRIntent = new Intent(getApplicationContext(), QRActivity.class);
            QRIntent.putExtra(Const.QR, Const.REPORT);
            startActivityForResult(QRIntent, 101);
            finish();

        });

        btnReport.setOnClickListener(view -> {

            //신고 접수 버튼

            //1. 사용자가 구체적으로 정보를 다 기입했는지 확인

            //2. AlertDialogue 확인

            //3. 문자 메세지로 관리자에게 전송

            //4. DB 삽입

            if (checkReportInfo()) {

                //사용자가 모든 정보를 다 기입했으면

                //대여 안내 Dialog 메세지 띄우기

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("고장신고 접수 안내").setMessage("고장신고를 접수하시겠습니까?");
                builder.setPositiveButton("네", (dialogInterface, i) -> {

                    //고장신고 접수 -> 1. 파이어베이스에 데이터 저장 (Report.class) 2. 관리자에게 문자 메세지 전송

                    setReportData();
                    finish();

                });

                builder.setNegativeButton("아니오", (dialogInterface, i) -> {


                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void init() {

        //초기화

        btnReport = findViewById(R.id.btnReport);
        btnReportQR = findViewById(R.id.btnReportQR);
        editReport = findViewById(R.id.editReport);
        editPhoneNum = findViewById(R.id.editPhoneNum);

        textRentInfo = findViewById(R.id.textRentInfo);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); //현재 사용자의 정보를 가져온다
        database = FirebaseDatabase.getInstance();
        reportRef = database.getReference(Const.REPORT_REFER);
    }

    Rent getReportObject(Intent intent) {

        //대여 물품 분류 -> 대여 물품 정보 표시


        reportObject = intent.getStringExtra(Const.REPORT);

        if (reportObject == null) {

            Log.i("DEBUG_CODE", "RENTAL OBJECT null");

            return null;
        }

        switch (reportObject) {

            case Const.notebook:

                //노트북

                rent = new Notebook(); //노트북 객체 생성
                ((Notebook) rent).setValue(); //노트북의 보증금, 정보, 모델명 세팅
                rent.object = Const.notebook; // Rent객체이름에 notebook 세팅

                break;
            case Const.battery:

                //보조배터리

                rent = new Battery();
                ((Battery) rent).setValue();
                rent.object = Const.battery;

                break;
            case Const.calculator:

                //공학용계산기

                rent = new Calculator();
                ((Calculator) rent).setValue();
                rent.object = Const.calculator;

                break;
        }


        return rent;
    }

    private void getReportInfo() {

        //고장신고에 대한 구체적인 정보를 정리하는 메소드

        //QR 코드 스캔 완료 후 제품에 대한 정보가 고장내용란에 입력된다

        objectInfo = "고장신고 접수\n" + "제품명 : " + rent.object + "\n모델명 : " + rent.modelName;

        String email = currentUser.getEmail(); //사용자의 이메일을 가져옴
        String phoneNum = currentUser.getPhoneNumber();

        Log.i("DEBUG_CODE", "현재 사용자 : " + currentUser.getEmail() + currentUser.getPhoneNumber());

        objectInfo = objectInfo + "\n제보자 이메일 : " + email + "\n제보자 연락처 : " + phoneNum;

        textRentInfo.setText(objectInfo);


    }

    private boolean checkReportInfo() {

        //1. 사용자가 구체적으로 정보를 다 기입했는지 확인하는 메소드

        if (!isQRScan) {

            Toast.makeText(getApplicationContext(), "QR 코드를 스캔하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String editString = editReport.getText().toString();

        if (editString.isEmpty()) {

            Toast.makeText(getApplicationContext(), "고장 내용을 구체적으로 기입해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String phoneNum = editPhoneNum.getText().toString();

        if (phoneNum.isEmpty()) {

            Toast.makeText(getApplicationContext(), "휴대폰 번호를 기입해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setReportData() {

        //파이어베이스에 고장신고 데이터 등록

        //데이터 세팅
        //SimpleDateFormat으로 현재 시간을 년:월:일:시:분:초로 포맷 설정
        SimpleDateFormat sdf = new SimpleDateFormat("    yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        //현재 시간을 앞서 설정한 포맷으로 가져온다
        String currentTime = sdf.format(new Date());

        //파이어베이스와 문자 메세지로 저장될 최종 정보
        String info = "제보자 이름 : " + currentUser.getEmail() + "\n제보자 연락처 : " + editPhoneNum.getText().toString() + "\n제품명 : " + rent.object
                + "\n모델명 : " + rent.modelName + "\n제보 시간 : " + currentTime;

        //Report 클래스 생성
        Report report = new Report(currentUser.getEmail(), editPhoneNum.getText().toString(), rent.object, rent.modelInfo, info, currentTime);

        String key = currentTime; //키는 제보한 날짜로 설정
        reportRef.child(key).setValue(report); //데이터 삽입
        sendSMS(info); //관리자에게 문자메세지 전송

        Toast.makeText(this, "고장신고가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(String info) {

        //관리자에게 문자 메세지를 보내는 메소드 (setReportData() 메소드에서 실행)

        Intent SMSIntent = new Intent(Intent.ACTION_VIEW);
        SMSIntent.putExtra("address", Const.ADMIN_NUM);
        SMSIntent.putExtra("sms_body", info);
        SMSIntent.setType("vnd.android-dir/mms-sms");
        startActivity(SMSIntent);
    }

//    private void sendSMS(String info) {
//
//
//        String phoneNumber = Const.ADMIN_NUM;
//        String smsBody = info;
//
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
//    }


}
