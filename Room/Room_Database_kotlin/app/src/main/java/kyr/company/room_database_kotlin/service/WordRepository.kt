package kyr.company.room_database_kotlin.service

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kyr.company.room_database_kotlin.MyApplication
import kyr.company.room_database_kotlin.dao.WordDao
import kyr.company.room_database_kotlin.model.Word

class WordRepository (private val wordDao: WordDao) {

//    val allWord: LiveData<List<Word>> = wordDao.getASCWord()
//    var test : List<Word>? = null

//    var allWord2 : List<Word>? =null

    suspend fun select(): List<Word>? {
        var test :List<Word> =wordDao.select()
        Toast.makeText(MyApplication.applicationContext(),"사이즈는:${test.size}",Toast.LENGTH_SHORT).show()
        return test
    }

/*    suspend fun select2() : List<Word>? {
        allWord2=wordDao.select2()
        return allWord2
    }*/

//    val test : List<Word> = wordDao.getAStestCWord()

    //suspend 이메소드는 코루틴에서 실행되어야 한다고 제한함
    suspend fun insert(word: Word) { wordDao.insert(word) }
}