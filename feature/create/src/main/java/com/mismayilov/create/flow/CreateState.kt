package com.mismayilov.create.flow

import com.mismayilov.domain.entities.local.IconModel

data class CreateState (
    val selectedTopCard:IconModel?=null,
    val selectedBottomCard:IconModel?=null,
    var amount:String="0",
    val note :String?=null,
    val selectTabPosition:Int?=null,
) {

}