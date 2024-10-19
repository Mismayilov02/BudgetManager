package com.mismayilov.icon.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.usecases.icon.DeleteIconUseCase
import com.mismayilov.domain.usecases.icon.GetAllIconUseCase
import com.mismayilov.icon.R
import com.mismayilov.icon.flow.icon_manager.IconManagerEffect
import com.mismayilov.icon.flow.icon_manager.IconManagerEvent
import com.mismayilov.icon.flow.icon_manager.IconManagerState
import com.mismayilov.uikit.util.getResourceIdByName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IconManagerViewModel @Inject constructor(
    private val getAllIconUseCase: GetAllIconUseCase,
    private val deleteIconUseCase: DeleteIconUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel<IconManagerState, IconManagerEvent, IconManagerEffect>() {

    private val _icons = MutableStateFlow<List<IconModel>>(emptyList())
    private var currentIconType = IconType.EXPENSE

    init {
        getIcons()
    }

    override fun onEventChanged(event: IconManagerEvent) {
        viewModelScope.launch {
            when (event) {
                is IconManagerEvent.IconTypeSelected -> {
                    currentIconType = event.iconType
                    setIcon()
                }
                is IconManagerEvent.DeleteIcon -> {
                    deleteIcon(event.id)
                }
            }
        }
    }

    private suspend fun deleteIcon(id: Long) {
        if (_icons.value.filter { it.type == currentIconType.name }.size == 1) {
            setEffect(IconManagerEffect.ShowToast(context.getString(com.mismayilov.uikit.R.string.cant_delete_icon)))
            return
        }
        deleteIconUseCase(id)
        _icons.value = _icons.value.filter { it.id != id }
        setState(getCurrentState().copy(icons = _icons.value))
    }

    private fun getIcons() {
        viewModelScope.launch {
            getAllIconUseCase()
                .onEach { icons ->
                    _icons.value = icons
                    setIcon()
                }
                .collect()
        }
    }

    private fun setIcon() {
        val icon = _icons.value.filter { it.type == currentIconType.name }
        setState(getCurrentState().copy(icons = icon))
    }

    override fun getInitialState(): IconManagerState = IconManagerState()
}
