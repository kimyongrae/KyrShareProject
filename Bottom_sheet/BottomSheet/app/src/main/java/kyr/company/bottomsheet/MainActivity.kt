package kyr.company.bottomsheet

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    lateinit var btnShow : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShow = findViewById(R.id.bt_show)

        btnShow.setOnClickListener {
            val bottomSheetDialog:BottomSheetDialog = BottomSheetDialog(this@MainActivity)
            bottomSheetDialog.setContentView(R.layout.bottomsheet_dialog)
            bottomSheetDialog.setCanceledOnTouchOutside(true)

            var etUsertName : EditText? = bottomSheetDialog.findViewById(R.id.et_username)
            var etpassword : EditText? = bottomSheetDialog.findViewById<EditText>(R.id.et_password)
            val btsubmit : Button? = bottomSheetDialog.findViewById(R.id.bt_submit)

            btsubmit!!.setOnClickListener {

                if(etUsertName!!.text.toString()=="admin" && etpassword!!.text.toString()=="admin"){
                    val builder : AlertDialog.Builder = AlertDialog.Builder(it.context)
                    builder.setTitle("Login Succeful")
                    builder.setMessage("Welcome to Android Cording")
                    builder.setNegativeButton("OK",DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    val alertDialog : AlertDialog = builder.create()
                    alertDialog.show()
                }else {
                 Toast.makeText(this,"login failed",Toast.LENGTH_SHORT).show()
                }


            }

            bottomSheetDialog.show()
        }

    }
}