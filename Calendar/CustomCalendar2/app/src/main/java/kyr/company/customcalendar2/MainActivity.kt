package kyr.company.customcalendar2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    /**
     * 연/월 텍스트뷰
     */
    private var tvDate: TextView? = null
    /**
     * 그리드뷰 어댑터
     */
    private var gridAdapter: GridAdapter? = null
    /**
     * 일 저장 할 리스트
     */
    private var dayList: ArrayList<String> = arrayListOf()
    /**
     * 그리드뷰
     */
    private var gridView: GridView? = null
    /**
     * 캘린더 변수
     */
    private var mCal: Calendar= Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDate = findViewById<View>(R.id.tv_date) as TextView
        gridView = findViewById<View>(R.id.gridview) as GridView

        // 오늘에 날짜를 세팅 해준다.
        val now = System.currentTimeMillis()
        val date = Date(now)
        //연,월,일을 따로 저장
        val curYearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
        val curMonthFormat = SimpleDateFormat("MM", Locale.KOREA)
        val curDayFormat = SimpleDateFormat("dd", Locale.KOREA)

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate!!.text = curYearFormat.format(date).toString() + "/" + curMonthFormat.format(date)

        //gridview 요일 표시
        dayList.add("일")
        dayList.add("월")
        dayList.add("화")
        dayList.add("수")
        dayList.add("목")
        dayList.add("금")
        dayList.add("토")

//        mCal.set(Calendar.MONTH,1)
        //이번달 1일 요일 판단
        mCal.set(Calendar.DAY_OF_MONTH, 1)
        
        //첫번째 요일 값 반환함
        val dayNum = mCal.get(Calendar.DAY_OF_WEEK)

        //1일- 요일 매칭 시키기 위해 공백
        for (i in 1 until dayNum){
            dayList.add("")
        }
        
        //캘린더 달력 설정
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = GridAdapter(applicationContext, dayList)
        gridView!!.adapter = gridAdapter;

    }

    private fun setCalendarDate(month: Int) {
        Log.d("month확인","$month")
        mCal.set(Calendar.MONTH, month - 1)
        for (i in 0 until mCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            dayList.add("" + (i + 1))
        }
    }


}