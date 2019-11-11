package com.example.projectmjurental.adapter;


import android.content.Intent;
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
import com.example.projectmjurental.ReturnActivity;
import com.example.projectmjurental.data.Const;
import com.example.projectmjurental.data.Rent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    public List<Rent> data;

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
            //사용 시간 세팅 -> 추후 반납함수에서 사용 예정
            holder.textRentTime.setText(getRentTime(rent.startDate,rent.endDate));


        }

        //대여 여부 세팅




        holder.btnRentInfo.setOnClickListener(view -> {

            //대여 세부정보 버튼 클릭 시 -> 반납 액티비티로 이동한다 (RentalActivity)

            Intent intent = new Intent(view.getContext(), ReturnActivity.class);
            intent.putExtra("Rent", rent); //현재 대여하고 있는 물품 클래스를 넘겨준다
            intent.putExtra("index",position); //현재 대여하고 있는  물품 클래스의 데이터 인덱스를 넘겨준다
            view.getContext().startActivity(intent);

        });



    }

    private String getRentTime(String startTime, String endTime) {

        //반납 시작 시간과 종료 시간을 인수로 받는다

        //startTime은 rent 클래스에 있던 String 형 변수를 가지고 Date 클래스를 이용해 Date형으로 다시 변환해준다

        String result = "";

        SimpleDateFormat sdf = new SimpleDateFormat("    yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        try {

            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            long cureDateTime = endDate.getTime();
            long reqDateTime = startDate.getTime();
            long diff = cureDateTime - reqDateTime;

            long hour = diff / 3600000;
            long min = (diff % 3600000) / 60000;

            result = hour + "시간 " + min + "분";


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
