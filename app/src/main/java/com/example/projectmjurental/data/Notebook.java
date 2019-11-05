package com.example.projectmjurental.data;

public class Notebook extends Rent {

    //노트북 데이터 클래스

    public Notebook() {
        //파이어베이스 연동을 위한 기본 생성자 정의

    }

    public void setValue() {

        modelName = "LG 그램"; //모델명
        deposit = 10000; //보증금
        modelInfo = "모델명 : " + this.modelName + "\n" + "보증금 : " + this.deposit + " 원\n" + "대여중 : " + renting + "\n";

    }




}
