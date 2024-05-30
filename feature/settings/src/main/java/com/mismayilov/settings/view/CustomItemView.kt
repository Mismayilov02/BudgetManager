package com.mismayilov.settings.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mismayilov.settings.R
import com.mismayilov.settings.databinding.CustomSettingsItemDesignBinding

class CustomItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = CustomSettingsItemDesignBinding.inflate(
        LayoutInflater.from(context), this, true)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomSettingsView)
            binding.itemName.text = typedArray.getString(R.styleable.CustomSettingsView_itemText)
            binding.iconImageView.setImageResource(typedArray.getResourceId(R.styleable.CustomSettingsView_iconImage, 0))
            val showSwitch = typedArray.getBoolean(R.styleable.CustomSettingsView_showSwitch, false)
            val showTimer = typedArray.getBoolean(R.styleable.CustomSettingsView_showTimer, false)
            binding.itemSwitch.visibility = if (showSwitch) VISIBLE else GONE
            binding.rightIcon.visibility = if (showSwitch) GONE else VISIBLE
            binding.itemTime.visibility = if (showTimer) VISIBLE else GONE
            if (!showSwitch) binding.rightIcon.setImageResource(typedArray.getResourceId(R.styleable.CustomSettingsView_rightIcon, 0))
            typedArray.recycle()
        }
    }
}