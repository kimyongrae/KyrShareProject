package kyr.privat.android_ftp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kyr.privat.android_ftp2.adapter.FileAdapter
import kyr.privat.android_ftp2.adapter.FileItemClickListener
import kyr.privat.android_ftp2.common.MyApplication
import kyr.privat.android_ftp2.databinding.ActivityFtpFileBinding
import org.apache.commons.net.ftp.FTPClient
import java.io.*
import java.lang.Exception

class FtpFileActivity : AppCompatActivity() {

    lateinit var binding: ActivityFtpFileBinding
    val fileAdapter : FileAdapter = FileAdapter()
    var connectFTP : ConnectFTP? =null
    val dirPath: MutableList<String> = mutableListOf()

    var readfile : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ftp_file)
        init()
        DataControll()
    }

    private fun init(){

        connectFTP = ConnectFTP(FTPClient())

        binding.back.setOnClickListener {
            val nextIntent = Intent(this@FtpFileActivity, MainActivity::class.java)
            startActivity(nextIntent)
            AppContants.ConnectFTP=connectFTP
            finish()
        }

        connectFTP=AppContants.ConnectFTP
        AppContants.ConnectFTP=null

//        val dir: String? =intent.getStringExtra("dir")

        val dir: String? ="/"

        if(dir==null){
            binding.path.text=""
        }else{
            dirPath.add(dir)
            val str:StringBuffer = StringBuffer()
            for (i in dirPath.indices){
                str.append(dirPath[i])
            }
            binding.path.text=str
        }

        val username: String? =intent.getStringExtra("username")
        binding.username.text=username

        binding.fileRecycler.layoutManager= LinearLayoutManager(MyApplication.applicationContext())
        binding.fileRecycler.adapter=fileAdapter

        CoroutineScope(Dispatchers.IO).launch {

            if(dir==null){
                return@launch
            }

            val list: MutableList<String> = connectFTP!!.ftpGetFileList(dir)

            for (i in list.indices){
                AppContants.println(list[i].toString())
            }

            fileAdapter.setItems(list)

            withContext(Dispatchers.Main){
                fileAdapter.notifyDataSetChanged()
            }

        }

    }

    private fun DataControll(){

        fileAdapter.setOnItemClickListener(object:FileItemClickListener{
            override fun onItemClick(holder: FileAdapter.ViewHolder, view: View, position: Int) {
                val item : String = fileAdapter.getItem(position)

                //디렉토리,텍스트 일때 처리
                if(item.contains("txt") || item.contains("(Directory)")){
                    if(item.contains("(Directory)")){
                        val a=item.indexOf(")")+1
                        val st:String= item.substring(a)
                        dirPath.add("$st")
                        val str:StringBuffer = StringBuffer()
                        for (i in dirPath.indices){
                            str.append(dirPath[i])
                        }
                        binding.path.text=str
                        val path:String = binding.path.text.toString()

                        CoroutineScope(Dispatchers.IO).launch {
                            connectFTP!!.ftpChangeDirctory(path)
                            AppContants.println("현재 디텍토리 확인:" + connectFTP!!.ftpGetDirectory())

                            val list: MutableList<String> = connectFTP!!.ftpGetFileList(path)
                            list.add(0,"...")

                            for (i in list.indices){
                                AppContants.println(list[i])
                            }
                            fileAdapter.setItems(list)
                            withContext(Dispatchers.Main){
                                fileAdapter.notifyDataSetChanged()
                            }
                        }
                        dirPath.add("/")
                    }

                    else{
                        AppContants.toast("txt 파일을 읽습니다.")

                        val job=CoroutineScope(Dispatchers.IO).launch {

                            val path :String = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath
                            val file = File(path)
                            if(!file.exists()){
                                file.mkdirs()
                            }

                            val a=item.indexOf(")")+1
                            val st:String= item.substring(a)
                            val currentfile_name ="/$st"
                            val newfile_path=path+currentfile_name
                            readfile = newfile_path
                            try {
                                val newFile:File=File(newfile_path)
                                newFile.createNewFile()
                            }catch (e:Exception){
                                e.printStackTrace()
                            }

                            val current_path:String = binding.path.text.toString()
                            AppContants.println("다운받는 폴더이름 확인:$current_path$currentfile_name")
                            connectFTP!!.ftpDownloadFile(current_path+currentfile_name,newfile_path)

                        }

                        CoroutineScope(Dispatchers.Default).launch {
                            job.join()
                            readFile(readfile)


                        }

                    }

                }

                //뒤로가기 처리
                else if(item.contains("...")){
                    AppContants.toast("back")
                    dirPath.removeAt(dirPath.size - 1)
                    dirPath.removeAt(dirPath.size - 1)
                    val str:StringBuffer = StringBuffer()
                    for (i in dirPath.indices){
                        str.append(dirPath[i])
                    }
                    binding.path.text=str
                    AppContants.println("경로 확인:$dirPath")
                    val path:String = binding.path.text.toString()

                    CoroutineScope(Dispatchers.IO).launch {
                        connectFTP!!.ftpChangeDirctory(path)
                        AppContants.println("현재 디텍토리 확인:" + connectFTP!!.ftpGetDirectory())

                        val list: MutableList<String> = connectFTP!!.ftpGetFileList(path)
                        if(path == "/"){
                        }else{
                            list.add(0,"...")
                        }
                        for (i in list.indices){
                            AppContants.println(list[i])
                        }

                        fileAdapter.setItems(list)

                        withContext(Dispatchers.Main){
                            fileAdapter.notifyDataSetChanged()
                        }
                    }
                }

                //디렉토리 텍스트가 아닌경우 처리
                else{
                    AppContants.toast("텍스트 파일 또는 디렉토리만 읽을 수 있습니다.")
                }

            }
        })

    }

    private fun readFile(fileName:String){
        val fileContents:String = ""

        try {

/*            val istream:InputStream = openFileInput(fileContents)
            if(istream != null){
                val istreamReader:InputStreamReader = InputStreamReader(istream)
                val bufferedReader:BufferedReader = BufferedReader(istreamReader)
                var temp:String =""
                while ((temp = bufferedReader.readLine()) != null){
                }
            }*/

            val bufferedReader :BufferedReader = File(fileName).bufferedReader(charset("UTF-8"))
            val text =  bufferedReader.use { it.readText() }

            CoroutineScope(Dispatchers.Main).launch {
                AppContants.toast(text)
            }

        }catch (e:Exception){

        }

    }




}