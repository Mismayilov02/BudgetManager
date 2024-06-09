package com.mismayilov.reports.flow

import com.mismayilov.domain.entities.local.IconModel

data class ReportsState(
    val accounts: List<IconModel>? = null,
    val expenseAmount: Double = 0.0,
    val incomeAmount: Double = 0.0,
    val transactionAmount: Double = 0.0,
    val totalAmount: Double = 0.0
)