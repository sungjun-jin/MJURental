package com.example.projectmjurental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmjurental.adapter.CustomAdapter;
import com.example.projectmjurental.data.Rent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReturnActivity extends AppCompatActivity {

    /*
    반납 액티비티

    1. 블루투스 연결

    2. 제품 상세정보 표시

    3. 반납 진행

     */

    TextView textInfo,textStartDate;

    Button btnReturn,btnBluetooth;

    Rent rent = null; //현재 대여하고 있는 물품

    CustomAdapter customAdapter;

    int index = 0; //인덱스


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        init();


    }

    private void init() {

        //초기화 메소드

        textInfo = findViewById(R.id.textInfo);
        textStartDate = findViewById(R.id.textStartDate);

        btnReturn = findViewById(R.id.btnReturn);
        btnBluetooth = findViewById(R.id.btnBluetooth);

        getRentInfo();
        setRentInfo();

        customAdapter = MainActivity.customAdapter;


        btnBluetooth.setOnClickListener(view -> {

            //블루투스 연결 버튼

        });

        btnReturn.setOnClickListener(view -> {

            //반납 버튼
            Rent rental;
            rental = customAdapter.data.get(index);
            rental.renting = false;

            //SimpleDateFormat으로 현재 시간을 년:월:일:시:분:초로 포맷 설정
            SimpleDateFormat sdf = new SimpleDateFormat("    yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            //현재 시간을 앞서 설정한 포맷으로 가져온다
            String endTime = sdf.format(new Date());
            rental.endDate = endTime; //반납 시간 삽입

            customAdapter.notifyDataSetChanged();
            finish();
        });


    }

    private void getRentInfo() {

        //1.대여한 물품의 정보를 가져옴 (이전 RentalActivity에서)

        //2.대여가 확정된 경우 대여한 물품의 데이터를 세팅

        Intent intent = getIntent();


        //RentalActivity에서 넘어온 Rent객체를 받는다
        if (intent.getSerializableExtra("Rent") != null) {

            rent = (Rent) intent.getSerializableExtra("Rent");

            Log.i("DEBUG_CODE","RentalActivity");

            Log.i("DEBUG_CODE","종류 : " + rent.object);
            Log.i("DEBUG_CODE","모델명 " + rent.modelName);
            Log.i("DEBUG_CODE","대여 키 : " + rent.rentKey);
            Log.i("DEBUG_CODE","사용자 이메일 : " + rent.email);
            Log.i("DEBUG_CODE","대여 시작 일 : " + rent.startDate);
            Log.i("DEBUG_CODE","대여 종료 일 : " + rent.endDate);


        }

        index = intent.getIntExtra("index",0);
        Log.i("DEBUG_CODE","인덱스 : " + index);

    }

    private void setRentInfo() {

        String info; //현재 대여하고 있는 물품의 정보를 담은 String

        //현재 대여하고 있는 물품의 정보를 View에 세팅

        //대여 물품 정보 초기화
        info = "대여 물품 : " + rent.object + "\n" +
                "모델명 : " + rent.modelName + "\n" +
                "보증금 : " + rent.deposit + "원\n";

        textStartDate.setText(rent.startDate); //대여 시작일 표시
        textInfo.setText(info); //대여 정보 표시

    }
}
