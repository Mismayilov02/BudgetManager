package com.mismayilov.create.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.create.flow.CreateEffect
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.usecases.AddTransactionUseCase
import com.mismayilov.domain.usecases.account.GetAllAccountUseCase
import com.mismayilov.domain.usecases.icon.GetAllIconUseCase
import com.mismayilov.create.flow.CreateEvent
import com.mismayilov.create.flow.CreateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getAllIconUseCase: GetAllIconUseCase,
    private val getAllAccountUseCase: GetAllAccountUseCase
) : BaseViewModel<CreateState, CreateEvent, CreateEffect>() {

    private var currentIconType: IconType = IconType.EXPENSE
    private val _icons = MutableStateFlow<List<IconModel>>(emptyList())
    val icons: StateFlow<List<IconModel>> = _icons

    override fun getInitialState(): CreateState = CreateState()

    init {
        getAllIcon()
        getAllAccounts()
    }

    override fun onEventChanged(event: CreateEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is CreateEvent.CategorySelected -> categorySelected(event.categoryType)
                is CreateEvent.AmountChanged -> setAmount(event.newNumber, event.lastAmount)
                is CreateEvent.SaveData -> saveData()
                is CreateEvent.ReverseCards -> reverseCards()
                is CreateEvent.CardSelectedId -> cardSelected(event.id, event.isTopCard)
                is CreateEvent.SelectCardData -> selectCardData(event.isTop)
            }
        }
    }

    private fun selectCardData(isTop: Boolean) {
        setEffect(
            CreateEffect.SelectCardData(
                isTop, getCardData(isTop)
            )
        )
    }

    private suspend fun cardSelected(id: Long, isTopCard: Boolean) {
        val selectedTopCard = _icons.value.find { it.id == id }
        if (isTopCard) setState(getCurrentState().copy(selectedTopCard = selectedTopCard))
        else setState(getCurrentState().copy(selectedBottomCard = selectedTopCard))
    }

    private suspend fun reverseCards() {
        currentIconType = when (currentIconType) {
            IconType.INCOME -> IconType.EXPENSE
            IconType.EXPENSE -> IconType.INCOME
            else -> IconType.EXPENSE
        }
        setState(
            getCurrentState().copy(
                selectedTopCard = getCurrentState().selectedBottomCard,
                selectedBottomCard = getCurrentState().selectedTopCard
            )
        )
    }

    private suspend fun saveData() {
        // TODO: Implement data saving logic
    }

    private fun getCardData(isTop: Boolean = false): List<IconModel> {
        _icons.value = _icons.value.distinct()
        val cardType =
            determineCardTypes(currentIconType).let { if (isTop) it.first else it.second }
        return _icons.value.filter { IconType.valueOf(it.type) == cardType }
    }

    private fun categorySelected(iconType: IconType) {
        currentIconType = iconType
        val topCardData = getCardData(true)
        val bottomCardData = getCardData()
        updateStateWithCardData(topCardData, bottomCardData)
    }
    private fun determineCardTypes(iconType: IconType): Pair<IconType, IconType> {
        return when (iconType) {
            IconType.INCOME -> IconType.INCOME to IconType.ACCOUNT
            IconType.EXPENSE -> IconType.ACCOUNT to IconType.EXPENSE
            else -> IconType.ACCOUNT to IconType.ACCOUNT
        }
    }

    private fun updateStateWithCardData(
        topCardData: List<IconModel>,
        bottomCardData: List<IconModel>
    ) {
        setState(
            getCurrentState().copy(
                selectedTopCard = topCardData.firstOrNull(),
                selectedBottomCard = bottomCardData.firstOrNull()
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setAmount(text: String, amount: String) {
        if (amount.length > 15) return
        if (amount == "0" && text != ".") setState(getCurrentState().copy(amount = text))
        else if ((amount.contains(".") && text == ".") || (amount.isEmpty() && text == ".")) return
        else setState(getCurrentState().copy(amount = amount + text))
    }

    private fun getAllIcon() {
        viewModelScope.launch {
            getAllIconUseCase()
                .onEach { icons ->
                    _icons.value += icons
                    categorySelected(currentIconType)
                }
                .collect()
        }
    }

    private fun getAllAccounts() {
        viewModelScope.launch {
            getAllAccountUseCase()
                .onEach { accounts ->
                    _icons.value += accounts.map {
                        IconModel(
                            it.name,
                            it.icon,
                            IconType.ACCOUNT.name,
                            it.id
                        )
                    }
                }
                .collect()
        }
    }
}
