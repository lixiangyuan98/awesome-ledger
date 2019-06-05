package com.demo.awesomeledger.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.fragment.DetailFragment;
import com.demo.awesomeledger.fragment.OverviewFragment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private int year, month, day;
    Calendar calendar = Calendar.getInstance();
    private TextView monthTextView;
    private TextView incomeTextView;
    private TextView outgoingTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
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
        monthTextView = (TextView)findViewById(R.id.month);
        incomeTextView = (TextView)findViewById(R.id.income);
        outgoingTextView = (TextView)findViewById(R.id.outgoing);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        //设置点击回调函数
        monthTextView.setClickable(true);
        monthTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0){
                changeMonth();
            }
        });
        // 设置月份显示
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
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

    public void changeMonth(){
        Log.d("点击","月份");
        DatePickerDialog dialog = new DatePickerDialog(context,AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int Year, int Month, int dayOfMonth) {
                year = Year;
                month = Month;
                day = dayOfMonth;
                final String data =  year+"年"+(month+1) + "月";
                //修改textView

                //修改列表内容
            }
        }, year, month, day);
        dialog.show();
        //只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup)((ViewGroup)dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(2).setVisibility(View.GONE);
            //如果想隐藏掉年，将getChildAt(2)改为getChildAt(0)
        }
    }
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }


}
