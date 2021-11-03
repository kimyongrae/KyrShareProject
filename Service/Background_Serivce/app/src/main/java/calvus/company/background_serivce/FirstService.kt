package calvus.company.background_serivce

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.util.*
import kotlin.concurrent.schedule


class FirstService : Service() {

    private var timer = Timer("TimerTest", false).schedule(3000, 3000) {
        doSomethingTimer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        // TODO("Return the communication channel to the service.")
        //super.onBind(intent)
        throw UnsupportedOperationException("Not yet")
    }

    companion object {
        fun startService(context: Context) {
            val startIntent = Intent(context, FirstService::class.java)
            context.startService(startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, FirstService::class.java)
            context.stopService(stopIntent)
        }
    }

    // 처음 시작되면 호출되는 함수.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        callEvent(intent)
        return START_NOT_STICKY
    }

    // 이벤트를 호출(구현하고 싶은 코드를 구현하면 됩니다.)
    private fun callEvent(intent: Intent?){
        Log.d("test", "callEvent()")
        val time:Long=timer.scheduledExecutionTime()
        Log.d("time","시간:$time")
    }

    // Timer에서 실행되는 함수.
    private fun doSomethingTimer(){
        Handler(Looper.getMainLooper()).post{
            Toast.makeText(applicationContext, "test", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }



}