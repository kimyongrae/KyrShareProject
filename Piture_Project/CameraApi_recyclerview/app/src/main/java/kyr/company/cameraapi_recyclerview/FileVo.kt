package kyr.company.cameraapi_recyclerview

import android.graphics.Bitmap

class FileVo constructor() {

    var filePath: String = ""
    var filename: String = ""
    var bitmap : Bitmap? = null

    var start : String = ""
    var end : String = ""

    constructor(filepath:String , filename:String):this(){
        this.filePath=filepath
        this.filename=filename
    }


    constructor(bitmap: Bitmap, filepath:String, filename:String):this(){
        this.bitmap = bitmap
        this.filePath=filepath
        this.filename=filename
    }

    constructor(bitmap: Bitmap, filepath:String, filename:String, start:String, end:String) :this(){
        this.bitmap = bitmap
        this.filePath= filepath
        this.filename= filename
        this.start = start
        this.end = end
    }

    override fun toString(): String {
        return "filepath=${filePath} \n filename = ${filename}"
    }

}