package com.demo.awesomeledger.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import com.demo.awesomeledger.BuildConfig;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;
import com.demo.awesomeledger.gson.*;
import com.demo.awesomeledger.gson.Error;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SyncUtil {

    private Context context;
    private Retrofit retrofit;
    private ItemDao itemDao;
    private RefreshListener refreshListener;

    public interface RefreshListener {
        void afterRefresh();
    }

    public SyncUtil(Context context){
        this.context = context;
        itemDao = ItemDao.getInstance(context);
        refreshListener = (RefreshListener)context;
        initHttp();
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
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.128.222.189:8080/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public void requestSync(){
        Sync sync = retrofit.create(Sync.class);
        Call<SyncResponse> call = sync.request(getSyncRequest());
        call.enqueue(new Callback<SyncResponse>() {
            @Override
            public void onResponse(@NonNull Call<SyncResponse> call, @NonNull retrofit2.Response<SyncResponse> response) {
                if(response.code() == 200){
                    SyncResponse syncResponse = response.body();
                    if (syncResponse != null) {
                        if (syncResponse.getLocalDelete() != null)
                            localDelete(syncResponse.getLocalDelete());
                        if (syncResponse.getLocalInsert() != null)
                            localInsert(syncResponse.getLocalInsert());
                        if (syncResponse.getLocalUpdate() != null)
                            localUpdate(syncResponse.getLocalUpdate());
                        if (syncResponse.getRemoteInsert() != null)
                            requestInsert(syncResponse.getRemoteInsert());
                        if (syncResponse.getRemoteUpdate() != null)
                            requestUpdate(syncResponse.getRemoteUpdate());
                    }
                    refreshListener.afterRefresh();
                    Toast.makeText(context, "数据同步完成!", Toast.LENGTH_LONG).show();
                } else {
                    handleError(response.errorBody());
                    Toast.makeText(context,"数据同步失败！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SyncResponse> call, @NonNull Throwable t) {
                Log.e("网络","失败");
                Toast.makeText(context, "数据同步失败，请检查网络设置!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void requestInsert(List<String> remoteInsert){
        Insert insert = retrofit.create(Insert.class);
        InsertRequest insertRequest = getInsertRequest(remoteInsert);
        request(insert.request(insertRequest));
    }

    private void request(Call<ResponseBody> request) {
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                if(response.code() != 200) {
                    handleError(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("网络","失败");
                Toast.makeText(context, "数据同步失败，请检查网络设置!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void handleError(ResponseBody responseBody) {
        Error error = null;
        Gson gson = new Gson();
        TypeAdapter<Error> adapter = gson.getAdapter(Error.class);
        try {
            if (responseBody != null)
                error = adapter.fromJson(responseBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (error != null) {
            Log.e("网络请求失败", error.getMessage());
        } else {
            Log.e("网络请求失败", "无回复错误信息，请检查服务器地址！");
        }
    }

    private void requestUpdate(List<String> remoteUpdate){
        Update update = retrofit.create(Update.class);
        UpdateRequest updateRequest = getUpdateRequest(remoteUpdate);
        request(update.request(updateRequest));
    }

    private void localInsert(List<Item> localInsertList){
        for (Item item: localInsertList) {
            Log.e("item",item.getAddress());
            itemDao.insert(item);
        }
    }

    private void localUpdate(List<Item> localUpdate){
        for (Item item: localUpdate) {
            Item localItem = itemDao.get(item.getUuid());
            if (localItem != null) {
                localItem.setDate(item.getDate());
                localItem.setItemType(item.getItemType());
                localItem.setItemKind(item.getItemKind());
                localItem.setAddress(item.getAddress());
                localItem.setMoney(item.getMoney());
                localItem.setComment(item.getComment());
                itemDao.update(localItem);
            }
        }
    }

    private void localDelete(List<String> localDelete){
        for (String uuid: localDelete) {
            Item localItem = itemDao.get(uuid);
            if (localItem != null) {
                itemDao.delete(uuid);
            }
        }
    }

    public interface Sync {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("sync")
        Call<SyncResponse> request(@Body SyncRequest syncrequest);
    }

    public interface Insert {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("insert")
        Call<ResponseBody> request(@Body InsertRequest insertRequest);
    }

    public interface Update {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("update")
        Call<ResponseBody> request(@Body UpdateRequest updateRequest);
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

    private InsertRequest getInsertRequest(List<String> remoteInsert){
        InsertRequest body = new InsertRequest();
        ItemDao itemDao = ItemDao.getInstance(context);
        for(String uuid: remoteInsert){
            Item item = itemDao.get(uuid);
            if (item != null) {
                body.setData(item);
            }
        }
        return body;
    }

    private UpdateRequest getUpdateRequest(List<String> remoteUpdate){
        UpdateRequest body = new UpdateRequest();
        ItemDao itemDao = ItemDao.getInstance(context);
        for(String uuid : remoteUpdate){
            Item item = itemDao.get(uuid);
            if (item != null) {
                body.setData(item);
            }
        }
        return body;
    }
}
