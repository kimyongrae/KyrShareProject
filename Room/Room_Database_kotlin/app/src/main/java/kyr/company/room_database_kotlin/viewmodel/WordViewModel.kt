package kyr.company.room_database_kotlin.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kyr.company.room_database_kotlin.MyApplication
import kyr.company.room_database_kotlin.adapter.WordListAdapter
import kyr.company.room_database_kotlin.db.AppDataBase
import kyr.company.room_database_kotlin.model.Word
import kyr.company.room_database_kotlin.service.WordRepository

class WordViewModel() : ViewModel() {

    private val repository: WordRepository
//    val allWord: LiveData<List<Word>>

    var aa : MutableLiveData<List<Word>>? = MutableLiveData()

     var Insertflag : MutableLiveData<Boolean> = MutableLiveData()

    init {
            val wordDao = AppDataBase.getDatabase(MyApplication.applicationContext(),viewModelScope).wordDao()
            repository = WordRepository(wordDao)
//            allWord = repository.allWord

        select()
//      select2()

        }

        fun select()=viewModelScope.launch{
            aa?.postValue(repository?.select())
            Toast.makeText(MyApplication.applicationContext(),"select",Toast.LENGTH_SHORT).show()
        }


    fun setflag(flag:Boolean){
        Insertflag.postValue(flag)
    }



    fun insert(word: Word) = viewModelScope.launch {
        repository?.insert(word)
//        select()
        Toast.makeText(MyApplication.applicationContext(),"insert",Toast.LENGTH_SHORT).show()
    }




}