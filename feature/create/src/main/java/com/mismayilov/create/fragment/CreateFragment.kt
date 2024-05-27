package com.mismayilov.create.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.mismayilov.common.unums.IconType
import com.mismayilov.common.utility.Util.Companion.DAY_FORMAT
import com.mismayilov.common.utility.Util.Companion.MONTH_FORMAT
import com.mismayilov.common.utility.Util.Companion.YEAR_FORMAT
import com.mismayilov.common.utility.showDatePicker
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.create.databinding.FragmentCreateBinding
import com.mismayilov.create.flow.CreateEffect
import com.mismayilov.create.flow.CreateEvent
import com.mismayilov.create.flow.CreateState
import com.mismayilov.create.viewmodel.CreateViewModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.uikit.util.getResourceIdByName
import com.mismayilov.uikit.views.CustomBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CreateFragment :
    BaseFragment<FragmentCreateBinding, CreateViewModel, CreateState, CreateEvent, CreateEffect>() {

    private var topCardIconList = mutableListOf<IconModel>()
    private var bottomCardIconList = mutableListOf<IconModel>()

    override fun getViewModelClass(): Class<CreateViewModel> = CreateViewModel::class.java

    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBinding =
        { inflater, container, attachToRoot ->
            FragmentCreateBinding.inflate(inflater, container, attachToRoot)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NavigationManager).bottomNavigationVisibility(false)

        setTime()
        initClickListeners()
        initNumberPad()
        initTabLayout()

    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setTime() {
        val currentTime = System.currentTimeMillis()
        val day = SimpleDateFormat(DAY_FORMAT).format(currentTime)
        val month = SimpleDateFormat(MONTH_FORMAT).format(currentTime)
        val year = SimpleDateFormat(YEAR_FORMAT).format(currentTime)
        binding.txtDate.text = "$day $month\n$year"
    }

    private fun initNumberPad() {
        val numberPadButtons = listOf(
            binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8,
            binding.btn9, binding.btn0, binding.btnDot
        )
        numberPadButtons.forEach { button ->
            button.setOnClickListener { setAmount(button.text.toString()) }
        }
        binding.btnDelete.setOnClickListener {
            binding.txtAmount.text = binding.txtAmount.text.dropLast(1)
        }
    }

    private fun showBottomSheet(icons: List<IconModel>, onSelected: ((Long) -> Unit)) {
        val bottomSheet = CustomBottomSheetDialog(icons, onSelected)
        bottomSheet.show(parentFragmentManager, "CustomBottomSheet")
    }

    private fun setAmount(text: String) {
        val amount = binding.txtAmount.text.toString()
        setEvent(CreateEvent.AmountChanged(amount, text))
    }

    private fun initTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                checkTabPosition(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    @SuppressLint("SetTextI18n")
    private fun checkTabPosition(position: Int) {
        when (position) {
            0 -> {
                setEvent(CreateEvent.CategorySelected(IconType.EXPENSE))
                binding.btnFinish.text = "Expense Now"
            }

            1 -> {
                setEvent(CreateEvent.CategorySelected(IconType.INCOME))
                binding.btnFinish.text = "Income Now"
            }

            2 -> {
                setEvent(CreateEvent.CategorySelected(IconType.ACCOUNT))
                binding.btnFinish.text = "Transfer Now"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initClickListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                (activity as NavigationManager).back()
            }

            btnDate.setOnClickListener {
                showDatePicker(requireContext()) { day, month, year ->
                    txtDate.text = "$day $month\n$year"
                }
            }
            btnFinish.setOnClickListener {
                /*showBottomSheet(bottomCardIconList) {
    //               setEvent(CreateEvent.CardSelectedId(it.toLong(), false))
                }*/
            }

            topCard.setOnClickListener {
                showBottomSheet(topCardIconList) {
                    setEvent(CreateEvent.CardSelectedId(it, true))
                }
            }

            bottomCard.setOnClickListener {
                showBottomSheet(bottomCardIconList) {
                    setEvent(CreateEvent.CardSelectedId(it, false))
                }
            }

            btnReverseCard.setOnClickListener {
                setEvent(CreateEvent.ReverseCards)
            }
        }
    }

    override fun renderState(state: CreateState) {
        super.renderState(state)
        state.topCardData?.let { topCardIconList = it.toMutableList() }
        state.bottomCardData?.let { bottomCardIconList = it.toMutableList() }
        state.selectedTopCard?.let {
            binding.topCardTitle.text = it.name
            binding.topCardIcon.setImageResource(getResourceIdByName(requireContext(), it.icon))
        }
        state.selectedBottomCard?.let {
            binding.bottomCardTitle.text = it.name
            binding.bottomCardIcon.setImageResource(
                getResourceIdByName(
                    requireContext(),
                    it.icon
                )
            )
        }
        state.amount.let { binding.txtAmount.text = it }
    }

}