package kyr.privat.sub_menu_item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            showPopup(it)
        }


    }

    private fun showPopup(v : View){

        val popup : PopupMenu =  PopupMenu(this , v)
        popup.inflate(R.menu.example_menu)


        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item1 -> Toast.makeText(this,"item 1 selected",Toast.LENGTH_SHORT).show()
                R.id.item2->Toast.makeText(this,"item 2 selected",Toast.LENGTH_SHORT).show()
                R.id.subitem1->Toast.makeText(this,"item 1 selected",Toast.LENGTH_SHORT).show()
                R.id.item1->Toast.makeText(this,"item 1 selected",Toast.LENGTH_SHORT).show()
            }
            true
        }
        popup.show()
    }

/*        popup.inflate(R.menu.example_menu)
        popup.show()*/


/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)

        val inflater : MenuInflater =  menuInflater
        inflater.inflate(R.menu.example_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       when(item.itemId){
            R.id.item1->Toast.makeText(this,"item 1 selected",Toast.LENGTH_SHORT).show()
            R.id.item2->Toast.makeText(this,"item 2 selected",Toast.LENGTH_SHORT).show()
            R.id.item3->Toast.makeText(this,"item 3 selected",Toast.LENGTH_SHORT).show()
            R.id.subitem1->Toast.makeText(this,"item 1 selected",Toast.LENGTH_SHORT).show()
            R.id.item1->Toast.makeText(this,"item 1 selected",Toast.LENGTH_SHORT).show()
        }
        true


        return super.onOptionsItemSelected(item)
    }*/


}