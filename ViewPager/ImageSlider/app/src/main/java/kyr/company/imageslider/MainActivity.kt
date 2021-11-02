package kyr.company.imageslider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var pager2 : ViewPager2
    lateinit var adapter : PageRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager2 = findViewById(R.id.view_pager)
        adapter = PageRecyclerAdapter()
        view_pager.adapter=adapter

    }


}