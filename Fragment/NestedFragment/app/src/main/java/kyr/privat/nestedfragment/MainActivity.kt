package kyr.privat.nestedfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kyr.privat.nestedfragment.ParentFragment.FragmentParent1
import kyr.privat.nestedfragment.ParentFragment.FragmentParent2

class MainActivity : AppCompatActivity() {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment()


    }

    private fun addFragment() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout

        val adapter : ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(FragmentParent1(),"Parent 1")
        adapter.addFragment(FragmentParent2(),"Parent 2")
        viewPager.adapter=adapter
        tabLayout.setupWithViewPager(viewPager)

//        adapter.getItem(0).

    }
}