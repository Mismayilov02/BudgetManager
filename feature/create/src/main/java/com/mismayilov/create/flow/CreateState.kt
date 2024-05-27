package com.mismayilov.create.flow

import com.mismayilov.domain.entities.local.IconModel

data class CreateState (
    val topCardData:List<IconModel>?=null,
    val bottomCardData:List<IconModel>?=null,
    val selectedTopCard:IconModel?=null,
    val selectedBottomCard:IconModel?=null,
    var amount:String="0",
    val isLoading:Boolean=false,
)