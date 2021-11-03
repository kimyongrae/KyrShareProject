package calvus.company.foreground_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MYBrodCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("test","${intent!!.action} 드군 ${intent.getStringExtra("test")}")
    }


}