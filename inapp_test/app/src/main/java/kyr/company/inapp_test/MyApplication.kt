package kyr.company.inapp_test

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    lateinit var context : Context

    @Override
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        private var instance : MyApplication? =null
        fun applicationContext() : Context {
            // !! 컴파일러 에러방지
            return instance!!.applicationContext
        }
    }

}