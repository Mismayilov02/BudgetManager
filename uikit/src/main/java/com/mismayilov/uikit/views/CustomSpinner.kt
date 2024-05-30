package com.mismayilov.uikit.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.mismayilov.uikit.R
import com.mismayilov.uikit.databinding.CustomSpinnerDesignBinding

class CustomSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding =
        CustomSpinnerDesignBinding.inflate(LayoutInflater.from(context), this, true)

    private var listData = mutableListOf<String>()
    var spinnerText: String = ""
        get() = binding.spinner.selectedItem.toString()

    fun setSpinnerData(data: List<String>) {
        listData = data.toMutableList()
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, listData)
        binding.spinner.adapter = adapter
    }

    fun setSelection(data: String) {
        val position = listData.indexOf(data)
        binding.spinner.setSelection(position)
    }
//10:44

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner)
        val title = attributes.getString(R.styleable.CustomSpinner_spinner_text)
        binding.spinnerText.text = title
        initSpinner()
        attributes.recycle()
    }

    private fun initSpinner() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                spinnerText = parent?.getItemAtPosition(position).toString()
                (view as? TextView)?.setTextColor(resources.getColor(R.color.text_main, null))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}