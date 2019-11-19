package com.example.projectmjurental.data;

public class Report {

    //고장신고 데이터 클래스

    //파이어베이스를 위한 기본 생성자
    public Report() {}


    public String userEmail; //사용자 이메일
    public String userPhoneNum; //사용자 연락처
    public String modelName; //대여 물품
    public String modelInfo; //대여 물품 모델명
    public String reportInfo; //고장 내용

    public String reportDate; //접수 시간

    public Report (String userEmail, String userPhoneNum, String modelName, String modelInfo, String reportInfo, String reportDate) {

        this.userEmail = userEmail;
        this.userPhoneNum = userPhoneNum;
        this.modelName = modelName;
        this.modelInfo = modelInfo;
        this.reportInfo = reportInfo;
        this.reportDate = reportDate;
    }
}
