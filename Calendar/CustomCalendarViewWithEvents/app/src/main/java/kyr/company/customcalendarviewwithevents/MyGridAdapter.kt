package kyr.company.customcalendarviewwithevents

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MyGridAdapter: ArrayAdapter<Date>{

    var dates: MutableList<Date> = mutableListOf()
    var events : MutableList<Events> = mutableListOf()
    lateinit var currentDate:Calendar
    lateinit var inflater: LayoutInflater


    constructor(context: Context, dates: MutableList<Date>,currentDate : Calendar,events : MutableList<Events>) : super(context,R.layout.single_cell_layout){
        this.dates = dates
        this.currentDate = currentDate
        this.events = events
        inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val monthDate : Date = dates[position]
        val dateCalendar : Calendar = Calendar.getInstance()
        dateCalendar.time =monthDate
        val DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val displayMonth = dateCalendar[Calendar.MONTH]+1
        val displayYear = dateCalendar[Calendar.YEAR]
        val currentMonth = currentDate[Calendar.MONTH]+1
        val currentYear = currentDate[Calendar.YEAR]

        var view : View? = convertView

        if(view == null){
            view = inflater.inflate(R.layout.single_cell_layout,parent,false)
        }

        if(displayMonth==currentMonth && displayYear == currentYear){
            view!!.setBackgroundColor(context.resources.getColor(R.color.green))
        }else{
            view!!.setBackgroundColor(Color.parseColor("#cccccc"))
        }

        val DayNumber : TextView = view!!.findViewById(R.id.calendar_day)
        val EventNumber : TextView = view!!.findViewById(R.id.events_id)
        DayNumber.text= DayNo.toString()
        val eventCalendar = Calendar.getInstance()
        val arrayList:MutableList<String> = arrayListOf()

        Log.d("event","$events")

        for (i in events.indices){
            Log.d("event2",events[i].toString())
            eventCalendar.time = ConvertStringToDate(events[i].DATE)
            if(DayNo == eventCalendar[Calendar.DAY_OF_MONTH] && displayMonth == eventCalendar[Calendar.MONTH]+1 && displayYear == eventCalendar[Calendar.YEAR]){
                arrayList.add(events[i].EVENT)
            }
        }

        if(arrayList.size>0){
            EventNumber.text="${arrayList.size} Events"
        }



        return view
    }

    private fun ConvertStringToDate(eventDate: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-ddd",Locale.KOREA)
        var date : Date? = null
        try {
            date = format.parse(eventDate)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return date
    }


    override fun getCount(): Int {
        return dates.size
    }

    override fun getPosition(item: Date?): Int {
        return dates.indexOf(item)
    }

    override fun getItem(position: Int): Date? {
        return dates[position]
    }




}