package kyr.company.room_database_kotlin.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kyr.company.room_database_kotlin.model.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getASCWord(): LiveData<List<Word>>

/*    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAStestCWord(): List<Word>*/

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    suspend fun select() : List<Word>

/*
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun select2(): List<Word>
*/



    //OnConflictStrategy 충돌처리 충돌 발생시 동일한 데이터 무시
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()


}