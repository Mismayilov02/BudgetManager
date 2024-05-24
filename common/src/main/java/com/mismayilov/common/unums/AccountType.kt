package com.mismayilov.common.unums

enum class AccountType(val value: String? = null) {
    CASH("Cash"),
    BANK("Bank"),
    CREDIT_CARD("Credit card"),
    DEBIT_CARD("Debit card"),
    DEPOSIT("Deposit"),
    INVESTMENT("Investment"),
    LOAN("Loan"),
    MORTGAGE("Mortgage"),
    OTHER("Other"),
}