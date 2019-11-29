package com.example.projectmjurental.data;

/*
사용자의 정보를 담는 데이터 클래스
 */

public class User {

    public String email; //이메일
    String password; //비밀번호 -> 현재상태는 번호
    String name; //이름
    String dept; //전공
    public String num; //학번
    boolean admin; //관리자 여부
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

    public boolean isRent() {
        return rent;
    }

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
