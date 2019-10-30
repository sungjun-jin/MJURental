package com.example.projectmjurental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.projectmjurental.data.Battery;
import com.example.projectmjurental.data.Calculator;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Notebook;
import com.example.projectmjurental.data.Rent;
import com.example.projectmjurental.fragment.FragmentAdapter;
import com.example.projectmjurental.fragment.ImageFragment;

import java.util.ArrayList;

import static com.example.projectmjurental.data.Const.notebook;

public class RentalActivity extends AppCompatActivity {


    TextView textObject, textObjectInfo; //대여물품, 대여물품정보
    Button btnDeposit, btnRent;
    String rentalObject; //대여물품
    ViewPager viewPager;

    ArrayList<Integer> listImage = new ArrayList<>();

    FragmentAdapter fragmentAdapter;

    Rent rent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        init();

        //test용
//        rentalObject = "명지대학교노트북";
//        rentalObject = "명지대학교배터리";
//        rentalObject = "명지대학교계산기";

        rent = getRentalObject();
        setImage();

        //로그 테스트
        Log.i("DEBUG_CODE", rent.modelName);
        Log.i("DEBUG_CODE", rent.modelInfo);
        Log.i("DEBUG_CODE", rent.deposit + "");
        Log.i("DEBUG_CODE", rent.available + "");
        //로그 테스트

        btnRent.setOnClickListener(view -> {

            //대여 버튼 누를 시

            //보증금 조건 검사 후, 물품의 대여 가능 여부 후 실행

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("Rent",rent);
            startActivity(intent);
            finish();


        });

        btnDeposit.setOnClickListener(view -> {

            //보증금 결제 누를 시

        });
    }

    void setImage() {

//        Fragment에 대여 이미지 삽입

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        //ViewPager와 FragmentAdapter 연결

        viewPager.setAdapter(fragmentAdapter);

        if (rentalObject.equals(notebook)) {

            Log.i("DEBUG_CODE", "노트북");

            listImage.add(R.drawable.gram1);
            listImage.add(R.drawable.gram2);
            listImage.add(R.drawable.gram3);

        } else if (rentalObject.equals(Const.battery)) {

            Log.i("DEBUG_CODE", "배터리");

            //보조배터리

            listImage.add(R.drawable.battery1);
            listImage.add(R.drawable.battery2);
            listImage.add(R.drawable.battery3);


        } else if (rentalObject.equals(Const.calculator)) {

            Log.i("DEBUG_CODE", "계산기");

            //공학용계산기

            listImage.add(R.drawable.calculator1);
            listImage.add(R.drawable.calculator2);
            listImage.add(R.drawable.calculator3);
        }

        for (int i = 0; i < listImage.size(); i++) {

            ImageFragment imageFragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", listImage.get(i));
            imageFragment.setArguments(bundle);
            fragmentAdapter.addItem(imageFragment);
        }
        fragmentAdapter.notifyDataSetChanged();
    }


    void init() {

        //액티비티 위젯 초기화

        textObject = findViewById(R.id.textObject);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnRent = findViewById(R.id.btnRent);
        textObjectInfo = findViewById(R.id.textObjectInfo);
        viewPager = findViewById(R.id.viewPager);
    }

    Rent getRentalObject() {

        //대여 물품 분류 -> 대여 물품 정보 표시

        Intent intent = getIntent();
        rentalObject = intent.getStringExtra("Object");

        Rent rent = null;


        if (rentalObject.equals(notebook)) {


            //노트북
            textObject.setText(rentalObject);
            rent = new Notebook();
            ((Notebook) rent).setValue();
            textObjectInfo.setText(rent.modelInfo);


        } else if (rentalObject.equals(Const.battery)) {

            //보조배터리

            //노트북
            textObject.setText(rentalObject);
            rent = new Battery();
            ((Battery) rent).setValue();
            textObjectInfo.setText(rent.modelInfo);


        } else if (rentalObject.equals(Const.calculator)) {

            //공학용계산기

            //노트북
            textObject.setText(rentalObject);
            rent = new Calculator();
            ((Calculator) rent).setValue();
            textObjectInfo.setText(rent.modelInfo);

        }

        return rent;
    }


}
