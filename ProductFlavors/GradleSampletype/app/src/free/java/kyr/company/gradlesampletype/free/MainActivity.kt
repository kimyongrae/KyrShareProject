package kyr.company.gradlesampletype.free

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kyr.company.gradlesampletype.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.text).setOnClickListener {
            Toast.makeText(this,"text클릭함",Toast.LENGTH_SHORT).show()
        }
    }
}