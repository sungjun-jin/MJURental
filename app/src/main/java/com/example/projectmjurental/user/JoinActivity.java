package com.example.projectmjurental.user;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmjurental.R;
import com.example.projectmjurental.data.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
회원가입 Activity
 */


public class JoinActivity extends AppCompatActivity {



    private EditText join_id; // 학번
    private EditText join_passwd; // 비밀번호
    private EditText join_passwd_re; // 비밀번호 재확인
    private EditText join_name; // 이름
    private EditText join_major; // 전공
    private EditText join_email; // 이메일
    private Button join_button; // 회원가입
    private Button esc_join_button; // 취소

    private FirebaseAuth mAuth; //파이어베이스 연결

    FirebaseDatabase database; //파이어베이스 데이터베이스
    DatabaseReference userRef; //레퍼런스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_id = findViewById(R.id.join_id);
        join_passwd = findViewById(R.id.join_passwd);
        join_passwd_re = findViewById(R.id.join_passwd_re);
        join_name = findViewById(R.id.join_name);
        join_email = findViewById(R.id.join_email);
        join_major = findViewById(R.id.join_major);
        join_button = findViewById(R.id.join_button);
        esc_join_button = findViewById(R.id.esc_join_button);

        mAuth = FirebaseAuth.getInstance(); //파이어베이스 커넥션 설정

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Const.FB_REFER);


        // 비밀번호 일치 검사
        join_passwd_re.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String password = join_passwd.getText().toString();
                String passwore_re = join_passwd_re.getText().toString();

                if (password.equals(passwore_re)) {
                    join_passwd.setBackgroundColor(Color.GREEN);
                    join_passwd_re.setBackgroundColor(Color.GREEN);
                } else {
                    join_passwd.setBackgroundColor(Color.RED);
                    join_passwd_re.setBackgroundColor(Color.RED);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 회원가입 버튼
        join_button.setOnClickListener(v -> {

            String email; //이메일
            String password; //패스워드
            String dept; //전공
            String name; //이름
            String id; //학번


            // 학번 입력확인
            if (join_id.getText().toString().length() == 0) {
                Toast.makeText(JoinActivity.this, "학번을 입력하세요!", Toast.LENGTH_SHORT).show();
                join_id.requestFocus();
                return;
            }

            // 비밀번호 입력확인
            if (join_passwd.getText().toString().length() == 0) {
                Toast.makeText(JoinActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                join_passwd.requestFocus();
                return;
            }

            // 비밀번호 재확인 입력확인
            if (join_passwd_re.getText().toString().length() == 0) {
                Toast.makeText(JoinActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                join_passwd_re.requestFocus();
                return;
            }

            // 비밀번호 일치 확인
            if (!join_passwd.getText().toString().equals(join_passwd_re.getText().toString())) {
                Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                join_passwd.setText("");
                join_passwd_re.setText("");
                join_passwd.requestFocus();
                return;
            }

            // 이름 입력확인
            if (join_name.getText().toString().length() == 0) {
                Toast.makeText(JoinActivity.this, "이름을 입력하세요!", Toast.LENGTH_SHORT).show();
                join_name.requestFocus();
                return;
            }

            // 전공 입력확인
            if (join_major.getText().toString().length() == 0) {
                Toast.makeText(JoinActivity.this, "전공을 입력하세요!", Toast.LENGTH_SHORT).show();
                join_major.requestFocus();
                return;
            }

            // 이메일 입력확인
            if (join_email.getText().toString().length() == 0) {
                Toast.makeText(JoinActivity.this, "전공을 입력하세요!", Toast.LENGTH_SHORT).show();
                join_email.requestFocus();
                return;
            }

            if (join_passwd.getText().toString().length() <= 6) {

                //비밀번호 길이 테스트

                Toast.makeText(JoinActivity.this, "비밀번호는 길이가 6자리 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
                join_email.requestFocus();
                return;
            }

            email = join_email.getText().toString(); //이메일
            password = join_passwd.getText().toString(); //패스워드
            dept = join_major.getText().toString(); //전공
            name = join_name.getText().toString(); //이름
            id = join_id.getText().toString(); //학번

            User user = new User(email, password, name, dept, id); //사용자 객체 생성, 초기화

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.d("DEBUG_CODE", "회원가입 데이터베이스 가입");


                    //테스트 로그
                    Log.d("DEBUG_CODE", "이메일 : " + user.email);
                    Log.d("DEBUG_CODE", "비밀번호 : " + user.password);
                    Log.d("DEBUG_CODE", "이름 : " + user.name);
                    Log.d("DEBUG_CODE", "학과 : " + user.dept);
                    Log.d("DEBUG_CODE", "학번 : " + user.num);
                    //테스트 로그

                    String key = user.num; //키를 학번으로 설정
                    userRef.child(key).setValue(user);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            signUP(email, password); //회원가입 메소드 실행
        });

        //회원가입 취소 버튼을 눌렀을 시
        esc_join_button.setOnClickListener(v -> finish());
    }

    private void signUP(String email, String password) {

        //회원가입 메소드

        //이메일과 패스워드를 넘김
        //네트워크에 있는 파이어베이스 컨트롤러로 가서 이메일이 있는지 없는지 확인

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        //회원가입 성공

                        Toast.makeText(JoinActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        //사용자 로그아웃 : 회원가입 성공시 자동적으로 로그인이 되어있기 때문에 로그인 창이 스킵된다 그러므로 한번 더 로그아웃 해준다
                        mAuth.signOut();
                        finish(); //액티비티 종료
                    } else {

                        //회원가입 실패, 토스트 메세지 출력

                        Toast.makeText(JoinActivity.this, "회원가입이 실패하였습니다.", Toast.LENGTH_SHORT).show();


                    }

                });


    }

}

