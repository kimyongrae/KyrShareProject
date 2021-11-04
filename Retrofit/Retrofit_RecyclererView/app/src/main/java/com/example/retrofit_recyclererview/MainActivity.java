package com.example.retrofit_recyclererview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    String baseUrl = "http://www.kobis.or.kr";
    String API_KEY = "fa47b9a20fff09c3f70bb77044c991c2";
    Retrofit retrofit;

    Map<String,Object> boxOfficeResult;
    ArrayList<Map<String, Object>> jsonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest =chain.request();

                        Request newRequest = originalRequest.newBuilder().header("Interceptor-Header","xyz")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        recyclerView=findViewById(R.id.rv_recyclerview);

        //Retrofit 객체생성
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        RetrofitInterface retrofitInterface= retrofit.create(RetrofitInterface.class);

        retrofitInterface.getBoxOffice(API_KEY,"20200319").enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                boxOfficeResult= (Map<String, Object>) response.body().get("boxOfficeResult");
                jsonList = (ArrayList) boxOfficeResult.get("dailyBoxOfficeList");
                mAdapter=new MyAdapter(jsonList);
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });

        MySwipeHelper swipeHelper= new MySwipeHelper(MainActivity.this,recyclerView,150) {
            @Override
            public void instantiatrMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(MainActivity.this,
                        "Delete",
                        30,
                        R.drawable.ic_delete_black_24dp,
                        Color.parseColor("#FF3C30"),
                        new MyButtonClickListener() {
                            @Override
                            public void onclick(int pos) {

                                Map<String, Object> json = jsonList.get(viewHolder.getAdapterPosition());

                                String aa =(String)jsonList.get(viewHolder.getAdapterPosition()).get("movieNm");


                                Toast.makeText(MainActivity.this, "Delete click", Toast.LENGTH_SHORT).show();
                                Log.d("TAG",viewHolder.getAdapterPosition()+""+aa);
                                jsonList.remove(viewHolder.getAdapterPosition());                // 해당 항목 삭제
                                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());    // Adapter에 알려주기.
                            }
                        }
                ));
                buffer.add(new MyButton(MainActivity.this,
                        "Update",
                        30,
                        R.drawable.ic_edit_white_24dp,
                        Color.parseColor("#03DAC5"),
                        new MyButtonClickListener() {
                            @Override
                            public void onclick(int pos) {
                                Toast.makeText(MainActivity.this, "edit click", Toast.LENGTH_SHORT).show();
                                //TODO: 편집할 코드
                            }
                        }
                ));
            }
        };// swipeHelper

    }// onCreate()..

    public void click_btn(View view) {

        recyclerView.setAdapter(mAdapter);
    }




}// MainActivity class..
