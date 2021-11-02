package kyr.company.room_database_kotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity 테이블 정의
@Entity(tableName = "word_table")
class Word {

//    @PrimaryKey(autoGenerate = true) autoincrea ment 속성
//    ColumnInfo 테이블 컬럼 이름 지정
    
    @PrimaryKey
    @ColumnInfo(name = "word")
    var word: String = ""


    constructor(word: String){
        this.word = word
    }


}