package kyr.company.pagedlist.room;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import kyr.company.pagedlist.model.ItemModel;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교재에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
@Dao
public interface ArticleDAO {

    @Query("SELECT * FROM article")
    DataSource.Factory<Integer, ItemModel> getAll();

    @Insert
    void insertAll(ItemModel... users);

    @Query("DELETE FROM article")
    void deleteAll();
}
