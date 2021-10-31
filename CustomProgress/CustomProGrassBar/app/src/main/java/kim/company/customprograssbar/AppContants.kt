package kim.company.customprograssbar

import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast

class AppContants {

    companion object
    {
        val TAG : String ="LOG"

        val metrics: DisplayMetrics = MyApplication.applicationContext().resources.displayMetrics

        fun println(data : String){
            Log.d(TAG,data)
        }

        fun toast(data: String) {
            Toast.makeText(MyApplication.applicationContext(),data,Toast.LENGTH_SHORT).show()
        }



    }

}