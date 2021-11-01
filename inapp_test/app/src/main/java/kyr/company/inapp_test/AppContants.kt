package kyr.company.inapp_test

import android.util.Log
import android.widget.Toast

class AppContants {

    companion object
    {
        val TAG : String ="LOG"


        fun println(data : String){
            Log.d(TAG,data)
        }

        fun toast(data: String) {
            Toast.makeText(MyApplication.applicationContext(),data,Toast.LENGTH_SHORT).show()
        }



    }

}