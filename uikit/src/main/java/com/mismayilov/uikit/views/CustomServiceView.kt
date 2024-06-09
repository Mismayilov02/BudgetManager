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
        context.obtainStyledAttributes(attrs, R.styleable.CustomServiceView).apply {
            val text = getString(R.styleable.CustomServiceView_text) ?: ""
            val icon = getResourceId(R.styleable.CustomServiceView_icon, R.drawable.send_money)
            val color = getColor(R.styleable.CustomServiceView_iconColor, 0)
            val iconRotation = getFloat(R.styleable.CustomServiceView_iconRotation, 0f)
            recycle()
            setView(text, icon, color, iconRotation)
        }
    }

    private fun setView(text: String, icon: Int, color: Int, iconRotation: Float) {
        binding.apply {
            textView.text = text
            textView.setTextColor(color)
            imageView.apply {
                setImageResource(icon)
                setColorFilter(color)
                rotation = iconRotation
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.root.setOnClickListener(l)
    }
}