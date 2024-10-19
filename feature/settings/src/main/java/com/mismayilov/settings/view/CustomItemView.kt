package com.mismayilov.settings.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mismayilov.settings.R
import com.mismayilov.settings.databinding.CustomSettingsItemDesignBinding

@SuppressLint("CustomViewStyleable")
class CustomItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = CustomSettingsItemDesignBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setSubText(subText: String) {
        binding.subText.text = subText
    }

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomSettingsView)
            val subText = typedArray.getString(R.styleable.CustomSettingsView_subText)
            if (subText.isNullOrEmpty()) {
                binding.subText.visibility = GONE
            } else {
                binding.subText.visibility = VISIBLE
                binding.subText.text = subText
            }
            binding.itemName.text = typedArray.getString(R.styleable.CustomSettingsView_itemText)
            binding.iconImageView.setImageResource(
                typedArray.getResourceId(
                    R.styleable.CustomSettingsView_iconImage,
                    0
                )
            )
            typedArray.recycle()
        }
    }

}