package com.mismayilov.icon.viewmodel

import android.content.Context
import androidx.drawerlayout.R
import androidx.lifecycle.viewModelScope
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.usecases.icon.AddIconUseCase
import com.mismayilov.domain.usecases.transaction.GetTransactionIconUseCase
import com.mismayilov.icon.flow.create_icon.CreateIconEffect
import com.mismayilov.icon.flow.create_icon.CreateIconEvent
import com.mismayilov.icon.flow.create_icon.CreateIconState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateIconViewModel @Inject constructor(
    private val addIconUseCase: AddIconUseCase,
    private val getTransactionIconUseCase: GetTransactionIconUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel<CreateIconState, CreateIconEvent, CreateIconEffect>() {

    private val _icons = MutableStateFlow<List<IconModel>>(emptyList())
    val icons: Flow<List<IconModel>> = _icons

    override fun getInitialState(): CreateIconState = CreateIconState()

    init {
        getAllIcon()
    }

    private fun getAllIcon() {
        viewModelScope.launch(Dispatchers.IO) {
            val incomeIcons =
                context.resources.getStringArray(com.mismayilov.uikit.R.array.income_icons)
            val expenseIcons =
                context.resources.getStringArray(com.mismayilov.uikit.R.array.expense_icons)
            val transferIcons =
                context.resources.getStringArray(com.mismayilov.uikit.R.array.transfer_icons)
            val otherIcons =
                context.resources.getStringArray(com.mismayilov.uikit.R.array.other_icon)
            (incomeIcons + expenseIcons + transferIcons + otherIcons).map { name ->
                IconModel(
                    name = "",
                    icon = name,
                    type = ""
                )
            }.let {
                _icons.value = it
                setState(getCurrentState().copy(icons = _icons.value))
            }
        }
    }

    override fun onEventChanged(event: CreateIconEvent) {
        when (event) {
            is CreateIconEvent.AddIconClicked -> {
                if (event.name.isEmpty()) {
                    setEffect(CreateIconEffect.ShowToast("Please enter icon name"))
                } else {
                    saveIcon(
                        event.name,
                        event.iconPosition,
                        event.iconType,
                        event.isEdit,
                        event.id ?: 0
                    )
                }
            }
        }
    }

    private fun saveIcon(
        name: String,
        iconPosition: Int,
        iconType: String,
        isEdit: Boolean = false,
        id: Long = 0
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val icon = _icons.value[iconPosition]
            val iconModel = IconModel(name = name, icon.icon, iconType, id = id)
            addIconUseCase(iconModel)
            getTransactionIconUseCase(iconModel)
            setEffect(CreateIconEffect.ShowToast(if (isEdit) "Icon updated" else "Icon added"))
            setEffect(CreateIconEffect.CloseFragment)
        }

    }

}