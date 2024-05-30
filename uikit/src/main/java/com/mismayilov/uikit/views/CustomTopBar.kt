package com.mismayilov.uikit.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mismayilov.uikit.R
import com.mismayilov.uikit.databinding.CustomTopbarDesignBinding

@SuppressLint("Recycle")
class CustomTopBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){
    private val binding = CustomTopbarDesignBinding.inflate(LayoutInflater.from(context), this, true)
    var backClickListener: (() -> Unit)? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTopBar)
       initAttributes(typedArray)
        typedArray.recycle()
    }

    private fun initAttributes(typedArray: TypedArray) {
        val title = typedArray.getString(R.styleable.CustomTopBar_topText)
        binding.text.text = title
        binding.btnBack.setOnClickListener {
            backClickListener?.invoke()
        }
    }
}