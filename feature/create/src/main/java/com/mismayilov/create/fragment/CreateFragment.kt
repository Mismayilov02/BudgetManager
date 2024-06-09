package com.mismayilov.create.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismayilov.common.unums.IconType
import com.mismayilov.common.utility.Util.Companion.DAY_FORMAT
import com.mismayilov.common.utility.Util.Companion.MONTH_FORMAT
import com.mismayilov.common.utility.Util.Companion.MULTILINE_DATE_FORMAT
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
import com.mismayilov.uikit.util.showToast
import com.mismayilov.create.view.CustomBottomSheetDialog
import com.mismayilov.data.local.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

@AndroidEntryPoint
class CreateFragment :
    BaseFragment<FragmentCreateBinding, CreateViewModel, CreateState, CreateEvent, CreateEffect>() {

    private var selectedTabLayoutPosition = 0
    private var isNavigatedBack: Boolean = false
    private var isUpdate: Boolean = false
    private var showBalance by Delegates.notNull<Boolean>()

    override fun getViewModelClass(): Class<CreateViewModel> = CreateViewModel::class.java

    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBinding =
        { inflater, container, attachToRoot ->
            FragmentCreateBinding.inflate(inflater, container, attachToRoot)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NavigationManager).bottomNavigationVisibility(false)
        showBalance = SharedPreferencesManager.getValue("hideBalance", false)

        setTime()
        initClickListeners()
        initNumberPad()
        initTabLayout()
        selectDefaultTab()
        checkIsUpdate()
    }

    private fun checkIsUpdate() {
        val id = arguments?.getLong("id")
        if (id !=0L && id != null) {
            isUpdate = true
            binding.btnTransfer.text = getString(com.mismayilov.uikit.R.string.update)
            setEvent(CreateEvent.GetTransaction(id))
            binding.tabLayout.setTabEnabled(false)
        }
    }

    private fun selectDefaultTab() {
        val selectedIndex = arguments?.getInt("tabSelectedIndex")?: 0
        binding.tabLayout.selectTab(selectedIndex)
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

    private fun showBottomSheet(
        icons: List<IconModel>,
        onSelected: ((Long) -> Unit)
    ) {
        val bottomSheet = CustomBottomSheetDialog(showBalance ,icons = icons, onSelected = onSelected, addIconSelected = {
            val iconType = icons.first().type
            isNavigatedBack = true
            (activity as NavigationManager).navigateByNavigationName(if (iconType == IconType.ACCOUNT.name) "account_navigation" else "icon_navigation")
        })
        bottomSheet.show(parentFragmentManager, "CustomBottomSheet")
    }

    private fun setAmount(text: String) {
        val amount = binding.txtAmount.text.toString()
        setEvent(CreateEvent.AmountChanged(amount, text))
    }

    private fun initTabLayout() {
        binding.tabLayout.onClickListener = {
            selectedTabLayoutPosition = it
            checkTabPosition(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkTabPosition(position: Int) {
        if (isNavigatedBack) {
            isNavigatedBack = false
            return
        }else if (isUpdate) return
        when (position) {
            0 -> setEvent(CreateEvent.CategorySelected(IconType.EXPENSE))
            1 -> setEvent(CreateEvent.CategorySelected(IconType.INCOME))
            2 -> setEvent(CreateEvent.CategorySelected(IconType.ACCOUNT))
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initClickListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                (activity as NavigationManager).back()
            }

            btnDate.setOnClickListener {
                showDatePicker(requireContext()) {timeMillis ->
                    txtDate.text = SimpleDateFormat(MULTILINE_DATE_FORMAT).format(timeMillis)
                }
            }
            btnTransfer.setOnClickListener {
                val date = SimpleDateFormat(MULTILINE_DATE_FORMAT).parse(txtDate.text.toString())
                val note = transactionNote.text.toString()
                setEvent(CreateEvent.SaveData(date!!.time, note))
            }

            topCard.setOnClickListener {
                setEvent(CreateEvent.ClickCard(true))
            }

            bottomCard.setOnClickListener {
                setEvent(CreateEvent.ClickCard(false))
            }

            btnReverseCard.setOnClickListener {
                val position = tabLayout.currentSelectedTabPosition
                if (position == 2) {
                    setEvent(CreateEvent.ReverseCards)
                } else if (!isUpdate) tabLayout.selectTab(if (position == 0) 1 else 0)

            }
        }
    }

    override fun renderEffect(effect: CreateEffect) {
        when (effect) {
            is CreateEffect.ShowBottomSheet -> {
                showBottomSheet(effect.cardData) {
                    setEvent(CreateEvent.CardSelectedId(it, effect.isTop))
                }
            }
            is CreateEffect.CloseScreen -> {
                (activity as NavigationManager).back()
            }
            is CreateEffect.ShowToast -> {
                showToast(effect.message)
            }
        }
    }

    override fun renderState(state: CreateState) {
        state.selectedTopCard?.let {
            binding.topCardTitle.text = it.name
            binding.topCardIcon.setImageResource(getResourceIdByName(requireContext(), it.icon))
        }
        state.selectedBottomCard?.let {
            binding.bottomCardTitle.text = it.name
            binding.bottomCardIcon.setImageResource(getResourceIdByName(requireContext(), it.icon))
        }
        state.amount.let { binding.txtAmount.text = it }
        state.note.let { binding.transactionNote.setText(it) }
        state.selectTabPosition?.let { binding.tabLayout.selectTab(it) }
    }

    override fun onResume() {
        super.onResume()
        (activity as NavigationManager).bottomNavigationVisibility(false)
        if (isNavigatedBack) {
            binding.tabLayout.selectTab(selectedTabLayoutPosition)
        }
    }

}