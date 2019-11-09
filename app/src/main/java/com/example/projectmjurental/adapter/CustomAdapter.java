package com.example.projectmjurental.adapter;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmjurental.R;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Rent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    List<Rent> data;

    public CustomAdapter(List<Rent> data) {


        //생성자로 데이터를 가져온다
        this.data = data;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        //실제적으로 View표시

        Rent rent = data.get(position);

        //이미지 뷰 세팅

        if (rent.object.equals(Const.notebook)) {

            //노트북
            holder.imageItem.setImageResource(R.drawable.icon_notebook); //리스트의 이미지뷰를 노트북 아이콘으로 변환

        } else if (rent.object.equals(Const.battery)) {

            //배터리
            holder.imageItem.setImageResource(R.drawable.icon_battery); //리스트의 이미지뷰를 배터리 아이콘으로 변환

        } else if (rent.object.equals(Const.calculator)) {

            //계산기
            holder.imageItem.setImageResource(R.drawable.icon_calculator); //리스트의 이미지뷰를 계산기 아이콘으로 변환
        }

        //이미지 뷰 세팅

        holder.textDate.setText(rent.startDate);


        //대여 여부 세팅

        if (rent.renting) {

            holder.textRent.setText("대여 중");
            holder.textRent.setTextColor(Color.parseColor("#26F736"));

            //대여 중일 때 때 사용 시간을 공백("")으로 표시한다
            holder.textRentTime.setText("");

        } else {

            holder.textRent.setText("반납 완료");
            holder.textRent.setTextColor(Color.parseColor("#EC3636"));

        }

        //대여 여부 세팅

        //사용 시간 세팅
        holder.textRentTime.setText(getRentTime(rent.startDate));



    }

    private String getRentTime(String startTime) {

        String result = "";

        SimpleDateFormat sdf = new SimpleDateFormat("    yyyy-MM-dd HH:mm:ss", Locale.KOREA);


        Date curDate = new Date();

        try {

            Date time = sdf.parse(startTime);

            Log.i("DEBUG_CODE", "시작 시간 : " + time);

            long cureDateTime = curDate.getTime();
            long reqDateTime = time.getTime();
            long diff = cureDateTime - reqDateTime;

            long hour = diff / 3600000;
            long min = (diff % 3600000) / 60000;

            result = hour + "시간\n" + min + "분";


        } catch (ParseException e) {

            Log.d("DEBUG_CODE", "getRentTime 에러 : " + e.getMessage());
        }


        //사용 시간을 구하는 메소드


        return result;

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        //아이템 레이아웃에서 사용되는 모든 요소를 미리 정의
        TextView textDate, textRentTime, textRent;
        Button btnRentInfo;
        ImageView imageItem;


        public Holder(@NonNull View itemView) {
            super(itemView);

            //생성자에서 findViewId로 각 요소들과 연결

            textDate = itemView.findViewById(R.id.textDate);
            textRentTime = itemView.findViewById(R.id.textRentTime);
            textRent = itemView.findViewById(R.id.textRent);

            btnRentInfo = itemView.findViewById(R.id.btnRentInfo);
            imageItem = itemView.findViewById(R.id.imageItem);
        }
    }
}
