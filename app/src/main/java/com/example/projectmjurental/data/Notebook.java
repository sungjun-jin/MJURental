package com.example.projectmjurental.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notebook {

    //노트북 데이터 클래스

    public String modelName = "LG 그램"; //모델명
    public int deposit = 10000; //보증금
    public String modelInfo = "모델명 : " + this.modelName + "\n" + "보증금 : " + this.deposit + " 원\n" + "대여가능여부 : " + this.available + "\n";
    public String startDate; //대여 시작시간
    public String endDate; //대여 종료시간
    public boolean available = true; //대여 가능 여부


    public Notebook() {
        //파이어베이스 연동을 위한 기본 생성자 정의

    }

    public void rent(Date date) {

        //대여 시작
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        startDate = sdf.format(date);

    }


}
