package com.example.projectmjurental.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rent implements Serializable {

    /*
    대여 물품 객체 (최상위 객체)
     */

    public String modelName; //모델명
    public String startDate; //대여 시작시간
    public String endDate; //대여 종료시간
    public int deposit; //보증금
    public String modelInfo; //제품 정보
    public boolean available = true; //대여 가능 여부
    public String rentKey; //렌트 키 (나중에 대여를 시작할때 발급)

    public Rent() {

        //파이어베이스 연동을 위한 기본 생성자 정의
    }

    public void rent(Date date) {

        //대여 시작

        available = false;//대여 가능여부를 false로 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        startDate = sdf.format(date);
    }

    //getters & setters

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public String getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(String modelInfo) {
        this.modelInfo = modelInfo;
    }

    //getters & setters
}
