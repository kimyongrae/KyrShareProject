package kyr.company.expendabletextview

import android.animation.LayoutTransition
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import android.widget.TextView.BufferType
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {

    companion object{
        private const val LONG_TEXT = "Lorem ipsum dolor sit amet, et" +
                " alienum inciderint efficiantur nec, posse causae molestie" +
                " eos in. Ea vero praesent vix, nam soleat recusabo id." +
                " Qui ut exerci option laboramus. In habeo posse ridens quo," +
                " eligendi volutpat interesset ut est, mel nibh accusamus no." +
                " Te eam consulatu repudiare adipiscing, usu et choro quodsi euripidis."

        private const val SHORT_TEXT = " For the 2009 model " +
                "the G35 sedan was replaced by the G37 sedan."

        private const val SHORT_TEXT2 = " For the 2009 model "

        private const val MAX_LINES_COLLAPSED = 3
        private const val INITIAL_IS_COLLAPSED = true
    }

    private var isCollapsed = INITIAL_IS_COLLAPSED

    private var mExpandableTV: TextView? = null
    private var mParentLayout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mParentLayout = findViewById(R.id.root_container)
        mExpandableTV = findViewById(R.id.expandable_tv)

        mExpandableTV!!.text = LONG_TEXT
        makeTextViewResizable(mExpandableTV!!,2,"더보기",true)

        applyLayoutTransition()

    }

    private fun applyLayoutTransition(){
        val transition = LayoutTransition()
        transition.setDuration(300)
        transition.enableTransitionType(LayoutTransition.CHANGING)
        mParentLayout!!.layoutTransition = transition
    }


    fun makeTextViewResizable(tv: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver.also {
            it.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val text: String
                    val lineEndIndex: Int
                    val obs = tv.viewTreeObserver
                    obs.removeOnGlobalLayoutListener(this)
                    if (maxLine == 0) {
                        lineEndIndex = tv.layout.getLineEnd(0)
                        text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                            .toString() + " " + expandText
                    }else if(maxLine>=tv.layout.lineCount){
                        lineEndIndex = tv.layout.getLineEnd(maxLine-1)
                        text = tv.text.subSequence(0, lineEndIndex).toString()
                    }
                    else if (maxLine > 0 && tv.lineCount >= maxLine) {
                        lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                        text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                            .toString() + " " + expandText
                    } else {
                        lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                        text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                    }
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextViewResizable(
                            SpannableString(tv.text.toString()), tv, lineEndIndex, expandText,
                            viewMore
                        ), BufferType.SPANNABLE
                    )
                }
            })
        }
    }


    private fun addClickablePartTextViewResizable(strSpanned: Spanned, tv: TextView, maxLine: Int, spanableText: String, viewMore: Boolean): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), BufferType.SPANNABLE)
                    tv.invalidate()
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "닫기", false)
                    } else {
                        makeTextViewResizable(tv, 2, "더보기", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }




}