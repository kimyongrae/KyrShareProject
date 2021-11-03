package kyr.company.childfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kyr.company.childfragment.databinding.FragmentOneBinding


class OneFragment : Fragment() {

    private var Childfragment1 : Child1Fragment? = null
    private var Childfragment2 : Child2Fragment? = null
    private var Childfragment3 : Child3Fragment? = null

    private lateinit var binding : FragmentOneBinding

    //frament 데이터
//    private var fragmentManager: FragmentManager? = childFragmentManager

    private var fragmentTransaction: FragmentTransaction? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one, container, false)

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_one,container,false)

        Log.d("호출이 왜안되지..","ㅠㅠㅠㅠ1번")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }



    private fun initUI() {

        if(Childfragment1==null){
            Childfragment1 = Child1Fragment()
            childFragmentManager.beginTransaction().add(R.id.child_fragment, Childfragment1!!).commit()
        }

        if(Childfragment2==null){
            Childfragment2 = Child2Fragment()
            childFragmentManager.beginTransaction().add(R.id.child_fragment, Childfragment2!!).commit()
        }

        if(Childfragment3==null){
            Childfragment3 = Child3Fragment()
            childFragmentManager.beginTransaction().add(R.id.child_fragment, Childfragment3!!).commit()
        }

        if (Childfragment1 != null) childFragmentManager?.beginTransaction()?.show(Childfragment1!!)?.commit()
        if (Childfragment2 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment2!!)?.commit()
        if (Childfragment3 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment3!!)?.commit()


        binding.subButton1.setOnClickListener{
            Toast.makeText(activity,"비행기 클릭",Toast.LENGTH_SHORT).show()

            if (Childfragment1 != null) childFragmentManager?.beginTransaction()?.show(Childfragment1!!)?.commit()
            if (Childfragment2 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment2!!)?.commit()
            if (Childfragment3 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment3!!)?.commit()

        }

        binding.subButton2.setOnClickListener {

            if (Childfragment1 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment1!!)?.commit()
            if (Childfragment2 != null) childFragmentManager?.beginTransaction()?.show(Childfragment2!!)?.commit()
            if (Childfragment3 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment3!!)?.commit()

        }

        binding.subButton3.setOnClickListener {

            if (Childfragment1 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment1!!)?.commit()
            if (Childfragment2 != null) childFragmentManager?.beginTransaction()?.hide(Childfragment2!!)?.commit()
            if (Childfragment3 != null) childFragmentManager?.beginTransaction()?.show(Childfragment3!!)?.commit()

        }


    }


}


