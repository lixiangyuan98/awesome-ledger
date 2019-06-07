package com.demo.awesomeledger.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;
import com.demo.awesomeledger.util.ItemKind;
import com.demo.awesomeledger.util.ItemType;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private TextView tvDate;
    private TextView locationView;
    private EditText noteView;
    private EditText editText;
    private ItemDao itemDao;
    private Item item;
    public int year, month, day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        itemDao = ItemDao.getInstance(AddItemActivity.this);
        if (getIntent().getBooleanExtra("isNew", true)) {
            item = new Item();
        } else {
            item = itemDao.get(getIntent().getIntExtra("id", -1));
            if (item == null) {
                item = new Item();
            }
        }
        //设置ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_return_64);
        }
        initView();
        initDateTime();
        initFAB();
        initPosition();
    }

    //初始化控件
    private void initView() {
        tvDate = findViewById(R.id.dateView);
        tvDate.setOnClickListener(this);
        noteView = findViewById(R.id.note);
        //类别选择部分
        Spinner spinnerType = findViewById(R.id.type);

        List<String> typeList = new ArrayList<>();
        typeList.add(ItemType.INCOME.getType());
        typeList.add(ItemType.OUTGOING.getType());
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(AddItemActivity.this,
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        if(!getIntent().getBooleanExtra("isNew", true)){
            spinnerType.setSelection(item.getItemType().ordinal());
        }
        //绑定事件监听
        spinnerType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        item.setItemType(ItemType.OUTGOING);
                        break;
                    case 1:
                        item.setItemType(ItemType.INCOME);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //类别描述部分
        Spinner spinnerKind = findViewById(R.id.kind);
        List<String> kindList = new ArrayList<>();
        for (ItemKind itemKind : ItemKind.values()) {
            kindList.add(itemKind.getKind());
        }
        ArrayAdapter<String> kindAdapter = new ArrayAdapter<>(AddItemActivity.this,
                android.R.layout.simple_spinner_item, kindList);
        kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKind.setAdapter(kindAdapter);
        if(!getIntent().getBooleanExtra("isNew", true)){
            spinnerKind.setSelection(item.getItemKind().ordinal());
        }
        spinnerKind.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (ItemKind itemKind : ItemKind.values()) {
                    if (itemKind.ordinal() == position) {
                        item.setItemKind(itemKind);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //总额输入部分
        editText = findViewById(R.id.editText);
        if(!getIntent().getBooleanExtra("isNew", true)){
            NumberFormat nf = NumberFormat.getInstance();
            //设置保留多少位小数
            nf.setMaximumFractionDigits(2);
            // 取消科学计数法
            nf.setGroupingUsed(false);
            editText.setText(nf.format(item.getMoney()));
        }
        //设置监听器
        editText.addTextChangedListener(new TextWatcher() {
            //    Tips:
            //    1. onTextChanged和beforTextChanged传入的参数s其实是当前EditText的文字内容，而不是当前输入的内容
            //    2. 如果在任意一个方法中调用了设置当前EditText文本的方法，setText()，实际都触发了一遍这3个函数，
            //       所以要有判断条件，在if体内去setText，而且就需要手动设置光标的位置，不然每次光标都会到最开始的位置
            //    3. onTextChanged中，before=0： 增加；before=1： 点击删除按键

            private int count_decimal_points_ = 0;  // 标识当前是不是已经有小数点了
            private int selection_start_;  			// 监听光标的位置
            private StringBuffer str_buf_;			// 缓存当前的string，用以修改内容
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                str_buf_ = new StringBuffer(s.toString().trim());
                // 先判断输入的第一位不能是小数点
                if (before == 0 && s.length() == 1 && s.charAt(start) == '.') {
                    editText.setText("");
                } else if (before == 0 && count_decimal_points_ == 1) {
                    // 在判断如果当前是增加，并且已经有小数点了，就要判断输入是否合法；如果是减少不做任何判断
                    // 注意在if语句中都是在else体内调用了设置光标监听位的方法，因为在调用setText之后会出现嵌套的情况
                    // 非合法的输入包括： 1. 输入的依旧是小数点，2.小数点后位数已经达到两位了
                    if (s.charAt(start) == '.' ||  (start - str_buf_.indexOf(".") > 2) ) {
                        str_buf_.deleteCharAt(start);
                        editText.setText(str_buf_);
                    } else {
                        selection_start_ = str_buf_.length();		// 设置光标的位置为结尾
                    }
                } else {
                    selection_start_ = str_buf_.length();		// 设置光标的位置为结尾
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (s.toString().contains(".")) {
                    count_decimal_points_ = 1;
                } else {
                    count_decimal_points_ = 0;	// 因为可能存在如果是删除的话，把小数点删除的情况
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 重置光标位置
                if (s != null) {
                    try {
                        editText.setSelection(s.length());
                        if(s.toString() != null){
                            item.setMoney(Double.parseDouble(s.toString()));
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    //保留两位小数正则
    private static boolean isOnlyPointNumber(String number) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        if(!getIntent().getBooleanExtra("isNew", true)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
            calendar.setTime(item.getDate());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);
            tvDate.setText(dateFormat.format(item.getDate()));
        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);
            tvDate.setText(dateFormat.format(calendar.getTime()));
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dateView){
            changeMonth();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        item.setDate(calendar.getTime());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 点击左上角返回键返回
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFAB() {
        //保存按钮
        FloatingActionButton saveFab = findViewById(R.id.saveBtn);
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setComment(noteView.getText().toString());
                // 金额不为0则保存
                if (item.getMoney() != 0) {
                    itemDao.insert(item);
                }
                //itemDao.insert(item);
                finish();
            }
        });
        //取消按钮
        FloatingActionButton cancelFab = findViewById(R.id.cancelBtn);
        cancelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initPosition() {
        locationView = findViewById(R.id.location);
        if(!getIntent().getBooleanExtra("isNew", true)){
            locationView.setText(item.getAddress());
        }
        else {
            //声明AMapLocationClient类对象
            AMapLocationClient mLocationClient;
            //声明定位回调监听器
            //可在其中解析amapLocation获取相应内容。
            //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            //声明定位回调监听器
            AMapLocationListener mLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (amapLocation != null) {
                        if (amapLocation.getErrorCode() == 0) {
                            //可在其中解析amapLocation获取相应内容。
                            //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                            String address = amapLocation.getAddress();
                            locationView.setText(address);
                            item.setAddress(address);
                        } else {
                            Log.e("location", amapLocation.getErrorInfo());
                            item.setAddress("");
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        }
                    }
                }
            };
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);

            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }
    private void changeMonth() {
        DatePickerDialog dialog = new DatePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        //修改textView
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDayOfMonth;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
                        tvDate.setText(dateFormat.format(calendar.getTime()));
                        item.setDate(calendar.getTime());
                    }
                }, year, month, day);
        dialog.show();
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
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
