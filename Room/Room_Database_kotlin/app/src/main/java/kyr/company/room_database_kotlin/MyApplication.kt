package kyr.company.room_database_kotlin

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    lateinit var context : Context

/*    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }*/

    @Override
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

/*  init {
        instance = this
    }*/

    companion object{
        private var instance : MyApplication ? =null
        fun applicationContext() : Context {
            // !! 컴파일러 에러방지
            return instance!!.applicationContext
        }
    }

/*    fun abcd() : Int = 100;

    fun context(): Context =applicationContext;*/

}