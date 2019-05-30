package com.demo.awesomeledger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.fragment.DetailFragment;
import com.demo.awesomeledger.fragment.OverviewFragment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置Fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        // 设置月份, 收支
        setBriefInfo();

        // 设置Tab
        setTab();
    }

    /* 设置中间信息栏的信息 */
    private void setBriefInfo() {
        TextView monthTextView = (TextView)findViewById(R.id.month);
        TextView incomeTextView = (TextView)findViewById(R.id.income);
        TextView outgoingTextView = (TextView)findViewById(R.id.outgoing);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        // 设置月份显示
        SimpleDateFormat dateFormat = new SimpleDateFormat("y年\nM月▼", Locale.CHINA);
        SpannableString monthString = new SpannableString(dateFormat.format(new Date()));
        monthString.setSpan(new RelativeSizeSpan(3f), 5, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        monthTextView.setText(monthString);

        // 设置支出
        double outgoing = 1000.64;
        SpannableString outgoingString = new SpannableString("支出(元)\n\n" + numberFormat.format(outgoing));
        outgoingString.setSpan(new RelativeSizeSpan(1.5f), 5,
                outgoingString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        outgoingTextView.setText(outgoingString);

        // 设置收入
        double income = 960.1;
        SpannableString incomeString = new SpannableString("收入(元)\n\n" + numberFormat.format(income));
        incomeString.setSpan(new RelativeSizeSpan(1.5f), 5,
                incomeString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        incomeTextView.setText(incomeString);
    }

    private void setTab() {
        ViewPager viewPager = (ViewPager)findViewById(R.id.main_view_pager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.main_tab);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new DetailFragment();
                } else {
                    return new OverviewFragment();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "明细";
                } else {
                    return "报表";
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}
