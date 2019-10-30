package com.example.projectmjurental;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.projectmjurental.data.Rent;
import com.example.projectmjurental.user.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.demo.DetectorActivity;

import static com.example.projectmjurental.user.JoinActivity.FB_REFER;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth; //파이어베이스 연결
    FirebaseDatabase database; //파이어베이스 데이터베이스
    DatabaseReference userRef; //레퍼런스

    User currentUser; //현재 사용자

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //QR코드 버튼
    Button btnQR;
    //YOLO 버튼
    Button btnYOLO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getUserInfo();
        navigationListener();

        btnQR.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), QRActivity.class);
            startActivity(intent);
        });

        btnYOLO.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), DetectorActivity.class);
            startActivity(intent);
        });

        showRentInfo();
    }

    private void init() {

        //파이베이스 커넥션을 통해 DB, 로그인한 회원 정보를 가져온다
        mAuth = FirebaseAuth.getInstance(); //파이어베이스 커넥션 설정
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(FB_REFER);

        //DrawerLayout 연결
        drawerLayout = findViewById(R.id.drawerLayout);

        //NavigationView 연결
        navigationView = findViewById(R.id.navigationView);

        //버튼 연결
        btnQR = findViewById(R.id.btnQR);
        btnYOLO = findViewById(R.id.btnYOLO);
    }

    public void getUserInfo() {

        //현재 로그인한 회원 정보를 가져오는 메소드
        Log.i("DEBUG_CODE", "-----------------------회원 정보-----------------------");
        Log.i("DEBUG_CODE", "회원 이메일 : " + mAuth.getCurrentUser().getEmail());

        //현재 로그인한 회원의 이메일을 가져온다

        String email;
        email = mAuth.getCurrentUser().getEmail();

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    User user;
                    user = child.getValue(User.class);

                    if (user.email.equals(email)) {

                        currentUser = user;


                        //로그인이 성공하면 메인엑테비티에 navigationview header에 사용자에 대한 정보를 표시
                        TextView textName = findViewById(R.id.textName);
                        textName.setText(user.name);

                        TextView textDept = findViewById(R.id.textDept);
                        textDept.setText(user.dept);

                        TextView textNum = findViewById(R.id.textNum);
                        textNum.setText(user.num);

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

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.account:

                    //회원정보
                    Toast.makeText(getApplicationContext(), "계정 버튼", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.setting:

                    //고장신고
                    Toast.makeText(getApplicationContext(), "고장신고 버튼", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.logout:

                    //로그아웃
                    //현재 로그인한 회원은 로그아웃
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
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

    public void showRentInfo() {

        //대여한 물품의 정보를 가져옴

        Intent intent = getIntent();


        if (intent.getSerializableExtra("Rent") != null) {

            Rent rent = (Rent) intent.getSerializableExtra("Rent");

            //로그 테스트
            Log.i("DEBUG_CODE", "메인 액티비티 : " + rent.getModelName());
            Log.i("DEBUG_CODE", "메인 액티비티 : " + rent.getModelInfo());
            Log.i("DEBUG_CODE", "메인 액티비티 : " + rent.getDeposit() + "");
            Log.i("DEBUG_CODE", "메인 액티비티 : " + rent.available + "");
            //로그 테스트
        }


    }


}