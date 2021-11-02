package kyr.company.imageslider

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PageRecyclerAdapter :RecyclerView.Adapter<PageRecyclerAdapter.PagerViewHolder>(){

    private val ImageResource : Array<Int> = arrayOf(R.drawable.apple,R.drawable.banana,R.drawable.grape,R.drawable.orange,R.drawable.pear)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View = inflater.inflate(R.layout.swipe_layout, parent, false)
        return PagerViewHolder(itemView)
    }

    override fun getItemCount(): Int =ImageResource.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textView : TextView = itemView.findViewById(R.id.image_count)
        private val image : ImageView = itemView.findViewById(R.id.image_view)

        fun bind(position: Int){
            textView.text = "Image Number:$position"
            image.setImageResource(ImageResource[position])
        }

    }

}