package com.demo.awesomeledger.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.text.TextWatcher;
import android.widget.EditText;
import android.text.Editable;
import android.widget.*;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.util.ItemKind;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener{
    private Spinner spinnerType;
    private Spinner spinnerKind;
    private TextView tvDate;
    private EditText edittext;
    private List<String> type_list;
    private List<String> kind_list;
    private ArrayAdapter<String> arr_adapter;
    private ArrayAdapter<String> kind_adapter;
    private int year, month, day;
    private StringBuffer date;
    private Context context;
    private int amount;
    private int selectionStart;
    private int selectionEnd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        //设置ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_return_64);
        }

        date = new StringBuffer();
        context = this;
        initView();
        initDateTime();
    }

    //初始化控件
    private void initView(){
        tvDate = (TextView) findViewById(R.id.dateView);
        tvDate.setOnClickListener(this);

        //类别选择部分
        spinnerType = (Spinner) findViewById(R.id.type);
        type_list = new ArrayList<String>();
        type_list.add("支出");
        type_list.add("收入");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,type_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinnerType.setAdapter(arr_adapter);
        //绑定事件监听
        spinnerType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {  //选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //类别描述部分
        spinnerKind = (Spinner) findViewById(R.id.kind);
        kind_list = new ArrayList<String>();
        for (ItemKind itemKind: ItemKind.values()){
            kind_list.add(itemKind.getKind());
        }
        //适配器
        kind_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,kind_list);
        //设置样式
        kind_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinnerKind.setAdapter(kind_adapter);
        //绑定事件监听
        spinnerKind.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {  //选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        //总额输入部分
        edittext = (EditText) findViewById(R.id.editText);
        //设置监听器
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = edittext.getSelectionStart();
                selectionEnd = edittext.getSelectionEnd();
                if(!edittext.getText().toString().equals("")){
                    if (!isOnlyPointNumber(edittext.getText().toString())){
                        s.delete(selectionStart - 1, selectionEnd);
                        edittext.setText(s);
                        edittext.setSelection(s.length());
                    }
                }
            }
        });

    }
    public static boolean isOnlyPointNumber(String number) {//保留两位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(date.append(String.valueOf(month+1)).append("月").append(day).append("日"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateView:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        tvDate.setText(date.append(String.valueOf(month+1)).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month , day, this);
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 点击左上角返回键返回
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
