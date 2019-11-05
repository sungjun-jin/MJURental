package com.example.projectmjurental.adapter;

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

import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    List<Rent> data;

    public CustomAdapter(List<Rent> data) {

        Log.i("DEBUG_CODE", "custom adapter 생성자");

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

        if(rent.object.equals(Const.notebook)) {

            //노트북
            holder.imageItem.setImageResource(R.drawable.icon_notebook); //리스트의 이미지뷰를 노트북 아이콘으로 변환

        } else if(rent.object.equals(Const.battery)) {

            //배터리
            holder.imageItem.setImageResource(R.drawable.icon_battery); //리스트의 이미지뷰를 배터리 아이콘으로 변환

        } else if(rent.object.equals(Const.calculator)) {

            //계산기
            holder.imageItem.setImageResource(R.drawable.icon_calculator); //리스트의 이미지뷰를 계산기 아이콘으로 변환
        }

        //이미지 뷰 세팅

        holder.textDate.setText(rent.startDate);



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
