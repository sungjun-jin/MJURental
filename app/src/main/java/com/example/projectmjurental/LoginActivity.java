package com.example.projectmjurental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmjurental.user.JoinActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; //파이어베이스 연결

    private EditText join_email;
    private EditText join_passwd;
    private Button btnSignUp; //회원가입
    private Button btnLogin;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        join_email = findViewById(R.id.join_email);
        join_passwd = findViewById(R.id.join_passwd);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance(); //파이어베이스 커넥션 설정

        join_email.setText(""); //이메일을 보여주는 EditText를 초기화
        join_passwd.setText(""); //비밀번호를 보여주는 EditText를 초기화


        btnSignUp.setOnClickListener(v -> {

            //회원가입 버튼을 눌렀을 시


            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);

            // SINGLE_TOP : 이미 만들어진게 있으면 그걸 쓰고, 없으면 만들어서 씀
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            //회원가입 액티비티로 이동한다
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {

            //로그인 버튼 클릭 시

            if (join_email.getText().toString().length() == 0) {

                //이메일을 입력하지 않았을 때

                Toast.makeText(LoginActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                join_email.requestFocus();
                return;
            }

            if (join_passwd.getText().toString().length() == 0) {

                //비밀번호를 입력하지 않았을 때

                Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                join_passwd.requestFocus();
                return;

            }

            String email = join_email.getText().toString();
            String passwd = join_passwd.getText().toString();



            signinProcess(email, passwd);


        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

    }

    private void signinProcess(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {


                currentUser = mAuth.getCurrentUser(); //sign-in된 user의 데이터가 들어감

                if (currentUser != null) {

                    // 로그인 성공 시

                    Toast.makeText(LoginActivity.this, "로그인 되었습니다!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    //1. 이후 액티비티를 호출
                    finish();

                }

            } else {

                Toast.makeText(LoginActivity.this, "Email또는 Password가 잘못되었습니다.", Toast.LENGTH_SHORT).show();

            }

        });


    }
}

