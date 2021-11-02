package kyr.company.stackbarchart

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IndexOutOfBoundsException

class MainActivity : AppCompatActivity() {

    val color : MutableList<Int> = ArrayList()
    var datavals : MutableList<BarEntry> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        color.add(Color.BLUE)
        color.add(Color.RED)
        color.add(Color.GREEN)

        //      mpLineChart.setAutoScaleMinMaxEnabled(true);
        bar_chart.legend.isEnabled=true // 라벨값
        bar_chart.legend.orientation = Legend.LegendOrientation.HORIZONTAL //라벨 차트 정렬 수평
        bar_chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP //수직 조정 위로
        bar_chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER //수평 조정 오른쪽
        bar_chart.legend.setDrawInside(false)//차트 안에 레전드 넣기
        bar_chart.legend.textSize=25f //차트 안 글씨 조정
        bar_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val xAxis = bar_chart.xAxis
        xAxis.textSize=25f
        xAxis.labelCount=3
        xAxis.axisMinimum= 0F
        xAxis.axisMaximum= 4F

        val yAxis = bar_chart.axisLeft
        yAxis.setDrawLabels(true)
        bar_chart.axisRight.setDrawLabels(false)
        yAxis.axisMaximum = 100f
        yAxis.axisMinimum = 0f

        val description = Description()
        description.text = "Percent to Total(%)" //라벨
        description.textSize = 15f
        description.isEnabled = true
        bar_chart.description=description

        xAxis.valueFormatter= object : IAxisValueFormatter{

            override fun getFormattedValue(value: Float, axis: AxisBase?): String {

                   return when(value.toInt()){
                        0->""
                        1->"Qmax"
                        2->"PDE"
                        3->"PC"
                       else-> ""
                    }
            }
        }

        datavals.add(BarEntry(1f, floatArrayOf(2.5f,5f,4f)))
        datavals.add(BarEntry(2f, floatArrayOf(5f,10f,15f)))
        datavals.add(BarEntry(3f, floatArrayOf(2.5f,5f)))

        val barDataSet = BarDataSet(datavals, "")
        barDataSet.stackLabels= arrayOf("EM","US","ATT")
        barDataSet.setColors(color)

        val barData : BarData = BarData(barDataSet)
        bar_chart.data=barData
        barData.barWidth=0.5f

    }



}




