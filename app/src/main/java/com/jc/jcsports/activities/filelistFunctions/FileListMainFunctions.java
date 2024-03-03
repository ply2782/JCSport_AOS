package com.jc.jcsports.activities.filelistFunctions;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jc.jcsports.activities.adapter.Fragment_ViewPager2_Adapter;
import com.jc.jcsports.activities.filelistFunctions.fileCreateFunctions.FileCreateActivity;

public class FileListMainFunctions {

    private Context context;
    private FragmentActivity fragmentActivity;
    private ViewPager2 viewPager2;
    private Fragment_ViewPager2_Adapter fragment_viewPager2_adapter;
    private final int fragmentLength = 2;

    public FileListMainFunctions(
            Context context,
            FragmentActivity fragmentActivity,
            ViewPager2 viewPager2,
            TabLayout tabLayout
    ) {

        this.context = context;
        this.viewPager2 = viewPager2;
        this.fragmentActivity = fragmentActivity;

        adapterInit();
        viewPager2Init();
        createTabItem(tabLayout);
    }


    public void moveFileCreateActivity(){
        Intent intent = new Intent(context , FileCreateActivity.class);
        context.startActivity(intent);
    }


    //todo : 탭 아이템 만들기
    private void createTabItem(TabLayout tab_layout) {
        connectViewPagerWithTabLayout(tab_layout);
    }

    //todo : 탭 레이아웃과 뷰페이저2 연동
    private void connectViewPagerWithTabLayout(TabLayout tab_layout) {
        new TabLayoutMediator(
                tab_layout,
                viewPager2,
                (tab, position) -> {
                    String tabItemText = position == 0 ? "전체 게시물" : "나의 게시물";
                    tab.setText(tabItemText);
                }
        ).attach();
    }

    //todo : 뷰페이저2 어댑터 초기화
    private void adapterInit() {
        fragment_viewPager2_adapter = new Fragment_ViewPager2_Adapter(fragmentActivity, fragmentLength);
    }

    //todo : 뷰페이저2 초기화
    private void viewPager2Init() {
        viewPager2.setAdapter(fragment_viewPager2_adapter);
        viewPager2.setPageTransformer(new CompositePageTransformer());
        viewPager2.setOffscreenPageLimit(fragmentLength);
        viewPager2Listener();
    }

    //todo : 뷰페이저2 리스너
    private void viewPager2Listener() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {

                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

}
