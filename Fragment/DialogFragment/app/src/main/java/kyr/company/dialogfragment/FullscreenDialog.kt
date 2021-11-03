package kyr.company.dialogfragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class FullscreenDialog : DialogFragment(),View.OnClickListener {

    private var callback: Callback? = null

    companion object{

        fun newInstance(): FullscreenDialog? {
            return FullscreenDialog()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullscreenDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//         var contextThemeWrapper: Context = ContextThemeWrapper(context,R.style.Theme_DialogFullScreen)
/*        layoutInflater.cloneInContext(contextThemeWrapper)
        contextThemeWrapper.setTheme()*/

        val view: View = inflater.inflate(R.layout.fullscreen_dialog, container, false)
        val close = view.findViewById<ImageButton>(R.id.fullscreen_dialog_close)
        val action = view.findViewById<TextView>(R.id.fullscreen_dialog_action)

        close.setOnClickListener(this)
        action.setOnClickListener(this)

        return view
    }

    fun ab(){

    }

    fun setCallback(callback: Callback?):Unit{
        this.callback = callback
    }

    override fun onClick(v: View?) {

        val id : Int = v!!.id;

        when(id){

            R.id.fullscreen_dialog_close->dismiss()

            R.id.fullscreen_dialog_action->{
                callback?.onActionClick("ㅎㅎ")
                dismiss();
            }

        }


    }


    public  interface Callback{
        fun onActionClick(name : String)
    }


}