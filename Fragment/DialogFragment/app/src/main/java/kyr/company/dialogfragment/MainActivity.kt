package kyr.company.dialogfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bt : Button = findViewById(R.id.button)

        val dialog1 : FullscreenDialog = FullscreenDialog()

        bt.setOnClickListener {
            val dialog1 : FullscreenDialog = FullscreenDialog.newInstance()!!
            dialog1.setCallback(object:FullscreenDialog.Callback{
                override fun onActionClick(name: String) {
                    Toast.makeText(this@MainActivity, name, Toast.LENGTH_SHORT).show();
                }
            })
            dialog1.show(supportFragmentManager,"tag")
        }


    }
}