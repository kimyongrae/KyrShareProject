package kyr.company.inapp_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kyr.company.inapp_test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            binding.btnOneTime.setOnClickListener {
                val intent = Intent(this@MainActivity, OneTimeActivity::class.java)
                startActivity(intent)
            }
        }


    }

   /* private fun Itembuy(position:Int){

        when(position){
            1->{
                AppContants.toast("물 사주기")
            }
            2->{
                AppContants.toast("커피 사주기")
            }
            3->{
                AppContants.toast("맥 모닝 사주기")
            }
            4->{
                AppContants.toast("책 사주기")
            }
        }
    }*/



}