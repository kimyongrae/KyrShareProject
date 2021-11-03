package kyr.company.cameraapi_recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PageRecyclerAdapter constructor() : RecyclerView.Adapter<PageRecyclerAdapter.PagerViewHolder>()/*,OnPictureMenuItemClickListener*/{

    private val item : MutableList<FileVo> = mutableListOf()

    private val indexVo : MutableList<IndexVo> = mutableListOf()

    private var mContext: Context? = null

    constructor(context: Context) : this(){
        mContext=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View = inflater.inflate(R.layout.swipe_layout, parent, false)

        var listener :OnPictureMenuItemClickListener? = null
        if (mContext is OnPictureMenuItemClickListener){
            listener=mContext as OnPictureMenuItemClickListener
        }
        Log.d("listen","$listener")

        return PagerViewHolder(itemView,listener)
    }

    override fun getItemCount(): Int =item.size

    fun getItemList(): MutableList<FileVo> = item

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
//        holder.bind(position)
        val data :FileVo = item[position]
        data.start="${position+1}"
        data.end="${item.size}"
        holder.bind(data)
    }

    fun addItem(vo :FileVo){
        item.add(vo)
    }

/*    //아이템 클릭 리스너
    fun setOnItemClickListener(listener: OnPictureMenuItemClickListener?) {
        this.listener = listener
    }

    override fun onEditRemoveClick(position: Int) {
        if(listener !=null){
            listener!!.onEditRemoveClick(position)
        }
    }*/

/*    override fun onModifyClick(position: Int) {

    }*/


    inner class PagerViewHolder(itemView: View, var listener: OnPictureMenuItemClickListener?) : RecyclerView.ViewHolder(itemView) /*,View.OnCreateContextMenuListener*/ {

        private val filepath : TextView = itemView.findViewById(R.id.file_path)
        private val filename : TextView = itemView.findViewById(R.id.file_name)
        private val image : ImageView = itemView.findViewById(R.id.image_view)

        //전체 번호
        private val total_count : TextView = itemView.findViewById(R.id.total_count)
        //현재번호
        private val current_count : TextView = itemView.findViewById(R.id.current_count)

        private val remove : Button = itemView.findViewById(R.id.delete)
        private val modify : Button = itemView.findViewById(R.id.modify)

/*        private val listener : OnPictureMenuItemClickListener by lazy {

                mContext as OnPictureMenuItemClickListener
        }*/
//        private val listen = listener

        fun bind(data: FileVo){
            image.setImageBitmap(data.bitmap)

            filepath.text = data.filePath
            if(data.filename != ""){
                filename.text = data.filename
            }else{
                filename.text= "파일 이름 입력"
            }

            if(item.size>1){
                current_count.text=data.start+"/"
                total_count.text=data.end
            }else{
                current_count.text=""
                total_count.text=""
            }


            remove.setOnClickListener{

                listener!!.onRemoveClick(adapterPosition)


            }

            modify.setOnClickListener{

                listener!!.onModifyClick(adapterPosition)

            }

        }


    }



}