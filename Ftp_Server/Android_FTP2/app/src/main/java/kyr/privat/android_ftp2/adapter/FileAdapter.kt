package kyr.privat.android_ftp2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kyr.privat.android_ftp2.R
import kyr.privat.android_ftp2.common.MyApplication
import kyr.privat.android_ftp2.databinding.FileItemBinding


class FileAdapter :RecyclerView.Adapter<FileAdapter.ViewHolder>(),FileItemClickListener {

    private var fileItem : MutableList<String> = mutableListOf()
    lateinit var filelistener : FileItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(MyApplication.applicationContext())
//        val placeItemBinding : Fragment3DiaryNoteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment3_diary_note,parent,false)

        val fileItemBinding : FileItemBinding = DataBindingUtil.inflate(inflater, R.layout.file_item,parent,false)

        return ViewHolder(fileItemBinding)
    }

    override fun getItemCount()=fileItem.size

    fun setItems(items : MutableList<String>){
        this.fileItem=items
    }

    fun getItem(position: Int) : String = fileItem[position]


    //아이템 클릭 리스너
    fun setOnItemClickListener(listener : FileItemClickListener) {
        this.filelistener = listener
    }

    override fun onItemClick(holder: ViewHolder, view: View, position: Int) {

        if(filelistener !=null){
            filelistener.onItemClick(holder,view,position)
        }

    }

    fun addItem(item: String)= fileItem.add(item)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : String =fileItem[position]
        holder.setItem(item)
    }

    inner class ViewHolder(private val binding: FileItemBinding) : RecyclerView.ViewHolder(binding.root){

        var filebinding: FileItemBinding = binding

        init {

            filebinding.root.setOnClickListener {
                val position : Int =adapterPosition
                if(filelistener!=null){
                    filelistener.onItemClick(this,it,position)
                }
            }

        }

        fun setItem(item: String){
            binding.fileName.text=item
        }




    }




}


