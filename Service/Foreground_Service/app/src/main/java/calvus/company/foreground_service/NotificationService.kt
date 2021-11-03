package calvus.company.foreground_service
import android.app.*
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews

class NotificationService : Service() {

    private val channelId = "channelId test"
    private var notificationManager: NotificationManager? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Override
    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        startNotification()
    }

    // 채널을 등록하는 함수.
    // 오래오 윗버젼일 때는 아래와 같이 채널을 만들어 Notification과 연결해야 한다.
    private fun channelRegister(){
        val channelName = "김용래 채널"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    // foreground 시작하는 함수.
    private fun startNotification() {
        channelRegister()

        // PendingIntent 입니다.
        // PendingIntent를 이용하면 포그라운드 서비스 상태에서 알림을 누르면 앱의 MainActivity를 다시 열게 된다.
        val contentIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        // Foreground Service의 layout입니다.
        val view = RemoteViews(packageName, R.layout.service_first)
        view.setTextViewText(R.id.text_layout,"test 입니다.")

/*        val closePandingIntent : Intent = Intent(this,MainActivity::class.java)
        closePandingIntent.action = "clear"
        closePandingIntent.putExtra("test",0)
        val closeIntent = PendingIntent.getBroadcast(this, 0, closePandingIntent, 0)
        view.setOnClickPendingIntent(R.id.button,closeIntent)*/

        // notification 셋팅
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)  // 아이콘 셋팅
                .setCustomContentView(view)       // 레이아웃 셋팅
                .setContentIntent(contentIntent)  // pendingIntent 클릭시 화면 전환을 위해
                .setAutoCancel(true)
                .build()
        } else {
            //TODO("VERSION.SDK_INT < O")
            Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)  // 아이콘 셋팅
                .setContent(view)                 // 레이아웃 셋팅
                .setContentIntent(contentIntent)  // pendingIntent 클릭시 화면 전환을 위해
                .setAutoCancel(true)
                .build()
        }
        // Foreground 시작하는 코드
        startForeground(1, notification)


/*        stopForeground(true)
        stopSelf()*/

    }





}