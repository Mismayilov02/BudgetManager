package com.mismayilov.create.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mismayilov.common.extensions.toCurrencyString
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.viewmodel.BaseViewModel
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

    private var updateAccountId: Long? = null
    private var updateAccountToId: Long? = null
    private var updateAccountAmount: Double = 0.0
    private var updateAccountToAmount: Double = 0.0
    private var updateId: Long? = null
    private var exchangeRate = 1.0
    private var exchangeRateUsd = 1.0
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
            getTransactionUseCase(updateId!!).collect { transaction ->
                updateTransactionState(transaction)
                val topSelectedCard = getTopCard(transaction)
                val bottomSelectedCard = getBottomCard(transaction)

                setCurrencyBasedOnCards(topSelectedCard, bottomSelectedCard)
                setState(
                    getCurrentState().copy(
                        selectedTopCard = topSelectedCard,
                        selectedBottomCard = bottomSelectedCard,
                        amount = transaction.amount.toString(),
                        note = transaction.note ?: "",
                        selectTabPosition = getTabPosition()
                    )
                )
            }
        }
    }

    private fun getTopCard(transaction: TransactionModel): IconModel {
        return if (currentIconType == IconType.INCOME) transaction.category else transaction.account?.let {
            IconModel(
                it.name,
                it.icon,
                IconType.ACCOUNT.name,
                "${it.amount.toCurrencyString()}${CurrencyType.valueOf(it.currency).symbol}",
                it.id
            )
        }!!
    }

    private fun getBottomCard(transaction: TransactionModel): IconModel {
        return if (currentIconType == IconType.EXPENSE) transaction.category else if (transaction.accountTo != null) {
            IconModel(
                transaction.accountTo!!.name,
                transaction.accountTo!!.icon,
                IconType.ACCOUNT.name,
                "${transaction.accountTo!!.amount}${CurrencyType.valueOf(transaction.accountTo!!.currency).symbol}",
                transaction.accountTo!!.id
            )
        } else {
            IconModel(
                transaction.account!!.name,
                transaction.account!!.icon,
                IconType.ACCOUNT.name,
                "${transaction.account!!.amount}${CurrencyType.valueOf(transaction.account!!.currency).symbol}",
                transaction.account!!.id
            )
        }
    }

    private fun updateTransactionState(transaction: TransactionModel) {
        currentIconType = IconType.valueOf(transaction.category.type)
        updateAccountId = transaction.account?.id
        updateAccountToId = transaction.accountTo?.id
        updateAccountAmount = transaction.amount
        updateAccountToAmount = transaction.amountTo ?: 0.0
    }

    private fun getTabPosition(): Int {
        return when (currentIconType) {
            IconType.EXPENSE -> 0
            IconType.INCOME -> 1
            else -> 2
        }
    }

    private fun showCardSelection(isTop: Boolean) {
        setEffect(CreateEffect.ShowBottomSheet(isTop, getCardData(isTop)))
    }

    private fun selectCardById(id: Long, isTopCard: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val determinedCardType = determineCardTypes(currentIconType)
            val selectedCard = _icons.value.find {
                it.id == id && IconType.valueOf(it.type) == if (isTopCard) determinedCardType.first else determinedCardType.second
            }

            if (isTopCard) {
                setCurrencyBasedOnCards(selectedCard!!, getCurrentState().selectedBottomCard!!)
                setState(getCurrentState().copy(selectedTopCard = selectedCard))
            } else {
                setCurrencyBasedOnCards(getCurrentState().selectedTopCard!!, selectedCard!!)
                setState(getCurrentState().copy(selectedBottomCard = selectedCard))
            }
        }
    }


    private suspend fun reverseSelectedCards() {
        viewModelScope.launch(Dispatchers.IO) {
            currentIconType = when (currentIconType) {
                IconType.INCOME -> IconType.EXPENSE
                IconType.EXPENSE -> IconType.INCOME
                else -> IconType.ACCOUNT
            }
            if (currentIconType == IconType.ACCOUNT) setCurrencyBasedOnCards(
                getCurrentState().selectedBottomCard!!, getCurrentState().selectedTopCard!!
            )
            setState(
                getCurrentState().copy(
                    selectedTopCard = getCurrentState().selectedBottomCard,
                    selectedBottomCard = getCurrentState().selectedTopCard
                )
            )
        }
    }

    private fun saveTransactionData(date: Long, note: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val amount = getCurrentState().amount.toDouble()
            if (!isAmountValid(amount)) return@launch
            if (!isTransferValid()) return@launch

            val changedAmount = getChangedAmount(amount)
            val accountModel = getAccountModel()
            val categoryModel = getCategoryModel()
            val accountToModel = getAccountToModel()

            val transaction = createTransactionModel(
                amount, date, note, accountModel, categoryModel, accountToModel
            )
            addTransactionUseCase(transaction, updateId != null)

            updateAccountAmounts(accountModel!!.id, changedAmount)
            accountToModel?.let {
                updateAccountAmounts(it.id, amount * exchangeRate - updateAccountToAmount)
            }
            setEffect(CreateEffect.CloseScreen)
        }
    }

    private fun isAmountValid(amount: Double): Boolean {
        return if (amount == 0.0) {
            setEffect(CreateEffect.ShowToast("Amount cannot be 0"))
            false
        } else true
    }

    private fun isTransferValid(): Boolean {
        return if (currentIconType == IconType.ACCOUNT && getCurrentState().selectedTopCard == getCurrentState().selectedBottomCard) {
            setEffect(CreateEffect.ShowToast("Cannot transfer to the same account"))
            false
        } else true
    }

    private fun getChangedAmount(amount: Double): Double {
        return if (currentIconType == IconType.INCOME) amount else -amount
    }

    private fun getAccountModel(): AccountModel? {
        val accountId = getAccountId()
        return _accounts.value.find { it.id == accountId }
    }

    private fun getCategoryModel(): IconModel {
        return if (currentIconType == IconType.EXPENSE) {
            getCurrentState().selectedBottomCard!!
        } else {
            getCurrentState().selectedTopCard!!
        }
    }

    private fun getAccountToModel(): AccountModel? {
        val accountToIconModel = getCurrentState().selectedBottomCard!!.id
        return if (currentIconType == IconType.ACCOUNT) {
            _accounts.value.find { it.id == accountToIconModel }
        } else null
    }

    private fun createTransactionModel(
        amount: Double,
        date: Long,
        note: String?,
        accountModel: AccountModel?,
        categoryModel: IconModel,
        accountToModel: AccountModel?
    ): TransactionModel {
        return TransactionModel(
            amount = amount,
            account = accountModel,
            date = date,
            category = categoryModel,
            note = note,
            accountTo = accountToModel,
            amountTo = amount * exchangeRate,
            amountToUsd = amount * exchangeRateUsd,
            id = updateId ?: 0
        )
    }

    private fun updateAccountAmounts(accountId: Long, amount: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAccountAmountUseCase.invoke(accountId, amount + updateAccountAmount)
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
        topCardData: List<IconModel>, bottomCardData: List<IconModel>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                setState(
                    getCurrentState().copy(
                        selectedTopCard = topCardData.firstOrNull(),
                        selectedBottomCard = bottomCardData.firstOrNull()
                    )
                )
                setCurrencyBasedOnCards(topCardData.first(), bottomCardData.first())
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
            getAllIconUseCase().onEach { icons ->
                _icons.value = icons
                fetchAllAccounts()
            }.collect()
        }
    }

    private fun fetchAllAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllAccountUseCase().onEach { accounts ->
                _accounts.value = accounts
                _icons.value += accounts.map {
                    IconModel(
                        it.name,
                        it.icon,
                        IconType.ACCOUNT.name,
                        "${it.amount.toCurrencyString()}${CurrencyType.valueOf(it.currency).symbol}",
                        it.id
                    )
                }
                _icons.value = _icons.value.distinct()
                _accounts.value = _accounts.value.distinct()
                handleCategorySelected(currentIconType)
            }.collect()
        }
    }


    private fun getAccountId(): Long? {
        return if (getCurrentState().selectedTopCard?.type == IconType.ACCOUNT.name) getCurrentState().selectedTopCard?.id
        else if (getCurrentState().selectedBottomCard?.type == IconType.ACCOUNT.name) getCurrentState().selectedBottomCard?.id
        else null
    }

    private fun setCurrencyBasedOnCards(selectedTopCard: IconModel, selectedBottomCard: IconModel) {
        if (!areBothCardsAccounts(selectedTopCard, selectedBottomCard)) {
            setUsdCurrency(selectedTopCard, selectedBottomCard)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val topCardCurrency = _accounts.value.find { it.id == selectedTopCard.id }?.currency
            val bottomCardCurrency =
                _accounts.value.find { it.id == selectedBottomCard.id }?.currency

            topCardCurrency?.let {
                fetchCurrencyRate(it, bottomCardCurrency!!)
                fetchCurrencyRate(it, CurrencyType.USD.name, true)
            }
        }
    }

    private fun setUsdCurrency(topCard: IconModel, bottomCard: IconModel) {
        val accountId = if (topCard.type == IconType.ACCOUNT.name) topCard.id else bottomCard.id
        val currencyType = _accounts.value.find { it.id == accountId }?.currency.let {
            CurrencyType.valueOf(it!!)
        }
        fetchCurrencyRate(currencyType.name, CurrencyType.USD.name, true)
    }

    private fun fetchCurrencyRate(from: String, to: String, isUsd: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            invoke(kSuspendFunction = { getCurrencyUseCase(from, to) },
                onError = { e -> Log.e("FetchCurrencyRate", "Error: $e") },
                onSuccess = { result ->
                    if (isUsd) exchangeRateUsd = result.toDouble()
                    else exchangeRate = result.toDouble()
                })
        }
    }

    private fun areBothCardsAccounts(
        selectedTopCard: IconModel, selectedBottomCard: IconModel
    ): Boolean {
        return selectedTopCard.type == IconType.ACCOUNT.name && selectedBottomCard.type == IconType.ACCOUNT.name
    }
}
