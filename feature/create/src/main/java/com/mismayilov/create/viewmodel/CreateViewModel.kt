package com.mismayilov.create.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.core.generics.toEnum
import com.mismayilov.create.flow.CreateEffect
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.usecases.transaction.AddTransactionUseCase
import com.mismayilov.domain.usecases.account.GetAllAccountUseCase
import com.mismayilov.domain.usecases.icon.GetAllIconUseCase
import com.mismayilov.create.flow.CreateEvent
import com.mismayilov.create.flow.CreateState
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.domain.usecases.account.UpdateAccountAmountUseCase
import com.mismayilov.domain.usecases.remote.GetExchangeUseCase
import com.mismayilov.domain.usecases.transaction.GetTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
    private val getAllIconUseCase: GetAllIconUseCase,
    private val getAllAccountUseCase: GetAllAccountUseCase,
    private val getCurrencyUseCase: GetExchangeUseCase,
    private val updateAccountAmountUseCase: UpdateAccountAmountUseCase
) : BaseViewModel<CreateState, CreateEvent, CreateEffect>() {

    private var updateId: Long? = null
    private var exchangeFrom = "USD"
    private var exchangeTo = "USD"
    private var to = "USD"
    private var exchangeRate = 1.0
    private var currentUsdRate = 1.0
    private var currencySymbol = CurrencyType.USD.symbol
    private var currentIconType: IconType = IconType.EXPENSE
    private val _icons = MutableStateFlow<List<IconModel>>(emptyList())
    private val _accounts = MutableStateFlow<List<AccountModel>>(emptyList())

    override fun getInitialState(): CreateState = CreateState()

    init {
        fetchAllIcons()
    }

    override fun onEventChanged(event: CreateEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is CreateEvent.CategorySelected -> handleCategorySelected(event.categoryType)
                is CreateEvent.AmountChanged -> updateAmount(event.newNumber, event.lastAmount)
                is CreateEvent.SaveData -> saveTransactionData(event.date, event.note)
                is CreateEvent.ReverseCards -> reverseSelectedCards()
                is CreateEvent.CardSelectedId -> selectCardById(event.id, event.isTopCard)
                is CreateEvent.ClickCard -> showCardSelection(event.isTop)
                is CreateEvent.GetTransaction -> updateId = event.id
            }
        }
    }

    private fun getTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            getTransactionUseCase(updateId!!)
                .onEach { transaction ->
                    currentIconType = IconType.valueOf(transaction.category.type)
                    val accountModel = transaction.account
                    val categoryModel = transaction.category
                    val accountToModel = transaction.accountTo?: accountModel
                    val topSelectedCard =
                        if (currentIconType == IconType.INCOME) _icons.value.find { it.id == categoryModel.id } else _icons.value.find { it.type == IconType.ACCOUNT.name && it.name == accountModel!!.name }
                    val bottomSelectedCard =
                        if (currentIconType == IconType.EXPENSE) _icons.value.find { it.id == categoryModel.id } else _icons.value.find { it.type == IconType.ACCOUNT.name && it.name == accountToModel!!.name }
                    setState(
                        getCurrentState().copy(
                            selectedTopCard = topSelectedCard,
                            selectedBottomCard = bottomSelectedCard,
                            amount = transaction.amount.toString(),
                            note = transaction.note ?: ""
                        )
                    )
//                        validateAndSetCurrency()
                }
                .collect()
        }
    }

    private fun showCardSelection(isTop: Boolean) {
        setEffect(CreateEffect.ShowBottomSheet(isTop, getCardData(isTop)))
    }

    private suspend fun selectCardById(id: Long, isTopCard: Boolean) {
        val determinedCardType = determineCardTypes(currentIconType)
        val selectedCard = _icons.value.find {
            it.id == id &&
                    IconType.valueOf(it.type) == if (isTopCard) determinedCardType.first else determinedCardType.second
        }

        if (isTopCard) setState(getCurrentState().copy(selectedTopCard = selectedCard))
        else setState(getCurrentState().copy(selectedBottomCard = selectedCard))

        validateAndSetCurrency()
    }

    private fun validateAndSetCurrency() {
        if (areBothCardsAccounts()) {
            setCurrencyBasedOnCards()
        } else {
            setCurrencyBasedOnAccount()
        }
    }

    private suspend fun reverseSelectedCards() {
        viewModelScope.launch(Dispatchers.IO) {
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
            validateAndSetCurrency()
        }
    }

    private suspend fun saveTransactionData(date: Long, note: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = getCurrentState().amount.toDouble()
            if (amount == 0.0) {
                setEffect(CreateEffect.ShowToast("Amount cannot be 0"))
                return@launch
            }
            val changedAmount = if (currentIconType == IconType.INCOME) amount else -amount
            val accountId = getAccountId()
            val accountModel = _accounts.value.find { it.id == accountId }
            val categoryModel =
                if (currentIconType == IconType.EXPENSE) getCurrentState().selectedBottomCard!! else getCurrentState().selectedTopCard!!
            val accountToIconModel = getCurrentState().selectedBottomCard!!.id
            val accountToModel = if (currentIconType == IconType.ACCOUNT)
                _accounts.value.find { it.id == accountToIconModel }
            else null

            val transaction = TransactionModel(
                amount = amount,
                amountUsd = amount / currentUsdRate,
                account = accountModel,
                date = date,
                category = categoryModel,
                note = note,
                accountTo = accountToModel,
                amountTo = amount * exchangeRate,
            )

            addTransactionUseCase(transaction)
            updateAccountAmountUseCase.invoke(accountModel!!.id, changedAmount)
            setEffect(CreateEffect.CloseScreen)
        }
    }

    private fun getCardData(isTop: Boolean = false): List<IconModel> {
        val cardType = determineCardTypes(currentIconType).let {
            if (isTop) it.first else it.second
        }
        return _icons.value.filter { IconType.valueOf(it.type) == cardType }
    }

    private fun handleCategorySelected(iconType: IconType) {
        if (updateId != null) getTransaction()
        else {
            currentIconType = iconType
            val topCardData = getCardData(true)
            val bottomCardData = getCardData()
            if (bottomCardData.isEmpty() || topCardData.isEmpty()) return
            updateStateWithNewCardData(topCardData, bottomCardData)
        }
    }

    private fun determineCardTypes(iconType: IconType): Pair<IconType, IconType> {
        return when (iconType) {
            IconType.INCOME -> IconType.INCOME to IconType.ACCOUNT
            IconType.EXPENSE -> IconType.ACCOUNT to IconType.EXPENSE
            else -> IconType.ACCOUNT to IconType.ACCOUNT
        }
    }

    private fun updateStateWithNewCardData(
        topCardData: List<IconModel>,
        bottomCardData: List<IconModel>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                setState(
                    getCurrentState().copy(
                        selectedTopCard = topCardData.firstOrNull(),
                        selectedBottomCard = bottomCardData.firstOrNull()
                    )
                )
                validateAndSetCurrency()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateAmount(text: String, amount: String) {
        if (amount.length > 15) return
        if (amount == "0" && text != ".") {
            setState(getCurrentState().copy(amount = text))
        } else if (!(amount.contains(".") && text == "." || (amount.isEmpty() && text == "."))) {
            setState(getCurrentState().copy(amount = amount + text))
        }
    }


    private fun fetchAllIcons() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllIconUseCase()
                .onEach { icons ->
                    _icons.value = icons
                    fetchAllAccounts()
                }
                .collect()
        }
    }

    private fun fetchAllAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllAccountUseCase()
                .onEach { accounts ->
                    _accounts.value = accounts
                    _icons.value += accounts.map {
                        IconModel(it.name, it.icon, IconType.ACCOUNT.name, it.id)
                    }
                    _icons.value = _icons.value.distinct()
                    _accounts.value = _accounts.value.distinct()
                    handleCategorySelected(currentIconType)
                }
                .collect()
        }
    }

    private fun setCurrencyBasedOnAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            val accountId = getAccountId() ?: return@launch
            val fromCurrency = _accounts.value.find { it.id == accountId }!!.currency
            fetchCurrencyRate(fromCurrency, "USD")
        }
    }

    private fun getAccountId(): Long? {
        return if (getCurrentState().selectedTopCard?.type == IconType.ACCOUNT.name)
            getCurrentState().selectedTopCard?.id
        else if (getCurrentState().selectedBottomCard?.type == IconType.ACCOUNT.name)
            getCurrentState().selectedBottomCard?.id
        else null
    }

    private fun setCurrencyBasedOnCards() {
        viewModelScope.launch(Dispatchers.IO) {
            val topCardCurrency =
                _accounts.value.find { it.id == getCurrentState().selectedTopCard?.id }?.currency
            val bottomCardCurrency =
                _accounts.value.find { it.id == getCurrentState().selectedBottomCard?.id }?.currency

            topCardCurrency?.let { fetchCurrencyRate(it, bottomCardCurrency!!) }
            fetchCurrencyRate(topCardCurrency!!, "USD")
        }
    }

    private fun fetchCurrencyRate(from: String, to: String) {
        exchangeTo = to
        exchangeFrom = from
        viewModelScope.launch(Dispatchers.IO) {
            invoke(
                kSuspendFunction = { getCurrencyUseCase(from, to) },
                onError = { e -> Log.e("FetchCurrencyRate", "Error: $e") },
                onSuccess = { result ->
                    if (to == "USD") currentUsdRate = result.toDouble()
                    else exchangeRate = result.toDouble()
                    Log.d("FetchCurrencyRate", "Result: $result")
                }
            )
        }
    }

    private fun areBothCardsAccounts(): Boolean {
        return getCurrentState().selectedTopCard!!.type == IconType.ACCOUNT.name &&
                getCurrentState().selectedBottomCard!!.type == IconType.ACCOUNT.name
    }
}
