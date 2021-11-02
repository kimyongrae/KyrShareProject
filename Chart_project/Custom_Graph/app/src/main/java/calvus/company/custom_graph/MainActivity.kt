package calvus.company.custom_graph

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    var rvGraph: RecyclerView? = null
    var graphAdapter: GraphAdapter? = null

    private val workingHours: ArrayList<WorkingHour> = ArrayList()
    private val week = 10
    private val hour = 24
    private val lineWidth = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRamdomData()

        rvGraph = findViewById<RecyclerView>(R.id.rvGraph)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        //리스트 위치 조정
        linearLayoutManager.stackFromEnd = false

        graphAdapter = GraphAdapter(workingHours)
        graphAdapter!!.setWidthCount(week)
        graphAdapter!!.setHeightCount(hour)
        graphAdapter!!.setGraphLineWidth(lineWidth)
        rvGraph?.layoutManager = linearLayoutManager
        rvGraph?.adapter = graphAdapter


/*        rvGraph!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, scrollState: Int) {
                super.onScrollStateChanged(recyclerView, scrollState)
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    rvGraph!!.post { autoScroll() }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })*/

    }


 /*   private fun autoScroll() {
        var graph: FrameLayout? = null
        if (workingHours.size > 0) {
            val xy = IntArray(2)
            var gap = 0
            var position = 0
            var minimumGap = -1
            for (i in 0 until rvGraph!!.childCount) {
                graph = rvGraph!!.getChildAt(i) as FrameLayout?
                if (graph != null) {
                    graph.getLocationInWindow(xy)
                    position = xy[0] + (graph.width + lineWidth) / 2
                    gap = position - rvGraph!!.width
                    if (minimumGap == -1 || abs(gap) < abs(minimumGap)) {
                        minimumGap = gap
                    }
                }
            }
            rvGraph!!.smoothScrollBy(minimumGap, 0)
        }
    }*/

    private fun setRamdomData(){
        val random = Random()

        for (i in 0..100){

            val num = random.nextInt(100)
            Log.d("data","$num")

            workingHours.add(WorkingHour(num))
        }

        for (i in 0..200){

            val num = random.nextInt(2000)
            Log.d("data","$num")

            workingHours.add(WorkingHour(num))
        }

    }

}