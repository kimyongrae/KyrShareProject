package kyr.privat.android_ftp2.adapter

import android.view.View

interface FileItemClickListener {
    fun onItemClick(holder: FileAdapter.ViewHolder, view: View, position:Int)
}