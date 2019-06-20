package com.demo.awesomeledger.sync;

import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import com.demo.awesomeledger.activity.MainActivity;
import com.demo.awesomeledger.fragment.DetailFragment;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import com.demo.awesomeledger.Gson.syncRequest;
import com.demo.awesomeledger.Gson.syncResponse;
import com.demo.awesomeledger.Gson.insertbean;
import com.demo.awesomeledger.Gson.updatebean;
import com.demo.awesomeledger.Gson.errorbean;

import java.io.IOException;

public class sync {

    public Context context;
    public syncRequest syncrequest;
    public syncResponse syncresponse;
    public insertbean insertbean;
    public updatebean updatebean;
    public OkHttpClient client;
    public Retrofit retrofit;

    public sync(Context con){
        this.context = con;
    }
    //初始化http请求属性
    public void initHttp(){
        // 创建OkHttpClient.Builder对象
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder() ;
        // 设置拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                // 设置Header
                Request newRequest = chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36")
                        .build() ;
                return chain.proceed(newRequest);
            }
        }) ;
        // 获取OkHttpClient对象
        client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.128.222.189:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public void requestSync(){
        initHttp();
        //创建网络请求接口的实例
        Sync sync = retrofit.create(Sync.class);
        // TODO
        // 获取sync请求体
        syncrequest = getSyncRequest();
        // syncResponse为响应实体类，用来接受机器人返回的回复数据，以下为接口调用
        Call<syncResponse> call = sync.request(syncrequest);
        call.enqueue(new Callback<syncResponse>() {
            @Override
            public void onResponse(Call<syncResponse> call, retrofit2.Response<syncResponse> response) {
                if(response.code() == 200){
                    //取得返回数据
                    syncresponse = response.body();
                    //TODO ：
                    //获取同步信息后的操作
                    String str = "数据同步完成!";
                    Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                }else {
                    errorbean bean = null;
                    Gson gson = new Gson();
                    TypeAdapter<errorbean> adapter = gson.getAdapter(errorbean.class);
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
            public void onFailure(Call<syncResponse> call, Throwable t) {
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
        insertbean = getInsertRequest();
        Call<ResponseBody> call = insert.request(insertbean);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.code() != 200) {
                    errorbean bean = null;
                    Gson gson = new Gson();
                    TypeAdapter<errorbean> adapter = gson.getAdapter(errorbean.class);
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
        updatebean = getUpdateRequest();
        Call<ResponseBody> call = update.request(updatebean);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.code() != 200) {
                    errorbean bean = null;
                    Gson gson = new Gson();
                    TypeAdapter<errorbean> adapter = gson.getAdapter(errorbean.class);
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

    public interface Sync {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @GET("sync")
        Call<syncResponse> request(@Body syncRequest syncrequest);
    }
    public interface Insert {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("insert")
        Call<ResponseBody> request(@Body insertbean insertbean);
    }
    public interface Update {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("update")
        Call<ResponseBody> request(@Body updatebean updatebean);
    }


    public syncRequest getSyncRequest(){
        syncRequest body = null;
        // TODO
        return body;
    }

    public insertbean getInsertRequest(){
        insertbean body = null;
        // TODO
        return  body;
    }
    public updatebean getUpdateRequest(){
        updatebean body = null;
        // TODO
        return body;
    }
}
