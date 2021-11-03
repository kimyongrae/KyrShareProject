package kyr.company.pagedlist;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import kyr.company.pagedlist.retrofit.RetrofitFactory;
import kyr.company.pagedlist.retrofit.RetrofitService;
import kyr.company.pagedlist.room.AppDatabase;
import kyr.company.pagedlist.room.ArticleDAO;


/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교재에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MyApplication extends Application {

    public static Context context;
    public static ArticleDAO dao;
    public static RetrofitService networkService;
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "database-name").build();
        dao = db.articleDao();
        networkService = RetrofitFactory.create();
    }
}
