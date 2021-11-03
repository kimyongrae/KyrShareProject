package kyr.company.pagedlist.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교재에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
@Entity(tableName = "article")
public class ItemModel {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String author;
    public String title;
    public String description;
    public String urlToImage;
    public String publishedAt;
}
