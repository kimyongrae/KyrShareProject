package kyr.company.customcalendarviewwithevents

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class CustomCalendarView : LinearLayout {

    companion object{
        val MAX_CALENDAR_DAYS =42
    }
    val calendar = Calendar.getInstance()

    var dates: MutableList<Date> = mutableListOf()
    var eventsList : MutableList<Events> = mutableListOf()
    lateinit var alertDialog: AlertDialog
    var myGridAdapter : MyGridAdapter? =null

    var mcontext: Context? = null

    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.KOREA)
    val monthFormat = SimpleDateFormat("MMMM", Locale.KOREA)
    val yearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
    val eventDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    val dbOpenHelper : DBOpenHelper = DBOpenHelper(MyApplication.applicationContext())

    lateinit var gridView : GridView
    lateinit var NextButton:ImageButton
    lateinit var previousButton:ImageButton
    lateinit var currentDate:TextView
    var alarmYear : Int = 0
    var alarmMonth : Int = 0
    var alarmDay = 0
    var alarmHour = 0
    var alarMinute = 0

    constructor(context: Context?) : super(context){
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        mcontext=context
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mcontext=context
        initView()
    }


    private fun initView()
    {
        val inflater : LayoutInflater = mcontext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =  inflater.inflate(R.layout.calendar_layout,this)
        NextButton= view.findViewById(R.id.nextBtn)
        previousButton = view.findViewById(R.id.previousBtn)
        currentDate = view.findViewById(R.id.current_Date)
        gridView = view.findViewById(R.id.gridview)

        setUpItem()
        setUpCalendar()
    }

    private fun setUpItem(){
        previousButton.setOnClickListener {
            calendar.add(Calendar.MONTH,-1)
            setUpCalendar()
        }

        NextButton.setOnClickListener {
            calendar.add(Calendar.MONTH,1)
            setUpCalendar()
        }

        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val builder:AlertDialog.Builder = AlertDialog.Builder(mcontext)

            builder.setCancelable(true)
            val addView:View = LayoutInflater.from(parent.context).inflate(R.layout.add_newevents_layout,null)
            val EventName:EditText = addView.findViewById(R.id.eventname)
            val EventTime = addView.findViewById<TextView>(R.id.eventtime)
            val setTime : ImageButton = addView.findViewById(R.id.seteventtime)
            val AddEvent: Button = addView.findViewById(R.id.addevent)
            val alramMe:CheckBox = addView.findViewById(R.id.alarmme)
            val dateCalendar : Calendar = Calendar.getInstance()
            dateCalendar.time =dates[position]
            alarmYear = dateCalendar[Calendar.YEAR]
            alarmMonth = dateCalendar[Calendar.MONTH]
            alarmDay = dateCalendar[Calendar.DAY_OF_MONTH]


            setTime.setOnClickListener {
                val calendar:Calendar = Calendar.getInstance()
                val hours = calendar.get(Calendar.HOUR_OF_DAY)
                val minuts = calendar.get(Calendar.MINUTE)

                val timePickerDialog : TimePickerDialog = TimePickerDialog(addView.context,R.style.ThemeOverlay_AppCompat_Dialog,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                    val c:Calendar = Calendar.getInstance()
                    c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                    c.set(Calendar.MINUTE,minute)
                    c.timeZone = TimeZone.getDefault()
                    val hformate:SimpleDateFormat = SimpleDateFormat("K:mm a",Locale.KOREA)
                    val evnt_time:String = hformate.format(c.time)
                    EventTime.text=evnt_time
                    alarmHour = c[Calendar.HOUR_OF_DAY]
                    alarMinute = c[Calendar.MINUTE]

                },hours,minuts,false)

                timePickerDialog.show()
            }
            val date = eventDateFormat.format(dates[position])
            val month = monthFormat.format(dates[position])
            val year = yearFormat.format(dates.get(position))

            AddEvent.setOnClickListener {

                if(alramMe.isChecked){
                    saveEvent(EventName.text.toString(),EventTime.text.toString(),date,month,year,"on")
                    setUpCalendar()
                    val calendar : Calendar = Calendar.getInstance()
                    calendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarMinute)
                    setAlaram(calendar,EventName.text.toString(),EventTime.text.toString(),getRequestCode(
                        date,EventName.text.toString(),EventTime.text.toString()
                    ))
                    alertDialog.dismiss()
                }else{
                    saveEvent(EventName.text.toString(),EventTime.text.toString(),date,month,year,"off")
                    setUpCalendar()
                    alertDialog.dismiss()
                }

            }

            builder.setView(addView)
            alertDialog = builder.create()
            alertDialog.show()

        }

        gridView.setOnItemLongClickListener { parent, view, position, id ->

            val date:String = eventDateFormat.format(dates[position])

            val builder:AlertDialog.Builder = AlertDialog.Builder(mcontext)
            builder.setCancelable(true)
            val showView:View = LayoutInflater.from(parent.context).inflate(R.layout.show_events_layout,null)
            val recyclerView: RecyclerView = showView.findViewById(R.id.event_recyle)
            val eventRecyclerAdapter : EventRecyleAdapter = EventRecyleAdapter(showView.context,CollectEventByDate(date))

            recyclerView.apply {
                layoutManager = LinearLayoutManager(showView.context)
                setHasFixedSize(true)
            }
            recyclerView.adapter = eventRecyclerAdapter
            builder.setView(showView)
            alertDialog = builder.create()
            alertDialog.show()

            alertDialog.setOnCancelListener {
                setUpCalendar()
            }


            return@setOnItemLongClickListener true
        }

    }



    private fun CollectEventByDate(date: String):MutableList<Events>{
        val arrayList : MutableList<Events> = mutableListOf()
        val database:SQLiteDatabase = dbOpenHelper.writableDatabase
        val cursor:Cursor = dbOpenHelper.ReadEvents(date,database)
        while (cursor.moveToNext()){
            val event:String = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT))
            val time:String = cursor.getString(cursor.getColumnIndex(DBStructure.TIME))
            val Date:String = cursor.getString(cursor.getColumnIndex(DBStructure.DATE))
            val month:String = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH))
            val Year:String = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR))
            val events:Events = Events(event,time,Date,month,Year)
            arrayList.add(events)
        }
        cursor.close()
        dbOpenHelper.close()
        return arrayList
    }


    private fun saveEvent(event:String,time:String,date:String,month:String,year:String,notify:String){
        val database:SQLiteDatabase = dbOpenHelper.writableDatabase
        dbOpenHelper.SaveEvent(event,time,date,month,year,notify,database)
        dbOpenHelper.close()
        Toast.makeText(MyApplication.applicationContext(),"Event Save",Toast.LENGTH_SHORT).show()
    }

    private fun deleteCalendarEvent(event: String,date: String,time: String){
        val database:SQLiteDatabase = dbOpenHelper.writableDatabase
        dbOpenHelper.deleteEvent(event,date,time,database)
        dbOpenHelper.close()
    }

    private fun isAarmed(date: String,event: String,time: String):Boolean{
        var alarmed = false
        val database:SQLiteDatabase = dbOpenHelper.readableDatabase
        val cursor = dbOpenHelper.ReadIDEvents(date,event,time,database)
        while (cursor.moveToNext()){
            val notify :String = cursor.getString(cursor.getColumnIndex(DBStructure.Notify))
            if(notify=="on"){
                alarmed = true
            }else{
                alarmed = false
            }
        }
        cursor.close()
        dbOpenHelper.close()
        return alarmed
    }

    private fun setUpCalendar(){
        val cDate = dateFormat.format(calendar.time)
        currentDate.text = cDate
        dates.clear()
        var monthCalendar = calendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH,1)
        val FirstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1
        monthCalendar.add(Calendar.DAY_OF_MONTH,-FirstDayofMonth)
        CollectEventsPerMonth(monthFormat.format(calendar.time),yearFormat.format(calendar.time))

        while (dates.size < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }

        myGridAdapter = MyGridAdapter(context,dates,calendar,eventsList)
        gridView.adapter = myGridAdapter
    }


    private fun getRequestCode(date: String,event: String,time: String):Int{
        var code = 0
        val database=dbOpenHelper.readableDatabase
        val cursor:Cursor = dbOpenHelper.ReadIDEvents(date,event,time,database)
        while (cursor.moveToNext()){
            code  = cursor.getInt(cursor.getColumnIndex(DBStructure.ID))
        }
        cursor.close()
        dbOpenHelper.close()
        return code
    }

    private fun setAlaram(calendar: Calendar,event: String,time: String,RequestCode:Int){
        val intent:Intent = Intent(context.applicationContext,AlarmReceiver::class.java)
        intent.putExtra("event",event)
        intent.putExtra("time",time)
        intent.putExtra("id",RequestCode)
        val pendingIntent:PendingIntent = PendingIntent.getBroadcast(context,RequestCode,intent,PendingIntent.FLAG_ONE_SHOT)
        val alarmManager : AlarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }

    private fun CollectEventsPerMonth(Month:String,year: String){
        eventsList.clear()
        val database=dbOpenHelper.readableDatabase
        val cursor:Cursor = dbOpenHelper.ReadEventsPerMonth(Month,year,database)
        while (cursor.moveToNext()){
            val event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT))
            val time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME))
            val date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE))
            val month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH))
            val Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR))
            val events : Events = Events(event,time,date,month,year)
            eventsList.add(events)
        }
        cursor.close()
        dbOpenHelper.close()

    }


}