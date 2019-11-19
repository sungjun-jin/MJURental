package com.example.projectmjurental;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmjurental.adapter.CustomAdapter;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Rent;
import com.example.projectmjurental.user.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.demo.DetectorActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth; //파이어베이스 연결
    FirebaseDatabase database; //파이어베이스 데이터베이스
    DatabaseReference userRef; //사용자 레퍼런스
    DatabaseReference rentRef; //대여 레퍼런스
    FirebaseUser currentUser; //현재 사용자

    User loginUser; //현재 사용자

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //QR코드 버튼
    Button btnQR;
    //YOLO 버튼
    Button btnYOLO;
    //DrawerLayout버튼
    Button btnDrawer;

    Rent rent = null; //현재 대여하고 있는 장비
    List<Rent> rentData = new ArrayList<>(); //사용자의 대여이력을 담는 리스트

    RecyclerView recyclerView;
    static CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getUserInfo();
        getRentData();
        navigationListener();

        //버튼 리스너
        btnQR.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), QRActivity.class);
            intent.putExtra(Const.QR,Const.RENT);
            startActivity(intent);
            finish();


        });
        btnYOLO.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), DetectorActivity.class);
            startActivity(intent);
        });
        btnDrawer.setOnClickListener(view -> {

            drawerLayout.openDrawer(navigationView);

        });

        getRentInfo();
    }

    private void init() {

        //파이베이스 커넥션을 통해 DB, 로그인한 회원 정보를 가져온다
        mAuth = FirebaseAuth.getInstance(); //파이어베이스 커넥션 설정
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Const.FB_REFER);
        rentRef = database.getReference(Const.RENT_REFER);
        currentUser = mAuth.getCurrentUser();

        //DrawerLayout 연결
        drawerLayout = findViewById(R.id.drawerLayout);
        //NavigationView 연결
        navigationView = findViewById(R.id.navigationView);

        //버튼 연결
        btnQR = findViewById(R.id.btnQR);
        btnYOLO = findViewById(R.id.btnYOLO);
        btnDrawer = findViewById(R.id.btnDrawer);

        //RecyclerView 연결, 커스텀 어답터 연결, 데이터 세팅
        recyclerView = findViewById(R.id.recyclerView);


    }

    public void getUserInfo() {

        //현재 로그인한 회원 정보를 가져와 navigation header에 띄우는 메소드

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //오류

                Log.d("DEBUG_CODE", currentUser.getEmail());


                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    User user;
                    user = child.getValue(User.class);

                    if (user.email.equals(currentUser.getEmail())) {

                        loginUser = user;

                        //로그인이 성공하면 메인엑테비티에 navigationview header에 사용자에 대한 정보를 표시
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("DEBUG_CODE", "회원정보 가져오기 실패");
                Toast.makeText(getApplicationContext(), "회원정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void navigationListener() {

        //Navigation Menu Click Listener

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.account:

                    //회원정보
                    Toast.makeText(getApplicationContext(), "계정 버튼", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.setting:

                    //고장신고

                    Intent rentalIntent = new Intent(getApplicationContext(),ReportActivity.class);
                    rentalIntent.putExtra(Const.REPORT,Const.MAIN);
                    startActivity(rentalIntent);
                    break;

                case R.id.logout:

                    //로그아웃

                    //AlerDialog 추가

                    //현재 로그인한 회원은 로그아웃
                    mAuth.signOut();

                    //로그인 액티비티로 이동
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                    break;
            }

            //navigationView에 아이템을 눌렀으면 drawer가 닫힌다
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });
    }

    @Override
    public void onBackPressed() {

        //사용자가 뒤로가기 눌렀을때

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            //만일 drawer가 열려있다면 뒤로가기를 누르면 닫힌다

            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            //그냥 사용자가 뒤로가기를 누르면 뒤로간다
            super.onBackPressed();
        }

    }

    public void getRentInfo() {

        //1.대여한 물품의 정보를 가져옴 (이전 RentalActivity에서)

        //2.대여가 확정된 경우 대여한 물품의 데이터를 세팅

        Intent intent = getIntent();

        //RentalActivity에서 넘어온 Rent객체를 받는다
        if (intent.getSerializableExtra("Rent") != null) {

            rent = (Rent) intent.getSerializableExtra("Rent");

            if (rent.renting) {

                //이미 대여중이므로 대여 가 불가능, 토스트 메세지로 에러 처리
                Toast.makeText(getApplicationContext(), "이미 대여중입니다.", Toast.LENGTH_SHORT).show();

            } else {

                rent(); //대여 시작

                //정상적인 대여 토스트 메세지 출력
                Toast.makeText(getApplicationContext(), rent.object + " 대여가 시작되었습니다.", Toast.LENGTH_SHORT).show();


            }
        }
    }

    public void getRentData() {

//      사용자의 대여 이력을 가져오는 메소드


        rentRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                rentData.clear(); //리스트 초기화

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Rent rent;
                    rent = child.getValue(Rent.class);

                    if (rent.email.equals(mAuth.getCurrentUser().getEmail())) {

                        //현재 사용자와 렌트 이력들 중의 대여 사용자 이메일이 일치하면
                        rentData.add(rent);
                    }
                }

                //RecyclerView 설정
                customAdapter = new CustomAdapter(rentData);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("DEBUG_CODE", "대여정보 가져오기 실패");
                Toast.makeText(getApplicationContext(), "대여정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void rent() {

        //본격적으로 대여를 시작하는 메소드

        rent.email = mAuth.getCurrentUser().getEmail(); //현재 로그인한 사용자의 이메일

        //데이터 세팅
        //SimpleDateFormat으로 현재 시간을 년:월:일:시:분:초로 포맷 설정
        SimpleDateFormat sdf = new SimpleDateFormat("    yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        //현재 시간을 앞서 설정한 포맷으로 가져온다
        String currentTime = sdf.format(new Date());
        rent.startDate = currentTime; //현재 시간 삽입
        rent.renting = true; //현재 대여할 물품의 대여 여부상태를 "대여 중" 으로 바꿔준다

        //파이어베이스에 대여키로 대여 정보 삽입
        rent.rentKey = rentRef.push().getKey(); //대여키를 입력받는다
        rentRef.child(rent.rentKey).setValue(rent); //데이터 삽입


    }


}