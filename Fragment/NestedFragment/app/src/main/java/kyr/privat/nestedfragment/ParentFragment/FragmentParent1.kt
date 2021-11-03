package kyr.privat.nestedfragment.ParentFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kyr.privat.nestedfragment.ChildFragment.FragmentChild1
import kyr.privat.nestedfragment.ChildFragment.FragmentChild2
import kyr.privat.nestedfragment.R
import kyr.privat.nestedfragment.ViewPagerAdapter


class FragmentParent1 : Fragment() {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager: ViewPager

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_parent1, container, false)
        addFragment(view)
        return view

    }


    private fun addFragment(view: View) {

        viewPager = view.findViewById(R.id.viewPager2)
        tabLayout = view.findViewById(R.id.tabLayout2) as TabLayout

        val adapter : ViewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FragmentChild1(),"Child 1")
        adapter.addFragment(FragmentChild2(),"Child 2")
        viewPager.adapter=adapter
        tabLayout.setupWithViewPager(viewPager)

    }


}