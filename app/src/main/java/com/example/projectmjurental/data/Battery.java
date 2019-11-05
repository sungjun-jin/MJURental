package com.example.projectmjurental.data;

public class Battery extends Rent {

    //배터리 데이터 클래스


    public Battery() {
        //파이어베이스 연동을 위한 기본 생성자 정의
    }

    public void setValue() {

        modelName = "샤오미"; //모델명
        deposit = 10000; //보증금
        modelInfo = "모델명 : " + this.modelName + "\n" + "보증금 : " + this.deposit + " 원\n" + "대여중 : " + renting + "\n";

    }
}
