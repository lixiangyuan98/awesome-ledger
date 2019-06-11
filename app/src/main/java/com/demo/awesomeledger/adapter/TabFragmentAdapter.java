package com.demo.awesomeledger.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.demo.awesomeledger.activity.MainActivity;
import com.demo.awesomeledger.fragment.DetailFragment;
import com.demo.awesomeledger.fragment.IncomeFragment;
import com.demo.awesomeledger.fragment.OutgoingFragment;

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private Bundle bundle;
    private Context context;

    public TabFragmentAdapter(Context context, FragmentManager fm, Bundle bundle) {
        super(fm);
        this.context = context;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new DetailFragment();
                ((DetailFragment)fragment).setOnDeleteListener((MainActivity)context);
                break;
            case 1:
                fragment = new OutgoingFragment();
                break;
            default:
                fragment = new IncomeFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "明细";
        } else if (position == 1) {
            return "支出";
        }else {
            return "收入";
        }
    }
}
