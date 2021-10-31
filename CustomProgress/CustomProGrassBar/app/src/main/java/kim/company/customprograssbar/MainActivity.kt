package kim.company.customprograssbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kim.company.customprograssbar.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dialog: CustomProgressDialog = CustomProgressDialog(this@MainActivity)

        binding.btnLoading.setOnClickListener {
            dialog.show()
            CoroutineScope(Dispatchers.Default).launch {
                delay(3000)
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                }
            }
        }
    }
}