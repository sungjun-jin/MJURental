package com.example.projectmjurental;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.projectmjurental.data.Battery;
import com.example.projectmjurental.data.Calculator;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Notebook;
import com.example.projectmjurental.data.Rent;
import com.example.projectmjurental.adapter.FragmentAdapter;
import com.example.projectmjurental.fragment.ImageFragment;
import com.example.projectmjurental.kakao.KakaoActivity;


import java.util.ArrayList;


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

        btnRent.setOnClickListener(view -> {

//          대여 버튼 누를 시

//          보증금 조건 검사 후, 물품의 대여 가능 여부 후 실행

            showRentDialog();


        });

        btnDeposit.setOnClickListener(view -> {

            //보증금 결제 누를 시
            Intent intent = new Intent(getApplicationContext(),KakaoActivity.class);
            startActivityForResult(intent,102);

        });
    }

    void setImage() {

//        Fragment에 대여 이미지 삽입

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        //ViewPager와 FragmentAdapter 연결

        viewPager.setAdapter(fragmentAdapter);

        if (rentalObject.equals(Const.notebook)) {


            listImage.add(R.drawable.gram1);
            listImage.add(R.drawable.gram2);
            listImage.add(R.drawable.gram3);

        } else if (rentalObject.equals(Const.battery)) {


            //보조배터리

            listImage.add(R.drawable.battery1);
            listImage.add(R.drawable.battery2);
            listImage.add(R.drawable.battery3);


        } else if (rentalObject.equals(Const.calculator)) {


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

        if (rentalObject.equals(Const.notebook)) {

            //노트북

            textObject.setText(rentalObject); //항목명에 데이터 세팅
            rent = new Notebook(); //노트북 객체 생성

            ((Notebook) rent).setValue(); //노트북의 보증금, 정보, 모델명 세팅
            rent.object = Const.notebook; // Rent객체이름에 notebook 세팅
            textObjectInfo.setText(rent.modelInfo); //액티비티 항목 정보 세팅


        } else if (rentalObject.equals(Const.battery)) {

            //보조배터리

            textObject.setText(rentalObject);
            rent = new Battery();

            ((Battery) rent).setValue();
            rent.object = Const.battery;
            textObjectInfo.setText(rent.modelInfo);


        } else if (rentalObject.equals(Const.calculator)) {

            //공학용계산기


            textObject.setText(rentalObject);
            rent = new Calculator();
            ((Calculator) rent).setValue();
            rent.object = Const.calculator;
            textObjectInfo.setText(rent.modelInfo);


        }

        return rent;
    }

    private void showRentDialog() {

        //대여 안내 Dialog 메세지 띄우기

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("대여안내").setMessage(rentalObject + "(을)를 대여하시겠습니까?");
        builder.setPositiveButton("네", (dialogInterface, i) -> {

            //대여 시작, MainActivity로 현재 대여할 물품의 정보를 intent로 넘기고 이동

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("Rent", rent);
            startActivity(intent);
            finish();

        });

        builder.setNegativeButton("아니오", (dialogInterface, i) -> {


        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        //뒤로가기를 누르면 MainActivity로 간다
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
