package com.mismayilov.uikit.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.tabs.TabLayout
import com.mismayilov.uikit.R
import com.mismayilov.uikit.databinding.CustomTabLayoutDesignBinding

class CustomTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        CustomTabLayoutDesignBinding.inflate(LayoutInflater.from(context), this, true)
     var onClickListener: ((tabPosition: Int) -> Unit)? = null

     val currentSelectedTabPosition: Int
        get() = binding.tabLayout.selectedTabPosition

    fun selectTab(position: Int) {
        binding.tabLayout.getTabAt(position)?.select()
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout)
        initAttributes(typedArray)
        initClickListeners()
        typedArray.recycle()
    }

    private fun initClickListeners() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    onClickListener?.invoke(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun initAttributes(typedArray: TypedArray) {
        binding.apply {
            val tabTitlesResId = typedArray.getResourceId(R.styleable.CustomTabLayout_tabTitles, 0)
            if (tabTitlesResId != 0) {
                val tabTitles = resources.getStringArray(tabTitlesResId)
                tabTitles.forEach {
                    binding.tabLayout.addTab(binding.tabLayout.newTab().setText(it))
                }
            }
        }
    }

}