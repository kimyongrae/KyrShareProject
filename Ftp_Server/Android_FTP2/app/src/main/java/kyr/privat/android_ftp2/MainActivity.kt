package kyr.privat.android_ftp2

import AppContants
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kyr.privat.android_ftp2.databinding.ActivityMainBinding
import org.apache.commons.net.ftp.FTPClient

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var ConnectFTP: ConnectFTP? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initUI()

    }

    private fun initUI() {

        ConnectFTP = ConnectFTP(FTPClient())
        AppContants.ConnectFTP = ConnectFTP

        binding.connect.setOnClickListener {

            val host: String = binding.hostText.text.toString()
            val username: String = binding.userText.text.toString()
            val password: String = binding.passwordText.text.toString()
            val portstr: String = binding.portText.text.toString()

            if (host.isEmpty()) {
                AppContants.toast("HOST NOT INPUT")
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                AppContants.toast("USERNAME NOT INPUT")
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                AppContants.toast("PASSWORD NOT INPUT")
                return@setOnClickListener
            }

            if (portstr.isEmpty()) {
                AppContants.toast("PORT NOT INPUT")
                return@setOnClickListener
            }

            val port: Int = Integer.parseInt(portstr)

            CoroutineScope(Dispatchers.Default).launch {

                AppContants.println("호스트:$host")
                AppContants.println("유저이름:$username")
                AppContants.println("패스워드:$password")
                AppContants.println("포트:$port")

                var status = false
                status = AppContants.ConnectFTP!!.ftpConnect(host, username, password, port)


                if (status) {


                    val dir:String?=AppContants.ConnectFTP!!.ftpGetDirectory()

                    AppContants.println("연결성공$dir")

                    withContext(Dispatchers.Main) {
                        AppContants.toast("연결성공")
//                        AppContants.ConnectFTP!!.ftpDisconnect()

                        AppContants.println("dir확인:$dir")
                        val nextIntent = Intent(this@MainActivity, FtpFileActivity::class.java)
                        nextIntent.putExtra("dir",dir)
                        nextIntent.putExtra("username",username)
                        startActivity(nextIntent)
                        finish()
                    }

                } else {
                    AppContants.println("연결실패")
                    withContext(Dispatchers.Main) {
                        AppContants.toast("연결실패")
                    }
                }


            }


        }


    }


    fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }


}


