package com.demo.awesomeledger.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.fragment.DetailFragment;
import com.demo.awesomeledger.fragment.OverviewFragment;
import com.demo.awesomeledger.fragment.IncomeFragment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Integer year, month, day;
    private TextView monthTextView;
    private Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年\nMM月▼", Locale.CHINA);
    private SpannableString monthString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthTextView = findViewById(R.id.month);
        // 设置Fab
        FloatingActionButton fab = findViewById(R.id.add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra("isNew", true);
                startActivity(intent);
            }
        });
        // 设置月份, 收支
        setBriefInfo();
        // 设置Tab
        setTab();
    }

    // 设置中间信息栏的信息
    private void setBriefInfo() {
        TextView incomeTextView = findViewById(R.id.income);
        TextView outgoingTextView = findViewById(R.id.outgoing);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        //设置点击回调函数
        monthTextView.setClickable(true);
        monthTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMonth();
            }
        });

        calendar.setTime(new Date());
        // 设置月份显示
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        monthString = new SpannableString(dateFormat.format(calendar.getTime()));
        monthString.setSpan(new RelativeSizeSpan(3f), 5, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        monthTextView.setText(monthString);

        // 设置支出
        double outgoing = 1000.64;
        // TODO: 设置支出金额
        SpannableString outgoingString = new SpannableString("支出(元)\n\n" + numberFormat.format(outgoing));
        outgoingString.setSpan(new RelativeSizeSpan(1.5f), 5,
                outgoingString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        outgoingTextView.setText(outgoingString);

        // 设置收入
        double income = 960.1;
        // TODO: 设置收入金额
        SpannableString incomeString = new SpannableString("收入(元)\n\n" + numberFormat.format(income));
        incomeString.setSpan(new RelativeSizeSpan(1.5f), 5,
                incomeString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        incomeTextView.setText(incomeString);
    }

    private void setTab() {
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        TabLayout tabLayout = findViewById(R.id.main_tab);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new DetailFragment();
                } else if (position == 1){
                    return new OverviewFragment();
                }else {
                    return new IncomeFragment();
                }
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
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void changeMonth() {
        DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        //修改textView
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDayOfMonth;
                        calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        monthString = new SpannableString(dateFormat.format(calendar.getTime()));
                        monthString.setSpan(new RelativeSizeSpan(3f), 5, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        monthTextView.setText(monthString);
                        //修改列表内容
                    }
                }, year, month, day);
        dialog.show();
        //只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup)dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
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
