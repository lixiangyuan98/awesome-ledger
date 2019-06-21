package com.demo.awesomeledger.sync;

import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import com.demo.awesomeledger.BuildConfig;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import com.demo.awesomeledger.gson.SyncRequest;
import com.demo.awesomeledger.gson.SyncResponse;
import com.demo.awesomeledger.gson.Insertbean;
import com.demo.awesomeledger.gson.Updatebean;
import com.demo.awesomeledger.gson.Errorbean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class sync {

    public Context context;
    public SyncResponse syncresponse;
    private Retrofit retrofit;

    public sync(Context context){
        this.context = context;
    }
    //初始化http请求属性
    private void initHttp(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //设置日志Level
        if (BuildConfig.DEBUG) {
            // development build
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            // production build
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                //添加拦截器到OkHttp，这是最关键的
                .addInterceptor(logging);

        Log.w("tan","postBodyRequest");
        Gson gson=new Gson();
        //OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.128.222.189:8080/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public void requestSync(){
        initHttp();
        //创建网络请求接口的实例
        Sync sync = retrofit.create(Sync.class);
        // TODo
        // syncResponse为响应实体类，用来接受回复数据，以下为接口调用
        Call<SyncResponse> call = sync.request(getSyncRequest());
        call.enqueue(new Callback<SyncResponse>() {
            @Override
            public void onResponse(Call<SyncResponse> call, retrofit2.Response<SyncResponse> response) {
                if(response.code() == 200){
                    //取得返回数据
                    syncresponse = response.body();
                    String temp = syncresponse.getLocalInsert().get(0).getDate();
                    //TODO ：
                    //获取同步信息后的操作
                    localDelete();
                    localInsert();
                    localUpdate();
                    if(syncresponse.getRemoteInsert() != null)
                        requestInsert();
                    if(syncresponse.getRemoteUpdate() != null)
                        requestUpdate();

                    String str = "数据同步完成!";
                    Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                }else {
                    Errorbean bean = null;
                    Gson gson = new Gson();
                    TypeAdapter<Errorbean> adapter = gson.getAdapter(Errorbean.class);
                    try {
                        if (response.errorBody() != null)
                            bean = adapter.fromJson(
                                    response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String str = bean.getMessage();
                    Log.e("网络请求失败",str);
                    Toast.makeText(context,"数据同步失败！", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<SyncResponse> call, Throwable t) {
                //数据请求失败
                Log.e("网络","失败");
                String str = "数据同步失败，请检查网络设置!";
                Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void requestInsert(){
        Insert insert = retrofit.create(Insert.class);
        // TODO
        // 获取insert请求体
        Insertbean insertbean = getInsertRequest();
        Call<ResponseBody> call = insert.request(insertbean);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.code() != 200) {
                    Errorbean bean = null;
                    Gson gson = new Gson();
                    TypeAdapter<Errorbean> adapter = gson.getAdapter(Errorbean.class);
                    try {
                        if (response.errorBody() != null)
                            bean = adapter.fromJson(
                                    response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String str = bean.getMessage();
                    Log.e("网络请求失败",str);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //数据请求失败
                Log.e("网络","失败");
                String str = "数据同步失败，请检查网络设置!";
                Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
    public void requestUpdate(){
        Update update = retrofit.create(Update.class);
        // TODO
        // 获取insert请求体
        Updatebean updatebean = getUpdateRequest();
        Call<ResponseBody> call = update.request(updatebean);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.code() != 200) {
                    Errorbean bean = null;
                    Gson gson = new Gson();
                    TypeAdapter<Errorbean> adapter = gson.getAdapter(Errorbean.class);
                    try {
                        if (response.errorBody() != null)
                            bean = adapter.fromJson(
                                    response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String str = bean.getMessage();
                    Log.e("网络请求失败",str);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //数据请求失败
                Log.e("网络","失败");
                String str = "数据同步失败，请检查网络设置!";
                Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void localInsert(){

    }

    public void localUpdate(){

    }

    public void localDelete(){

    }

    public interface Sync {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("sync")
        Call<SyncResponse> request(@Body SyncRequest syncrequest);
    }
    public interface Insert {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("insert")
        Call<ResponseBody> request(@Body Insertbean insertbean);
    }
    public interface Update {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("update")
        Call<ResponseBody> request(@Body Updatebean updatebean);
    }


    private SyncRequest getSyncRequest() {
        SyncRequest body = new SyncRequest();
        ItemDao itemDao = ItemDao.getInstance(context);
        List<Item> itemList = itemDao.getItems();
        if (itemList != null) {
            for (Item item: itemList) {
                body.setData(item);
            }
        }
        return body;
    }

    private Insertbean getInsertRequest(){
        Insertbean body = new Insertbean();
        ItemDao itemDao = ItemDao.getInstance(context);
        for(String str : syncresponse.getRemoteInsert()){
            Item item = itemDao.get(str);
            body.setData(item);
        }
        return body;
    }
    private Updatebean getUpdateRequest(){
        Updatebean body = new Updatebean();
        ItemDao itemDao = ItemDao.getInstance(context);
        for(String str : syncresponse.getRemoteUpdate()){
            Item item = itemDao.get(str);
            body.setData(item);
        }
        return body;
    }
}
