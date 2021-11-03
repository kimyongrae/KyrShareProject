package kyr.company.childfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kyr.company.childfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var fragment1 : OneFragment? = null
    private var fragment2 : TwoFragment? = null

    //frament 데이터
    private var fragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        //계속 켜짐 상태로 두기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        fragmentManager = supportFragmentManager

        if(fragment1 == null){
            fragment1 = OneFragment()
            fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.add(R.id.container, fragment1!!)?.commit()
        }

        if(fragment2 == null){
            fragment2 = TwoFragment()
            fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.add(R.id.container, fragment2!!)?.commit()
        }

        //프래그먼트 1번은 보여주고 2번은 안보이게 설정
        if (fragment1 != null) fragmentManager?.beginTransaction()?.show(fragment1!!)?.commit()
        if (fragment2 != null) fragmentManager?.beginTransaction()?.hide(fragment2!!)?.commit()
        
        binding.oneFragment.setOnClickListener(this)
        binding.twoFragment.setOnClickListener(this)

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.oneFragment->{
                //프래그먼트 1번은 보여주고 2번은 안보이게 설정
                if (fragment1 != null) fragmentManager?.beginTransaction()?.show(fragment1!!)?.commit()
                if (fragment2 != null) fragmentManager?.beginTransaction()?.hide(fragment2!!)?.commit()
            }

            R.id.twoFragment->{
                //프래그먼트 1번은 보여주고 2번은 안보이게 설정
                if (fragment1 != null) fragmentManager?.beginTransaction()?.hide(fragment1!!)?.commit()
                if (fragment2 != null) fragmentManager?.beginTransaction()?.show(fragment2!!)?.commit()
            }

        }
    }



}