package com.demo.awesomeledger.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;
import com.demo.awesomeledger.type.ItemKind;
import com.demo.awesomeledger.type.ItemType;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private TextView tvDate;
    private TextView locationView;
    private EditText noteView;
    private EditText editText;
    private ItemDao itemDao;
    private Item item;
    private Integer year;
    private Integer month;
    private Integer day;
    private boolean isNew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        itemDao = ItemDao.getInstance(AddItemActivity.this);
        isNew = getIntent().getBooleanExtra("isNew", true);
        if (isNew) {
            item = new Item();
        } else {
            item = itemDao.get(getIntent().getIntExtra("id", -1));
            if (item == null) {
                item = new Item();
            }
        }
        // 设置ActionBar
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

    // 初始化控件
    private void initView() {
        tvDate = findViewById(R.id.dateView);
        tvDate.setOnClickListener(this);
        noteView = findViewById(R.id.note);
        editText = findViewById(R.id.editText);
        Spinner spinnerType = findViewById(R.id.type);
        List<String> typeList = new ArrayList<>();
        typeList.add(ItemType.OUTGOING.getType());
        typeList.add(ItemType.INCOME.getType());
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(AddItemActivity.this,
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // 绑定事件监听
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

        // 类别描述部分
        Spinner spinnerKind = findViewById(R.id.kind);
        List<String> kindList = new ArrayList<>();
        for (ItemKind itemKind : ItemKind.values()) {
            kindList.add(itemKind.getKind());
        }
        ArrayAdapter<String> kindAdapter = new ArrayAdapter<>(AddItemActivity.this,
                android.R.layout.simple_spinner_item, kindList);
        kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKind.setAdapter(kindAdapter);

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

        // 设置监听器
        editText.addTextChangedListener(new TextWatcher() {

            private int countDecimalPoints = 0;  // 标识当前是不是已经有小数点了
            private int selectionStart;            // 监听光标的位置
            private StringBuffer stringBuffer;            // 缓存当前的string，用以修改内容

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                stringBuffer = new StringBuffer(s.toString().trim());
                // 先判断输入的第一位不能是小数点
                if (before == 0 && s.length() == 1 && s.charAt(start) == '.') {
                    editText.setText("");
                } else if (before == 0 && countDecimalPoints == 1) {
                    // 在判断如果当前是增加，并且已经有小数点了，就要判断输入是否合法；如果是减少不做任何判断
                    // 注意在if语句中都是在else体内调用了设置光标监听位的方法，因为在调用setText之后会出现嵌套的情况
                    // 非合法的输入包括： 1. 输入的依旧是小数点，2.小数点后位数已经达到两位了
                    if (s.charAt(start) == '.' || (start - stringBuffer.indexOf(".") > 2)) {
                        stringBuffer.deleteCharAt(start);
                        editText.setText(stringBuffer);
                    } else {
                        selectionStart = stringBuffer.length();        // 设置光标的位置为结尾
                    }
                } else {
                    selectionStart = stringBuffer.length();        // 设置光标的位置为结尾
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (s.toString().contains(".")) {
                    countDecimalPoints = 1;
                } else {
                    countDecimalPoints = 0;    // 因为可能存在如果是删除的话，把小数点删除的情况
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 重置光标位置
                if (s != null) {
                    try {
                        editText.setSelection(s.length());
                        item.setMoney(Double.parseDouble(s.toString()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 对非新建的条目的处理
        if (!isNew) {
            spinnerType.setSelection(item.getItemType().ordinal());
            spinnerKind.setSelection(item.getItemKind().ordinal());
            noteView.setText(item.getComment());
            //总额输入部分
            NumberFormat nf = NumberFormat.getInstance();
            //设置保留多少位小数
            nf.setMaximumFractionDigits(2);
            // 取消科学计数法
            nf.setGroupingUsed(false);
            editText.setText(nf.format(item.getMoney()));
        }
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        if (!isNew) {
            calendar.setTime(item.getDate());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        tvDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dateView) {
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
                    if (isNew) {
                        itemDao.insert(item);
                    } else {
                        itemDao.update(item);
                    }
                }
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
        if (!isNew) {
            locationView.setText(item.getAddress());
        } else {
            AMapLocationClient mLocationClient;
            mLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (amapLocation != null) {
                        if (amapLocation.getErrorCode() == 0) {
                            String address = amapLocation.getAddress();
                            locationView.setText(address);
                            item.setAddress(address);
                        } else {
                            Log.e("location", amapLocation.getErrorInfo());
                            item.setAddress("");
                        }
                    }
                }
            });
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
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
        DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
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
    }
}
