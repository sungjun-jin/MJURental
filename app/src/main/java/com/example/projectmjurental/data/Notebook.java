package com.example.projectmjurental.data;

public class Notebook extends Rent {

    //노트북 데이터 클래스

    public Notebook() {
        //파이어베이스 연동을 위한 기본 생성자 정의

    }

    public void setValue() {

        modelName = "LG 그램"; //모델명
        deposit = 10000; //보증금

        if (renting) {

            //대여 중

            modelInfo = "모델명 : " + this.modelName + "\n" + "보증금 : " + this.deposit + " 원\n" + "대여 가능 여부 : 불가능\n";

        } else {

            //대여 가능

            modelInfo = "모델명 : " + this.modelName + "\n" + "보증금 : " + this.deposit + " 원\n" + "대여 가능 여부 : 가능\n";

        }


    }


}
