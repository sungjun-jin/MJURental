package com.example.projectmjurental.user;

/*
사용자의 정보를 담는 데이터 클래스
 */

public class User {

    public String email; //이메일
    public String password; //비밀번호 -> 현재상태는 번호
    public String name; //이름
    public String dept; //전공
    public String num; //학번
    public boolean admin; //관리자 여부
    public boolean rent; //보증금 입금 여부

    public User() {

        //파이어베이스 연동을 위한 기본 생성자 정의

    }

    public User(String email, String password, String name, String dept, String num) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.dept = dept;
        this.num = num;
        admin = false;
        rent = false;


    }
}
