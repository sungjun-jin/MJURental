package com.example.projectmjurental.data;

public class Calculator extends Rent {

    //계산기 데이터 클래스


    public Calculator() {
        //파이어베이스 연동을 위한 기본 생성자 정의

    }

    public void setValue() {

        modelName = "FX-30ES PLUS"; //모델명
        deposit = 5000; //보증금

        if (renting) {

            //대여 중

            modelInfo = "모델명 : " + this.modelName + "\n\n" + "보증금 : " + this.deposit + " 원\n\n" + "대여 가능 여부 : 불가능\n";

        } else {

            //대여 가능

            modelInfo = "모델명 : " + this.modelName + "\n\n" + "보증금 : " + this.deposit + " 원\n\n" + "대여 가능 여부 : 가능\n";

        }


    }


}
