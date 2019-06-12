package com.demo.awesomeledger.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.adapter.TabFragmentAdapter;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;
import com.demo.awesomeledger.fragment.DetailFragment;
import com.demo.awesomeledger.type.ItemType;

import com.vondear.rxtool.RxTool;
import com.vondear.rxtool.RxPermissionsTool;

import okhttp3.ResponseBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DetailFragment.OnDeleteListener {

    private Integer year, month, day;
    private TextView monthTextView;
    private TextView incomeTextView;
    private TextView outgoingTextView;
    private Calendar calendar = Calendar.getInstance();
    private Bundle bundle = new Bundle();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年\nMM月▼", Locale.CHINA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthTextView = findViewById(R.id.month);
        incomeTextView = findViewById(R.id.income);
        outgoingTextView = findViewById(R.id.outgoing);
        calendar.setTime(new Date());
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
        // 设置日期选择
        monthTextView.setClickable(true);
        monthTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMonth();
            }
        });
        initLocation();
        RxTool.init(this);
        requestRetrofit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        bundle.putLong("month", calendar.getTime().getTime());
        setBriefInfo();
        setTab();
    }

    private void setIncomeOrOutgoing(TextView textView, ItemType type) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        float totalMoney = 0f;
        List<Item> items = ItemDao.getInstance(this).getItemsOfMonth(calendar, type);
        if (items != null) {
            for (Item item: items) {
                totalMoney += item.getMoney();
            }
        }
        SpannableString outgoingString = new SpannableString(type.getType() + "(元)\n\n" + numberFormat.format(totalMoney));
        outgoingString.setSpan(new RelativeSizeSpan(1.5f), 5,
                outgoingString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(outgoingString);
    }

    // 设置中间信息栏的信息
    private void setBriefInfo() {
        SpannableString monthString = new SpannableString(dateFormat.format(calendar.getTime()));
        monthString.setSpan(new RelativeSizeSpan(3f), 5, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        monthTextView.setText(monthString);
        setIncomeOrOutgoing(outgoingTextView, ItemType.OUTGOING);
        setIncomeOrOutgoing(incomeTextView, ItemType.INCOME);
    }

    private void setTab() {
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        TabLayout tabLayout = findViewById(R.id.main_tab);
        viewPager.setAdapter(new TabFragmentAdapter(this, getSupportFragmentManager(), bundle));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void changeMonth() {
        DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDayOfMonth;
                        calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        bundle.putLong("month", calendar.getTime().getTime());
                        setBriefInfo();
                        setTab();
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

    @Override
    public void onDelete() {
        setBriefInfo();
        setTab();
    }
    private void initLocation(){
        RxPermissionsTool.
                with(this).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.LOCATION_HARDWARE).
                addPermission(Manifest.permission.INTERNET).
                addPermission(Manifest.permission.ACCESS_NETWORK_STATE).
                initPermission();
    }
    private void requestRetrofit(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").build();
        PersonalProtocol personalProtocol = retrofit.create(PersonalProtocol.class);
        Call<ResponseBody> call = personalProtocol.getInfo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = response.toString();
                Log.e("网络",s);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //数据请求失败
                Log.e("网络","失败");
                t.printStackTrace();
            }
        });
    }
    public interface PersonalProtocol {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @GET("sync")
        Call<ResponseBody> getInfo();
    }
}
