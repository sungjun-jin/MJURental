package com.example.projectmjurental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmjurental.data.Const;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class QRActivity extends AppCompatActivity {

    // 렌트할 장비명을 담는 변수
    String rentalObject;

    // QR CODE object
    private IntentIntegrator qrScan;
    Intent intent;
    String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);


        // intializing scan object
        qrScan = new IntentIntegrator(this);


        qrScan.setPrompt("Scanning...");
        qrScan.initiateScan();

        intent = getIntent();
        event = intent.getStringExtra(Const.QR);

        Log.i("DEBUG_CODE", "Event : " + event);

    }

    // Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(QRActivity.this, "취소!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {

                // qrcode 결과가 있으면
                Toast.makeText(QRActivity.this, "스캔완료!", Toast.LENGTH_SHORT);
                try {
                    // data를 json으로 반환
                    JSONObject object = new JSONObject(result.getContents());


                } catch (JSONException e) {

                    e.printStackTrace();

                    //대여 물품의 정보를 입력받는다
                    rentalObject = result.getContents();

                    if (event.equals(Const.RENT)) {

                        //메인 액티비티로부터 QRActivity가 호출되었을 시
                        //QR 코드로 제품 인식 후 RentalActivity로 대여 물품에 대한 정보를 보낸다.

                        Intent intent = new Intent(this.getApplicationContext(), RentalActivity.class);
                        intent.putExtra("Object", rentalObject); //대여할 물품의 이름을 String 형태로 넘겨준다
                        startActivity(intent); //대여 액티비티로 대여물품의 정보를 넘겨주고 이동
                        finish();

                    } else if (event.equals(Const.REPORT)) {

                        //고장신고 액티비티로부터 QRActivity가 호출되었을 시
                        //QR 코드로 제품 인식 후 ReportActivity로 대여 물품에 대한 정보를 보낸다.

                        Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                        Log.i("DEBUG_CODE", "QR -> REPORT :" + rentalObject);
                        intent.putExtra(Const.REPORT, rentalObject); //고장신고할 물품의 이름을 String 형태로 넘겨준다
                        startActivity(intent);
                        finish();
                    }


                }
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


}

