package kyr.company.customcalendarviewwithevents

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.usage.UsageEvents
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.LayoutInflater
import android.view.VerifiedInputEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class EventRecyleAdapter constructor() : RecyclerView.Adapter<EventRecyleAdapter.MyViewHolder>() {

    lateinit var context : Context
    var arrayList : MutableList<Events> = mutableListOf()
    val dbOpenHelper : DBOpenHelper = DBOpenHelper(MyApplication.applicationContext())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.show_events_rowlayout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val events:Events = arrayList[position]
        holder.Event.text = events.EVENT
        holder.DateTxt.text = events.DATE
        holder.Time.text = events.TIME
        holder.delete.setOnClickListener {
            deleteCalendarEvent(events.EVENT,events.DATE,events.TIME)
            arrayList.removeAt(position)
            notifyDataSetChanged()
        }

        val arlarm_flag=isAarmed(events.DATE,events.EVENT,events.TIME)


        if(arlarm_flag){
            holder.setAlarm.setImageResource(R.drawable.ic_baseline_notifications_on)
        }else{
            holder.setAlarm.setImageResource(R.drawable.ic_baseline_notifications_off_24)
        }

        val dateCalendar : Calendar = Calendar.getInstance()
        dateCalendar.time = ConvertStringToDate(events.DATE)
        val alarmYear = dateCalendar[Calendar.YEAR]
        val alarmMonth = dateCalendar[Calendar.MONTH]
        val alarmDay = dateCalendar[Calendar.DAY_OF_MONTH]//이달의 몇일

        val timecalendar : Calendar = Calendar.getInstance()
        timecalendar.time = ConvertStringToTome(events.TIME)
        val alarmHour = timecalendar[Calendar.HOUR_OF_DAY]//24시간제
        val alramMinute = timecalendar[Calendar.MINUTE]

        Log.d("데이터확인이요","$alarmYear,$alarmMonth,$alarmDay,$alarmHour,$alramMinute")

        holder.setAlarm.setOnClickListener {

            //알람이 있는 경우
            if(arlarm_flag){
                holder.setAlarm.setImageResource(R.drawable.ic_baseline_notifications_off_24)
                //알람의 ID를 가져와서 해당 알림을 꺼줌
                cancleAlaram(getRequestCode(events.DATE,events.EVENT,events.TIME))
                //알람의 상태 변경
                updateEvent(events.DATE,events.EVENT,events.TIME,"off")
                notifyDataSetChanged()
            }
            //알람이 없는 경우
            else{
                holder.setAlarm.setImageResource(R.drawable.ic_baseline_notifications_on)

                val alarmCalendar = Calendar.getInstance()
                alarmCalendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alramMinute)
                setAlaram(alarmCalendar,events.EVENT,events.TIME,getRequestCode(events.DATE,events.EVENT,events.TIME))
                updateEvent(events.DATE,events.EVENT,events.TIME,"on")
                notifyDataSetChanged()
            }
        }

    }


    private fun ConvertStringToDate(eventDate: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd",Locale.KOREA)
        var date : Date? = null
        try {
            date = format.parse(eventDate)
            Log.d("date","$date")
        }catch (e: Exception){
            e.printStackTrace()
        }
        return date
    }

    private fun ConvertStringToTome(eventDate: String): Date? {
        val format = SimpleDateFormat("hh:mm",Locale.KOREA)
        var date : Date? = null
        try {
            date = format.parse(eventDate)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return date
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    constructor(context: Context,arrayList:MutableList<Events>):this(){
        this.context = context
        this.arrayList = arrayList
    }

    inner class MyViewHolder : RecyclerView.ViewHolder {

        lateinit var DateTxt:TextView
        lateinit var Event:TextView
        lateinit var Time:TextView
        lateinit var itemview: View
        lateinit var delete: Button
        lateinit var setAlarm : ImageButton

        constructor(view : View) : super(view){
            this.itemview=view
            DateTxt = itemview.findViewById(R.id.eventdate)
            Event = itemview.findViewById(R.id.eventname)
            Time = itemview.findViewById(R.id.eventtime)
            delete = itemview.findViewById(R.id.delete)
            setAlarm = itemview.findViewById(R.id.alarmmeBtn)
        }

    }

    private fun deleteCalendarEvent(event:String,date:String,time:String){
        val database : SQLiteDatabase = dbOpenHelper.writableDatabase
        dbOpenHelper.deleteEvent(event,date,time,database)
        dbOpenHelper.close()
    }



    private fun isAarmed(date: String,event: String,time: String):Boolean{
        var alarmed = false
        val database:SQLiteDatabase = dbOpenHelper.readableDatabase
        val cursor = dbOpenHelper.ReadIDEvents(date,event,time,database)
        while (cursor.moveToNext()){
            val notify :String = cursor.getString(cursor.getColumnIndex(DBStructure.Notify))
            alarmed = notify == "on"
        }
        cursor.close()
        dbOpenHelper.close()
        return alarmed
    }


    private fun setAlaram(calendar: Calendar, event: String, time: String, RequestCode:Int){
        val intent: Intent = Intent(context.applicationContext,AlarmReceiver::class.java)
        intent.putExtra("event",event)
        intent.putExtra("time",time)
        intent.putExtra("id",RequestCode)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context,RequestCode,intent,PendingIntent.FLAG_ONE_SHOT)
        val alarmManager : AlarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }


    private fun cancleAlaram(RequestCode:Int){
        val intent:Intent = Intent(context.applicationContext,AlarmReceiver::class.java)
        val pendingIntent:PendingIntent = PendingIntent.getBroadcast(context,RequestCode,intent,PendingIntent.FLAG_ONE_SHOT)
        val alarmManager : AlarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun getRequestCode(date: String,event: String,time: String):Int{
        var code = 0
        val database=dbOpenHelper.readableDatabase
        val cursor: Cursor = dbOpenHelper.ReadIDEvents(date,event,time,database)
        while (cursor.moveToNext()){
            code  = cursor.getInt(cursor.getColumnIndex(DBStructure.ID))
        }
        cursor.close()
        dbOpenHelper.close()
        return code
    }

    private fun updateEvent(date:String,event: String,time: String,notify :String){
        val database:SQLiteDatabase = dbOpenHelper.writableDatabase
        dbOpenHelper.updateEvent(date,event,time,notify,database)
        dbOpenHelper.close()
    }



}