package com.jc.jcsports.activities.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jc.jcsports.activities.filelistFunctions.BulletinFileFragment;
import com.jc.jcsports.activities.filelistFunctions.MyBulletinFragment;

public class Fragment_ViewPager2_Adapter extends FragmentStateAdapter {

    public int mCount;

    public Fragment_ViewPager2_Adapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        switch(index){
            case 0 :
                return BulletinFileFragment.newInstance("hello","BulletinFileFragment");
            case 1:
                return MyBulletinFragment.newInstance("hello", "myBulletinFragment");
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public int getRealPosition(int position) {
        return position;
    }

}