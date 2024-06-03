package com.mismayilov.uikit.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mismayilov.uikit.R
import com.mismayilov.uikit.databinding.CustomServiceCardviewBinding

@SuppressLint("Recycle")
class CustomServiceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
   private val binding = CustomServiceCardviewBinding.inflate(
       LayoutInflater.from(context),
       this,
       true
   )
    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CustomServiceView)
        val text = array.getString(R.styleable.CustomServiceView_text)
        val icon = array.getResourceId(R.styleable.CustomServiceView_icon, R.drawable.send_money)
        val color = array.getColor(R.styleable.CustomServiceView_iconColor, 0)
        val iconRotation = array.getFloat(R.styleable.CustomServiceView_iconRotation, 0f)
       setView(text!!, icon, color, iconRotation)
        array.recycle()
    }

    private fun setView(text: String, icon: Int, color: Int, iconRotation: Float) {
        binding.apply {
            textView.text = text
            imageView.setImageResource(icon)
            textView.setTextColor(color)
            imageView.setColorFilter(color)
            imageView.rotation = iconRotation
        }
    }
    public fun getTextView() = binding.textView.text.toString()

    override fun setOnClickListener(l: OnClickListener?) {
        binding.root.setOnClickListener(l)
    }
}