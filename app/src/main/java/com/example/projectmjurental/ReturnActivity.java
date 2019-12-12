package com.example.projectmjurental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmjurental.adapter.CustomAdapter;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Rent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class ReturnActivity extends AppCompatActivity {

    /*
    반납 액티비티

    1. 블루투스 연결

    2. 제품 상세정보 표시

    3. 반납 진행

     */

    TextView textInfo, textStartDate, textBlueTooth;
    TextView textReturnTitle, textReturn1, textReturn2;
    Button btnReturn, btnBluetooth;
    Rent rent = null; //현재 대여하고 있는 물품

    CustomAdapter customAdapter;
    FirebaseDatabase database; //파이어베이스 데이터베이스
    DatabaseReference rentRef; //대여 레퍼런스
    int index = 0; //인덱스

    private BluetoothSPP bluetooth;

    //블루투스 연결 여부를 알려주는 변수
    private boolean isBluetooth = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        init();
        setRentalButton();
        connectBlueTooth();
    }

    private void init() {

        //초기화 메소드

        //블루투스 초기화
        bluetooth = new BluetoothSPP(this);

        textInfo = findViewById(R.id.textInfo);
        textStartDate = findViewById(R.id.textStartDate);
        textReturnTitle = findViewById(R.id.textReturnTitle);
        textReturn1 = findViewById(R.id.textReturn1);
        textReturn2 = findViewById(R.id.textReturn2);
        textBlueTooth = findViewById(R.id.textBlueTooth);

        btnReturn = findViewById(R.id.btnReturn);
        btnBluetooth = findViewById(R.id.btnBluetooth);

        getRentInfo();
        setRentInfo();

        customAdapter = MainActivity.customAdapter;

        database = FirebaseDatabase.getInstance();
        rentRef = database.getReference(Const.RENT_REFER);

        setView();

        btnBluetooth.setOnClickListener(view -> {

            //블루투스 연결 버튼
            if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bluetooth.disconnect();
            } else {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });

        btnReturn.setOnClickListener(view -> {

            returnItem();
        });
    }

    //블루투스 메소드

    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void connectBlueTooth() {

        //반납 전 블루투스 연결 메소드

        if (!bluetooth.isBluetoothAvailable()) {

            //블루투스 사용 불가
            Toast.makeText(getApplicationContext(), "블루투스 연결이 불가능합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        bluetooth.setOnDataReceivedListener((data, message) -> {

        });

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {

                //정상 연결

                Toast.makeText(getApplicationContext(), "블루투스를 통한 위치 인증이 완료되었습니다. " + name + "\n" + address, Toast.LENGTH_SHORT).show();

//                연결한 블루투스 디바이스의 이름이 CAPSTONE4일때만 반납 버튼 활성화
                if(name.equals("MJURENTAL")) {

                    isBluetooth = true;
                    setRentalButton();

                }



            }

            @Override
            public void onDeviceDisconnected() {

                //연결 끊김

                Toast.makeText(getApplicationContext(), "블루투스 연결 종료", Toast.LENGTH_SHORT).show();
                isBluetooth = false;
                setRentalButton();

            }

            @Override
            public void onDeviceConnectionFailed() {

                //연결 실패

                Toast.makeText(getApplicationContext(), "블루투스 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                isBluetooth = false;
                setRentalButton();

            }
        });

    }

    private void setRentalButton() {

        //블루투스 연결 상태에 따라 버튼 상태, 안내메세지 지정

        //블루투스 연결 성공 시 버튼 활성화

        if (isBluetooth) {

            btnReturn.setClickable(true);
            textBlueTooth.setText("(연결성공)");
            textBlueTooth.setTextColor(Color.parseColor("#26F736"));


        } else {

            btnReturn.setClickable(false);
            textBlueTooth.setText("(연결실패)");
            textBlueTooth.setTextColor(Color.parseColor("#EC3636"));


        }
    }


    public void setup() {

    }

    //블루투스 메소드

    //대여 메소드

    private void getRentInfo() {

        //1.대여한 물품의 정보를 가져옴 (이전 RentalActivity에서)

        //2.대여가 확정된 경우 대여한 물품의 데이터를 세팅

        Intent intent = getIntent();


        //RentalActivity에서 넘어온 Rent객체를 받는다
        if (intent.getSerializableExtra("Rent") != null) {

            rent = (Rent) intent.getSerializableExtra("Rent");

            Log.i("DEBUG_CODE", "RentalActivity");

            Log.i("DEBUG_CODE", "종류 : " + rent.object);
            Log.i("DEBUG_CODE", "모델명 " + rent.modelName);
            Log.i("DEBUG_CODE", "대여 키 : " + rent.rentKey);
            Log.i("DEBUG_CODE", "사용자 이메일 : " + rent.email);
            Log.i("DEBUG_CODE", "대여 시작 일 : " + rent.startDate);
            Log.i("DEBUG_CODE", "대여 종료 일 : " + rent.endDate);


        }

        index = intent.getIntExtra("index", 0);
        Log.i("DEBUG_CODE", "인덱스 : " + index);

    }

    private void setRentInfo() {

        String info; //현재 대여하고 있는 물품의 정보를 담은 String

        //현재 대여하고 있는 물품의 정보를 View에 세팅

        //대여 물품 정보 초기화
        info = "대여 물품 : " + rent.object + "\n" +
                "모델명 : " + rent.modelName + "\n" +
                "보증금 : " + rent.deposit + "원\n";

        if (!rent.renting) {

            //정상 반납일 경우 대여일시를 기존 정보에 더해준다

            info = info + "반납 일시 : " + rent.endDate + "\n";
        }

        textStartDate.setText(rent.startDate); //대여 시작일 표시
        textInfo.setText(info); //대여 정보 표시

    }

    private void updateData(String endTime) {

        //반납으로 인한 파이어베이스 데이터 수정

        //대여 시 발급받은 Rent Key를 활용하여 파이어베이스 데이터 접근

        rentRef.child(rent.rentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("renting").setValue(false); //파이어베이스의 대여 여부를 false(반납 완료)로 수정
                dataSnapshot.getRef().child("endDate").setValue(endTime); //파이어베이스의 반납 시간(endTime)에 현재 시간을 넣어준다
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.i("DEBUG_CODE", databaseError.getMessage());

            }
        });


    }

    private void returnItem() {

        //반납을 실행하는 메소드

        {
            //반납 안내 Dialog 메세지 띄우기

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("반납안내").setMessage("반납하시겠습니까?");

            builder.setPositiveButton("네", (dialogInterface, i) -> {

                //반납 버튼
                Rent rental;
                //RecylerView의 인덱스를 가져와 반납하고자 하는 물품의 인스턴스를 가져온다
                rental = customAdapter.data.get(index);
                //대여 중인 상태를 반납 완료 상태로 바꿔준다
                rental.renting = false;

                //SimpleDateFormat으로 현재 시간을 년:월:일:시:분:초로 포맷 설정
                SimpleDateFormat sdf = new SimpleDateFormat("    yyyy-MM-dd HH:mm:ss", Locale.KOREA);

                //파이어 베이스에서 수정을 해줘야 즉각 반영된다

                //현재 시간을 앞서 설정한 포맷으로 가져온다
                String endTime = sdf.format(new Date());
                //파이어베이스에 반납 일시를 저장한다
                updateData(endTime);

                Toast.makeText(this, "반납이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();

            });

            builder.setNegativeButton("아니오", (dialogInterface, i) -> {

                Toast.makeText(this, "반납이 취소되었습니다.", Toast.LENGTH_SHORT).show();

            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }

    private void setView() {

        //반납 여부에 따라 뷰를 조정

        if (!rent.renting) {

            //정상 반납 완료

            btnReturn.setVisibility(View.INVISIBLE);
            btnBluetooth.setVisibility(View.INVISIBLE);
            textReturn1.setVisibility(View.INVISIBLE);
            textReturn2.setVisibility(View.INVISIBLE);
            textReturnTitle.setVisibility(View.INVISIBLE);
            textBlueTooth.setVisibility(View.INVISIBLE);

        } else {

            //대여 중

        }


    }
}
