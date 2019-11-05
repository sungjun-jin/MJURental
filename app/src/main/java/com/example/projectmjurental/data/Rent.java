package com.example.projectmjurental.data;

import java.io.Serializable;

public class Rent implements Serializable {

    /*
    대여 물품 객체 (최상위 객체)
     */
    public String rentKey; //렌트 키 (나중에 대여를 시작할때 발급)
    public String email; //대여한 사용자의 이메일
    public boolean renting = false; //true : 대여 중, false : 반납 완료

    public String object; //대여 물품 종류
    public String modelName; //모델명
    public String startDate; //대여 시작시간
    public String endDate; //대여 종료시간

    public int deposit; //보증금
    public String modelInfo; //제품 정보





    public Rent() {

        //파이어베이스 연동을 위한 기본 생성자 정의
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

    public String getRentKey() {
        return rentKey;
    }

    public void setRentKey(String rentKey) {
        this.rentKey = rentKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRenting() {
        return renting;
    }

    public void setRenting(boolean renting) {
        this.renting = renting;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    //getters & setters
}
