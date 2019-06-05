package com.demo.awesomeledger;

import android.widget.TextView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import android.util.Log;



public class MyLocationListener extends BDAbstractLocationListener {
    private String locationDescribe;
    private TextView locationView;
    private static final String TAG = "MyActivity";
    public MyLocationListener(TextView textView){
        this.locationView = textView;
        Log.d(TAG, "currentX=" + "11");
    }
    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取位置描述信息相关的结果
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        locationDescribe = location.getLocationDescribe();
        Log.d(TAG, "currentX=" + locationDescribe);//获取位置描述信息
        locationView.setText(locationDescribe);
    }


}
