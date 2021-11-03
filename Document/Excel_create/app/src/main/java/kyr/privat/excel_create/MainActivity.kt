package kyr.privat.excel_create

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val mItems : MutableList<User> = mutableListOf()

    var xlsFile : File? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mItems.add(User("홍길동",2))
        mItems.add(User("홍길동1",3))
        mItems.add(User("홍길동2",4))
        mItems.add(User("홍길동3",5))
        mItems.add(User("홍길동4",6))
        mItems.add(User("홍길동5",7))
        mItems.add(User("홍길동6",8))
        mItems.add(User("홍길동7",9))
        mItems.add(User("홍길동8",10))

        findViewById<Button>(R.id.excel_export).setOnClickListener {
            saveExcel()
        }

/*        findViewById<Button>(R.id.excel_send).setOnClickListener {
            excel_send()
        }*/


    }


    private fun saveExcel() {

        val workbook: Workbook = HSSFWorkbook()
//        val sheet: Sheet = workbook.createSheet() // 새로운 시트 생성
        var sheet: Sheet = workbook.createSheet("홍길동")
        var row: Row = sheet.createRow(0) // 새로운 행 생성
        var cell: Cell
        cell = row.createCell(0) // 1번 셀 생성
        cell.setCellValue("이름") // 1번 셀 값 입력
        cell = row.createCell(1) // 2번 셀 생성
        cell.setCellValue("나이") // 2번 셀 값 입력

        for (i in 0 until mItems.size) { // 데이터 엑셀에 입력
            row = sheet.createRow(i + 1)
            cell = row.createCell(0)
            cell.setCellValue(mItems[i].name)
            cell = row.createCell(1)
            cell.setCellValue("${mItems[i].age}")
        }

        sheet= workbook.createSheet("홍길동2")
        row = sheet.createRow(0) // 새로운 행 생성

        cell = row.createCell(0) // 1번 셀 생성
        cell.setCellValue("이름") // 1번 셀 값 입력
        cell = row.createCell(1) // 2번 셀 생성
        cell.setCellValue("나이") // 2번 셀 값 입력

        for (i in 0 until mItems.size) { // 데이터 엑셀에 입력
            row = sheet.createRow(i + 1)
            cell = row.createCell(0)
            cell.setCellValue(mItems[i].name)
            cell = row.createCell(1)
            cell.setCellValue("${mItems[i].age}")
        }

        xlsFile=File(getExternalFilesDir(null), "test.xls")
        try {
            val os = FileOutputStream(xlsFile)
            workbook.write(os) // 외부 저장소에 엑셀 파일 생성
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Toast.makeText(applicationContext, xlsFile!!.absolutePath.toString() + "에 저장되었습니다", Toast.LENGTH_SHORT).show()

    }


  /*  private fun excel_send(){

        val path: Uri = Uri.fromFile(xlsFile)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/excel"
        shareIntent.putExtra(Intent.EXTRA_STREAM, path)
        startActivity(Intent.createChooser(shareIntent, "엑셀 내보내기"))

    }*/


}