package kyr.company.customcalendarviewwithevents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var customCalendarView : CustomCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customCalendarView= findViewById(R.id.custom_calendar_view)
    }
}