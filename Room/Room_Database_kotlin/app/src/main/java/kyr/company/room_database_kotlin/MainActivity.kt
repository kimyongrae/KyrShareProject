package kyr.company.room_database_kotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kyr.company.room_database_kotlin.adapter.WordListAdapter
import kyr.company.room_database_kotlin.model.Word
import kyr.company.room_database_kotlin.viewmodel.WordViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var wordViewModel: WordViewModel
    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter =
            WordListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

/*        wordViewModel.allWord?.observe(this, Observer { words ->
            words?.let { adapter.setWords(it) }
        })*/

/*        wordViewModel.allWord?.observe(this, Observer { words ->
            Toast.makeText(this,"$words",Toast.LENGTH_SHORT).show()
            adapter.setWords(words)
        })*/


/*        wordViewModel.aa?.observe(this, Observer { words ->
            Toast.makeText(this,"${words.toString()}",Toast.LENGTH_SHORT).show()
           adapter.setWords(words)
        })*/


        wordViewModel.aa?.observe(this, Observer { words ->
//            Toast.makeText(this,"${words.toString()}",Toast.LENGTH_SHORT).show()
           adapter.setWords(words)
        })

        wordViewModel.Insertflag.observe(this, Observer { flag ->
            if(flag){
                wordViewModel.select()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val word = Word(it)


                    wordViewModel.insert(word)
//                    wordViewModel.select()
                    wordViewModel.setflag(true)

/*                val jobTwo : Job = CoroutineScope(Dispatchers.Default).launch {
                    job.join()
                    wordViewModel.select()
                }

                job.start()
                jobTwo.start()*/



            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}