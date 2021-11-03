package kyr.company.pagedlist.datasource;


import android.util.Log;


import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.Gson;

import java.util.List;

import kyr.company.pagedlist.MyApplication;
import kyr.company.pagedlist.model.ItemModel;
import kyr.company.pagedlist.model.PageListModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교재에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MyDataSource  extends PageKeyedDataSource<Long, ItemModel> {

    private static final String QUERY = "travel";
    private static final String API_KEY = "2e88a62a740e410cbeb863a826d1337d";

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, ItemModel> callback) {
        MyApplication.networkService.getList(QUERY, API_KEY, 1, params.requestedLoadSize)
                .enqueue(new Callback<PageListModel>() {
                    @Override
                    public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
                        if (response.isSuccessful()) {
/*                            Log.d("kkang", new Gson().toJson(response.raw()));
                            Log.d("kkang", new Gson().toJson(response.body()));*/
                            callback.onResult(response.body().articles, null, 2L);
                            Log.d("loadInitial","loadInitial");
                            new InsertDataThread(response.body().articles).start();
                        }
                    }

                    @Override
                    public void onFailure(Call<PageListModel> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ItemModel> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ItemModel> callback) {
        MyApplication.networkService.getList(QUERY, API_KEY, params.key, params.requestedLoadSize).enqueue(new Callback<PageListModel>() {
            @Override
            public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
                if(response.isSuccessful()) {
                    Log.d("loadAfter","loadAfter");
/*                  Log.d("params.key:",params.key.toString());
                    Log.d("totalResults:",response.body().totalResults+"");*/
                    Log.d("params.requestedLoadSize",params.requestedLoadSize+"");

                    long nextKey = (params.key == response.body().totalResults) ? null : params.key+1;
                    callback.onResult(response.body().articles, nextKey);
                }
            }

            @Override
            public void onFailure(Call<PageListModel> call, Throwable t) {
            }
        });
    }

    class InsertDataThread extends Thread {
        List<ItemModel> articles;
        public InsertDataThread(List<ItemModel> articles){
            this.articles=articles;
        }
        @Override
        public void run() {
//            MyApplication.dao.deleteAll();
            MyApplication.dao.insertAll(articles.toArray(new ItemModel[articles.size()]));
//            MyApplication.dao.insertAll(articles);
        }
    }
}
