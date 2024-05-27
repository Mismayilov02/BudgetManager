package com.mismayilov.create.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.mismayilov.domain.usecases.AddTransactionUseCase
import com.mismayilov.domain.usecases.GetAllAccountUseCase
import com.mismayilov.common.unums.IconType
import com.mismayilov.domain.usecases.account.GetAllIconUseCase
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.create.flow.CreateEffect
import com.mismayilov.create.flow.CreateEvent
import com.mismayilov.create.flow.CreateState
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.IconModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getAllCategoryUseCase: GetAllIconUseCase,
    private val getAllAccountUseCase: GetAllAccountUseCase
) : BaseViewModel<CreateState, CreateEvent, CreateEffect>() {
    override fun getInitialState(): CreateState = CreateState()

    private var currentIconType: IconType = IconType.EXPENSE
    private val _icons = MutableStateFlow<List<IconModel>>(emptyList())
    val icons: StateFlow<List<IconModel>> = _icons
    private val _accounts = MutableStateFlow<List<AccountModel>>(emptyList())
    val accounts: StateFlow<List<AccountModel>> = _accounts

    init {
        getAllIcon()
        getAllAccounts()
    }

    override fun onEventChanged(event: CreateEvent) {
        when (event) {
            is CreateEvent.CategorySelected -> categorySelected(event.categoryType)
            is CreateEvent.AmountChanged -> setAmount(event.newNumber, event.lastAmount)
            is CreateEvent.SaveData -> saveData()
            is CreateEvent.ReverseCards -> reverseCards()
            is CreateEvent.CardSelectedId -> cardSelected(event.id, event.isTopCard)
        }
    }


    private fun cardSelected(id: Long, isTopCard: Boolean) {
        val selectedTopCard = _icons.value.find { it.id == id }
        if (isTopCard) setState(getCurrentState().copy(selectedTopCard = selectedTopCard))
        else setState(getCurrentState().copy(selectedBottomCard = selectedTopCard))

    }

    private fun reverseCards() {
        setState(
            getCurrentState().copy(
                topCardData = getCurrentState().bottomCardData,
                bottomCardData = getCurrentState().topCardData,
                selectedTopCard = getCurrentState().selectedBottomCard,
                selectedBottomCard = getCurrentState().selectedTopCard
            )
        )
    }

    private fun saveData() {
        TODO()
    }

    private fun categorySelected(iconType: IconType) {
        currentIconType = iconType
        val (topCardType, bottomCardType) = determineCardTypes(iconType)
        val accountCardData = createAccountCardData()
        val topCardData = getCardData(topCardType, accountCardData)
        val bottomCardData = getCardData(bottomCardType, accountCardData)

        updateStateWithCardData(topCardData, bottomCardData)
    }

    private fun determineCardTypes(iconType: IconType): Pair<IconType, IconType> {
        return when (iconType) {
            IconType.INCOME -> IconType.INCOME to IconType.ACCOUNT
            IconType.EXPENSE -> IconType.ACCOUNT to IconType.EXPENSE
            else -> IconType.ACCOUNT to IconType.ACCOUNT
        }
    }

    private fun createAccountCardData(): List<IconModel> {
        return _accounts.value.map { IconModel(it.name, it.icon, IconType.ACCOUNT.name, it.id) }
    }

    private fun getCardData(cardType: IconType, accountCardData: List<IconModel>): List<IconModel> {
        return if (cardType == IconType.ACCOUNT) {
            accountCardData
        } else {
            _icons.value.filter { IconType.valueOf(it.type) == cardType }
        }
    }

    private fun updateStateWithCardData(topCardData: List<IconModel>, bottomCardData: List<IconModel>) {
        setState(
            getCurrentState().copy(
                topCardData = topCardData,
                bottomCardData = bottomCardData,
                selectedTopCard = topCardData.firstOrNull(),
                selectedBottomCard = bottomCardData.firstOrNull()
            )
        )
    }
    private fun getAllIcon() {
        getAllCategoryUseCase().onEach { icons ->
            _icons.value = icons
            categorySelected(currentIconType)
        }.launchIn(viewModelScope)
    }

    private fun getAllAccounts() {
        getAllAccountUseCase().onEach { accounts ->
            _accounts.value = accounts
            categorySelected(currentIconType)
        }.launchIn(viewModelScope)
    }

    @SuppressLint("SetTextI18n")
    private fun setAmount(text: String, amount: String) {
        if (amount.length > 15) return
        if (amount == "0" && text != ".") setState(getCurrentState().copy(amount = text))
        else if ((amount.contains(".") && text == ".") || (amount.isEmpty() && text == ".")) return
        else setState(getCurrentState().copy(amount = amount + text))
    }

}