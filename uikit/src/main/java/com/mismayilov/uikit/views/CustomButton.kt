package com.mismayilov.uikit.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.mismayilov.uikit.R

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    var listener:(View) -> Unit = {}
    var text: String

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)
        text = array.getString(R.styleable.CustomButton_button_text).toString()
        array.recycle()
        initViews()
    }

    fun initViews() {
        setText(text)
        setOnClickListener {
            listener(it)
        }
    }
}
