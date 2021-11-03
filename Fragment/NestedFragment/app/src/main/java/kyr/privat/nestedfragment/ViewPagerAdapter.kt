package kyr.privat.nestedfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter : FragmentPagerAdapter {

    val framentArrayList : ArrayList<Fragment> = arrayListOf()
    val fragmentTitle : ArrayList<String> = arrayListOf()


    constructor ( fm:FragmentManager) : super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    }

    fun addFragment(fm : Fragment , title : String){
        framentArrayList.add(fm)
        fragmentTitle.add(title)
    }


    override fun getItem(position: Int): Fragment {
        return framentArrayList[position]
    }

    override fun getCount(): Int {
        return  framentArrayList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

}