package calvus.company.custom_graph

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import calvus.company.custom_graph.common.AppContants


class GraphAdapter constructor() : RecyclerView.Adapter<GraphAdapter.ViewHolder>() {

    private var data: List<WorkingHour>? = null
    private var width = 0
    private var height = 0
    private var widthCount = 0
    private var heightCount = 0
    private var graphLineWidth = 0

    constructor(data: List<WorkingHour>):this(){
        Log.d("graphadpater","GraphAdapter")
        this.data = data
    }

/*    fun GraphAdapter(data: List<WorkingHour>) {
        Log.d("graphadpater","GraphAdapter")
        this.data = data
    }

    fun setData(data: List<WorkingHour>) {
        this.data = data
    }*/

    fun setWidthCount(widthCount: Int) {
        this.widthCount = widthCount
    }

    //24시간
    fun setHeightCount(heightCount: Int) {
        this.heightCount = heightCount
    }

    fun setGraphLineWidth(graphLineWidth: Int) {
        this.graphLineWidth = graphLineWidth
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val v: View = inflater.inflate(R.layout.item_graph_line, viewGroup, false)
        width = viewGroup.width
        height = viewGroup.height

        AppContants.println("확인:${height}")

        if (width <= 0) {
            val dm = viewGroup.context.resources.displayMetrics
            width = dm.widthPixels
        }
        val params: ViewGroup.LayoutParams = v.layoutParams
        params.width = width / widthCount
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        v.layoutParams = params

//        AppContants.println("${width / widthCount}")
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val workHour: Int = data!![i].workingHour
        val currentHeight: Int


        //일한시간이 100시간인경우 100*heigth 높이를 곱해주고 24시간으로 나눔
        currentHeight = if (workHour > 0) {
            height * workHour / heightCount
        } else {
            0
        }

//        AppContants.println("확인:${height*workHour}  $currentHeight")
        
        //레이아웃을 가져와서 그래프 크기 설정
        val line1_param: ViewGroup.LayoutParams = holder.graphLine.layoutParams
        line1_param.width = graphLineWidth
        line1_param.height = currentHeight
        holder.graphLine.layoutParams = line1_param
    }

    override fun getItemCount(): Int {
        return if (data != null) data!!.size else 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val graphLine: View = itemView.findViewById(R.id.graphLine)
    }




}