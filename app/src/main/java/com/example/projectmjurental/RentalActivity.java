package com.example.projectmjurental;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.projectmjurental.data.Battery;
import com.example.projectmjurental.data.Calculator;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Notebook;
import com.example.projectmjurental.data.Rent;
import com.example.projectmjurental.adapter.FragmentAdapter;
import com.example.projectmjurental.data.User;
import com.example.projectmjurental.fragment.ImageFragment;
import com.example.projectmjurental.kakao.AndroidBridge;
import com.example.projectmjurental.kakao.KakaoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import static com.example.projectmjurental.kakao.AndroidBridge.kakaoPayResult;



public class RentalActivity extends AppCompatActivity {


    TextView textObject, textObjectInfo; //대여물품, 대여물품정보
    Button btnDeposit, btnRent;
    String rentalObject; //대여물품
    ViewPager viewPager;

    FirebaseAuth mAuth; //파이어베이스 연결
    FirebaseUser currentUser;
    DatabaseReference userRef;
    FirebaseDatabase database;

    ArrayList<Integer> listImage = new ArrayList<>();

    FragmentAdapter fragmentAdapter;

    Rent rent = null;

    User loginUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        init();


        rent = getRentalObject();
        setImage();

        btnRent.setOnClickListener(view -> {

//          대여 버튼 누를 시

//          보증금 조건 검사 후, 물품의 대여 가능 여부 후 실행

            showRentDialog();


        });

        btnDeposit.setOnClickListener(view -> {

            //보증금 결제 누를 시
            Intent intent = new Intent(getApplicationContext(), KakaoActivity.class);
            startActivityForResult(intent, Const.REQ_KAKAO_PAY);

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

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userRef = database.getReference(Const.FB_REFER);


        getUserInfo();


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

            if(loginUser.rent) {
                //보증금 정상 입금 완료

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Rent", rent);
                startActivity(intent);
                finish();

            } else {

                //보증금 입금 필요
                Toast.makeText(getApplicationContext(),"보증금 결제 후 이용해 주세요.",Toast.LENGTH_SHORT).show();
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.REQ_KAKAO_PAY) {

            if (resultCode == RESULT_OK) {

                if (data != null) {

                    //카카오페이 보증금 결제에 대한 결과를 처리

//                    boolean result =data.getBooleanExtra("boolean", false);

                    //Firebase Database에 보증금 입금 여부 설정

                    Log.d("DEBUG_CODE","intent result : " + kakaoPayResult);

                    userRef.child(loginUser.num).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (kakaoPayResult) {
                                //결제 성공
                                dataSnapshot.getRef().child("rent").setValue(true);
                                Log.d("DEBUG_CODE","db changed true");
                                Log.d("DEBUG_CODE","kakaopay result" + kakaoPayResult);


                            } else {

                                //결제 실패
                                dataSnapshot.getRef().child("rent").setValue(false);
                                Log.d("DEBUG_CODE","db changed false");
                                Log.d("DEBUG_CODE","kakaopay result" + kakaoPayResult);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(getApplicationContext(), "DB 오류 : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }
    }

    public void getUserInfo() {

        {

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


                                if(loginUser.rent) {

                                    //보증금 입금 여부에 따른 버튼 처리
                                    btnDeposit.setEnabled(false);

                                } else {

                                    btnDeposit.setEnabled(true);
                                }


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
}
