
import android.util.Log
import android.widget.Toast
import kyr.privat.android_ftp2.ConnectFTP
import kyr.privat.android_ftp2.common.MyApplication
import java.text.SimpleDateFormat

class AppContants {

    companion object
    {
        val TAG : String ="LOG"

        val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat2 = SimpleDateFormat("yyyy-MM-dd_HH:MM:ss")

        var ConnectFTP: ConnectFTP? = null

        fun println(data : String){
            Log.d(TAG,data)
        }

        fun toast(data: String) {
            Toast.makeText(MyApplication.applicationContext(),data,Toast.LENGTH_SHORT).show()
        }

    }

}