package kim.company.customprograssbar

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

class CustomProgressDialog (context: Context): Dialog(context) {

    init {
        val params : WindowManager.LayoutParams = window!!.attributes

/*        params.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = params*/
        setTitle(null)
        setCanceledOnTouchOutside(false)
        setOnCancelListener(null)

        val view: View = LayoutInflater.from(context).inflate(R.layout.loading_layout,null)
        setContentView(view)
    }

}