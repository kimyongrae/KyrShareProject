package kyr.privat.pdfcreate

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var btn: Button? = null
    var item1_spinner: Spinner? = null
    var item2_spinner: Spinner? = null
    var et_customer: EditText? = null
    var et_phone: EditText? = null
    var et_qty1: EditText? = null
    var et_qty2: EditText? = null
    var scaledbmp: Bitmap? = null

    var pageWidth = 1200
    var dateObj: Date? = null
    var dateFormat: DateFormat? = null
    var prices = floatArrayOf(0f, 2000f, 3000f, 4500f, 5000f)

    val canvas: Canvas? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn = findViewById(R.id.button);
        item1_spinner = findViewById(R.id.spinner1);
        item2_spinner = findViewById(R.id.spinner2);
        et_customer = findViewById(R.id.editText1);
        et_phone = findViewById(R.id.editText2);
        et_qty1 = findViewById(R.id.editText3);
        et_qty2 = findViewById(R.id.editText4);

        var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pizza);
        scaledbmp = Bitmap.createScaledBitmap(bitmap, 1200, 300, false);



        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)

        createPDF()

    }


    private fun createPDF() {

        btn?.setOnClickListener {

            if (et_customer?.text.toString().isEmpty() ||
                    et_phone?.text.toString().isEmpty() ||
                    et_qty1?.text.toString().isEmpty() ||
                    et_qty2?.text.toString().isEmpty()) {
                Toast.makeText(this, "모두입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                val pdfDocument : PdfDocument = PdfDocument();
                val paint : Paint = Paint();
                val titlePaint : Paint = Paint();
                var pageInfo : PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(1200, 2000, 1).create();
                var page : PdfDocument.Page = pdfDocument.startPage (pageInfo);
                var canvas: Canvas = page.canvas

//                var bitmap1 : Bitmap = Bitmap.createScaledBitmap(bitmap!!, 1200, 300, false);

                canvas.drawBitmap(scaledbmp!!, 0f, 0f, paint)

                dateObj = Date()

                titlePaint.textAlign = Paint.Align.CENTER;
                titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                titlePaint.textSize = 70F;
                canvas.drawText("맛있는 피자", (pageWidth / 2).toFloat(), 150.toFloat(), titlePaint);

                paint.color = Color.rgb(0, 113, 188);
                paint.textSize = 30f;
                paint.textAlign = Paint.Align.RIGHT;
                canvas.drawText("Tel:02-1234-5678", 1160f, 40f, paint);
                canvas.drawText("Tel:02-1234-123", 1160f, 80f, paint);
                titlePaint.textAlign = Paint.Align.CENTER;
                titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
                titlePaint.textSize = 70f;
                canvas.drawText("영수증", (pageWidth / 2).toFloat(), 500.toFloat(), titlePaint);
                paint.textAlign = Paint.Align.LEFT;
                paint.textSize = 35f;
                paint.color = Color.BLACK;
                canvas.drawText("고객 이름: " + et_customer!!.text.toString(), 20f, 590f, paint);
                canvas.drawText("연락처: " + et_phone!!.text.toString(), 20f, 640f, paint);
                paint.textAlign = Paint.Align.RIGHT;
                canvas.drawText("영수정 번호: " + "20202211", (pageWidth - 20).toFloat(), 590f, paint);
                dateFormat = SimpleDateFormat ("yy/MM/dd");
                canvas.drawText("Date: " + (dateFormat as SimpleDateFormat).format(dateObj), (pageWidth - 20).toFloat(), 640f, paint);
                dateFormat =  SimpleDateFormat ("HH:mm:ss");
                canvas.drawText("시간: " + (dateFormat as SimpleDateFormat).format(dateObj), (pageWidth - 20).toFloat(), 690f, paint);
                paint.style = Paint.Style.STROKE;
                paint.strokeWidth = 2f;
                canvas.drawRect(20f, 780f, (pageWidth - 20).toFloat(), 860f, paint);
                paint.textAlign = Paint.Align.LEFT
                paint.style = Paint.Style.FILL;


                canvas.drawText("번호", 40f, 830f, paint);
                canvas.drawText("구매목록", 200f, 830f, paint);
                canvas.drawText("가격", 700f, 830f, paint);
                canvas.drawText("수량", 900f, 830f, paint);
                canvas.drawText("총금액", 1050f, 830f, paint);
                canvas.drawLine(180f, 790f, 180f, 840f, paint);
                canvas.drawLine(680f, 790f, 680f, 840f, paint);
                canvas.drawLine(880f, 790f, 880f, 840f, paint);
                canvas.drawLine(1030f, 790f, 1030f, 840f, paint);
                var  total1 = 0f;
                if (item1_spinner!!.selectedItemPosition != 0) {
                    canvas.drawText("1.", 40f, 950f, paint);
                    canvas.drawText(item1_spinner?.selectedItem.toString(), 200f, 950f, paint);

                    canvas.drawText("${prices[item1_spinner!!.selectedItemPosition]}", 700f, 950f, paint)
                    canvas.drawText (et_qty1!!.text.toString(), 900f, 950f, paint)

                    total1 = (et_qty1!!.text.toString()).toFloat() * prices[item1_spinner!!.selectedItemPosition]

                    paint.textAlign = Paint.Align.RIGHT
                    canvas.drawText(total1.toString(), (pageWidth - 40).toFloat(), 950f, paint);
                    paint.textAlign = Paint.Align.LEFT;
                }

                var total2:Float = 0f

                if (item2_spinner!!.selectedItemPosition != 0) {

                    canvas.drawText("2.", 40f, 1050f, paint);
                    canvas.drawText(item2_spinner?.selectedItem.toString(), 200f, 1050f, paint);
                    canvas.drawText("${prices[item2_spinner!!.selectedItemPosition]}", 700f, 1050f, paint)
                    canvas.drawText (et_qty2!!.text.toString(), 900f, 1050f, paint);
//                    total2 = Float.parseFloat(et_qty2.getText().toString()) * prices[item2_spinner.getSelectedIte

                    total2 = (et_qty2!!.text.toString()).toFloat() * prices[item2_spinner!!.selectedItemPosition]
                    paint.textAlign = Paint.Align.RIGHT

                    canvas.drawText(total2.toString(), (pageWidth - 40).toFloat(), 1050.toFloat(), paint);
                    paint.textAlign = Paint.Align.LEFT
                }



                val subtotal:Float = total1 +total2;
                canvas.drawLine(680f, 1200f, (pageWidth - 20).toFloat(), 1200f, paint);
                canvas.drawText("합계", 700f, 1250f, paint);
                canvas.drawText(":", 900f, 1250f, paint);
                paint.textAlign = Paint.Align.RIGHT;
                canvas.drawText(subtotal.toString(), (pageWidth - 40).toFloat(), 1250f, paint);
                paint.textAlign = Paint.Align.LEFT;
                canvas.drawText("부가세(10%)", 700f, 1300f, paint);
                canvas.drawText(":", 900f, 1300f, paint);
                paint.textAlign = Paint.Align.RIGHT;
                canvas.drawText((subtotal * 10 / 100).toString(), (pageWidth - 40).toFloat(), 1300f, paint);
                paint.textAlign = Paint.Align.LEFT;
                paint.color = Color.rgb(247, 147, 30);

                canvas.drawRect(680f, 1350f, (pageWidth - 20).toFloat(), 1450f, paint);
                paint.color = Color.BLACK
                paint.textSize = 50f
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText("총합계", 700f, 1415f, paint);
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText((subtotal + (subtotal * 10 / 100)).toString(), (pageWidth - 40).toFloat(), 1415f, paint);
                pdfDocument.finishPage(page);


                pageInfo =PdfDocument.PageInfo.Builder(1200, 2000, 2).create();
                page = pdfDocument.startPage (pageInfo);
                canvas = page.canvas

                canvas.drawBitmap(scaledbmp!!, 0f, 0f, paint)
                pdfDocument.finishPage(page);

                val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")

                val dateFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                val dataName: String = dateFormat1.format(dateObj)
                val strFolderPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS+File.separator+dataName)!!.absolutePath

                val folder = File(strFolderPath)

                if (!folder.exists()) {
                    folder.mkdirs()
                }

//                val file:File = File(Environment.DIRECTORY_DOWNLOADS, "/pizza.pdf");
                try {
                    val time = dateFormat2.format(dateObj)

                    val image1 = "$strFolderPath$time.pdf"

                    val out =FileOutputStream(image1)
                    pdfDocument.writeTo(out);
                    out.flush()
                    out.close()
                } catch (e : IOException) {
                    e.printStackTrace();
                }
                pdfDocument.close();

            }
        }


      /*  
        안드로이드 Q 내부에 저장 할때 방식
        val values = ContentValues()
        val now = Date()
        val fileName = binding.switchInfo.text.toString() + AppContants.dateFormat2.format(now)

        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        values.put(MediaStore.Downloads.IS_PENDING, 1)

        val item: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

        try {
            val pdf = contentResolver.openFileDescriptor(item!!, "w", null)
            if (pdf == null) {
                Log.d("pdf", "null");
            } else {

                val fos = FileOutputStream(pdf.fileDescriptor)
                pdfDocument.writeTo(fos);
                fos.flush()
                fos.close()
                pdf.close()
                contentResolver.update(item, values, null, null)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        values.clear()
        // 파일을 모두 write하고 다른곳에서 사용할 수 있도록 0으로 업데이트를 해줍니다.
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(item!!, values, null, null)
        dialog.dismiss()
        AppContants.toast("PDF 저장완료")
    }*/
        

    }

}


