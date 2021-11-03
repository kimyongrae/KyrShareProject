package kyr.privat.word_doc

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import org.apache.poi.xwpf.usermodel.*
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var editText: EditText? = null
    var filePath: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PackageManager.PERMISSION_GRANTED
        )

        editText = findViewById(R.id.edittext)
        filePath = File(getExternalFilesDir(null), "Test.docx")

        try {

            if (!filePath!!.exists()) {
                filePath!!.createNewFile()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        findViewById<Button>(R.id.create_word).setOnClickListener {

            val document:XWPFDocument = XWPFDocument()

//            val document_Setting :CTSectPr =document.document.body.addNewSectPr()

            var xwpfPargraph:XWPFParagraph = document.createParagraph()
            val xwpfRun:XWPFRun = xwpfPargraph.createRun()

            xwpfRun.setText(editText!!.text.toString())
            xwpfRun.addCarriageReturn()
            xwpfRun.setText("동해물과 백두산이 마르고 달도록 하느님이 보우하사 우리나라만세")
            xwpfRun.addBreak()
            xwpfRun.setText("2절 시작입니다.")

            xwpfPargraph.alignment = ParagraphAlignment.LEFT;
            xwpfRun.setText("이기상과..............................")

/*            val table:XWPFTable =document.createTable(4,4)
            table.createRow()*/

            document.createTOC()





            val fileOutputStream : FileOutputStream = FileOutputStream(filePath)
            document.write(fileOutputStream)

            if(fileOutputStream!=null){
                fileOutputStream.flush()
                fileOutputStream.close()
            }
            document.close()


        }


    }
}