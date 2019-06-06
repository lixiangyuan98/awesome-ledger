package com.demo.awesomeledger.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.demo.awesomeledger.util.ItemKind;
import com.demo.awesomeledger.util.ItemType;

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
    private int selectionStart;
    private int selectionEnd;
    private ItemDao itemDao;
    private Item item;

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
        typeList.add(ItemType.OUTGOING.getType());
        typeList.add(ItemType.INCOME.getType());
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(AddItemActivity.this,
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
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
        editText.setText("0");
        editText.setSelection(1);
        //设置监听器
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // fixme: 删除原始0导致崩溃
                selectionStart = editText.getSelectionStart();
                selectionEnd = editText.getSelectionEnd();
                if (!editText.getText().toString().equals("")) {
                    if (!isOnlyPointNumber(editText.getText().toString())) {
                        s.delete(selectionStart - 1, selectionEnd);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                // 设置金额
                item.setMoney(Double.parseDouble(editText.getText().toString()));
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        tvDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dateView) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
                    tvDate.setText(dateFormat.format(calendar.getTime()));
                    item.setDate(calendar.getTime());
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
            View dialogView = View.inflate(AddItemActivity.this, R.layout.dialog_date, null);
            final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

            dialog.setTitle("设置日期");
            dialog.setView(dialogView);
            dialog.show();
            //初始化日期监听事件
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(item.getDate());
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE), this);
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
