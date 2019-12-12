package com.example.projectmjurental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.projectmjurental.adapter.FragmentAdapter;
import com.example.projectmjurental.fragment.ImageFragment;

import java.util.ArrayList;

public class AppInfoActivity extends AppCompatActivity {

    FragmentAdapter appInfoFragmentAdapter;
    ViewPager appInfoViewPager;

    ArrayList<Integer> appInfolistImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        appInfoViewPager = findViewById(R.id.appInfoViewPager);
        setAppInfoImage();
    }

    private void setAppInfoImage() {

        {

//        Fragment에 대여 이미지 삽입

            appInfoFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

            //ViewPager와 FragmentAdapter 연결

            appInfoViewPager.setAdapter(appInfoFragmentAdapter);

            appInfolistImage.add(R.drawable.appinfo1);
            appInfolistImage.add(R.drawable.appinfo2);
            appInfolistImage.add(R.drawable.appinfo3);
            appInfolistImage.add(R.drawable.appinfo4);
            appInfolistImage.add(R.drawable.appinfo5);
            appInfolistImage.add(R.drawable.appinfo6);

            for (int i = 0; i < appInfolistImage.size(); i++) {

                ImageFragment imageFragment = new ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("imgRes", appInfolistImage.get(i));
                imageFragment.setArguments(bundle);
                appInfoFragmentAdapter.addItem(imageFragment);
            }
            appInfoFragmentAdapter.notifyDataSetChanged();
        }
    }
}
