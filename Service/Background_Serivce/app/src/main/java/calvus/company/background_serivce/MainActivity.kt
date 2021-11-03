package calvus.company.background_serivce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 서비스 시작 버튼을 눌렀을 때 이벤트 함수.
        start_service_btn.setOnClickListener {
            // 서비스를 시작하는 코드
            FirstService.startService(this)
        }
        // 서비스 종료 버튼을 눌렀을 때 이벤트 함수.
        stop_service_btn.setOnClickListener {
            // 서비스를 종료하는 코드
            FirstService.stopService(this)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}