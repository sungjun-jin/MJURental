package com.example.projectmjurental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class QRActivity extends AppCompatActivity {

    // 렌트할 장비명을 담는 변수
    String rentalObject;

    // QR CODE object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);


        // intializing scan object
        qrScan = new IntentIntegrator(this);


        qrScan.setPrompt("Scanning...");
        qrScan.initiateScan();

    }

    // Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(QRActivity.this, "취소!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,MainActivity.class);
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


                    Log.i("DEBUG_CODE","rentalobject");
                    Intent intent = new Intent(this.getApplicationContext(),RentalActivity.class);
                    intent.putExtra("Object",rentalObject);
                    startActivity(intent); //대여 액티비티로 대여물품의 정보를 넘겨주고 이동
                    finish();

                }
            }




        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


}

