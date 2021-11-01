package kyr.company.customcalendar2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*
import java.util.zip.Inflater


class GridAdapter(private val context: Context,private val list: MutableList<String>) : BaseAdapter() {

/*    var list : MutableList<String> = mutableListOf()
    lateinit var inflater:LayoutInflater*/

/*    constructor(context: Context, list: MutableList<String>):this(){
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.list=list
    }*/

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var holder:ViewHolder

        var view =convertView
        if(view == null){
            view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater).inflate(R.layout.item_calendar_gridview, parent, false)
            holder = ViewHolder()
            holder.tvItemGridView = view.findViewById(R.id.tv_item_gridview) as TextView
            view.tag = holder

        }else{
                holder = view!!.tag as ViewHolder
        }
        holder.tvItemGridView.text = "" + getItem(position)

        val mCal = Calendar.getInstance()
        //오늘 day 가져옴
        //오늘 day 가져옴
        val today: Int = mCal.get(Calendar.DAY_OF_MONTH)
        val sToday = today.toString()

        if (sToday == getItem(position)) { //오늘 day 텍스트 컬러 변경
            holder.tvItemGridView.setTextColor(Color.BLUE)
        }

        return view

    }

    inner class ViewHolder{
        lateinit var tvItemGridView : TextView
    }


}