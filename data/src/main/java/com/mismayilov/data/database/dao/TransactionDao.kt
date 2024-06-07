package com.mismayilov.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.entities.local.TransactionAmountsModel
import com.mismayilov.domain.entities.local.TransactionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg note: TransactionModel)

    @Query("SELECT * FROM 'transaction' ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionModel>>

    @Query("SELECT * FROM 'transaction' WHERE id=:id")
    fun getById(id: Long): Flow<TransactionModel>

    @Query("SELECT * FROM 'transaction' ORDER BY date DESC LIMIT :limit")
    fun getWithLimit(limit: Int): Flow<List<TransactionModel>>

    @Query("SELECT * FROM 'transaction' WHERE date >= :startDate AND date <= :endDate")
    fun getTransactionByTimeRange(startDate: Long, endDate: Long): Flow<List<TransactionModel>>

    @Query(
        """
    SELECT IFNULL(SUM(t.amount), 0)
    FROM `transaction` t
    WHERE (t.category LIKE '%EXPENSE%' OR t.category LIKE '%ACCOUNT%')
    AND t.account LIKE '%"id":' || (SELECT id FROM account WHERE is_pinned = 1)|| '%'
    """
    )
    fun getSumExpense(): Flow<Double>


    @Query(
        """
    SELECT IFNULL(SUM(t.amount_to), 0)
    FROM `transaction` t
    WHERE 
        ((t.category LIKE '%ACCOUNT%' 
        AND t.account_to LIKE '%"id":' || (SELECT id FROM account WHERE is_pinned = 1)|| '%')
        OR
        (t.category LIKE '%INCOME%'
        AND t.account LIKE '%"id":' || (SELECT id FROM account WHERE is_pinned = 1)|| '%'))
   """
    )
    fun getSumIncome(): Flow<Double>


    @Delete
    fun delete(note: TransactionModel)

    @Query("DELETE FROM 'transaction' WHERE id=:id")
    suspend fun deleteTransaction(id: Long)

    @Update
    suspend fun update(note: TransactionModel)


    @Query("SELECT * FROM 'transaction' WHERE id=:transactionId")
    suspend fun getTransactionById(transactionId: Long): TransactionModel

    @Query("UPDATE account SET amount = amount + :addAmount WHERE id = :accountId")
    suspend fun updateAccountAmount(accountId: Long, addAmount: Double)

    @Query("""UPDATE `transaction` SET account = :account WHERE account LIKE '%"id":' || :accountId || '%' OR
        account_to LIKE '%"id":' || :accountId || '%'""")
    suspend fun updateTransactionAccount(account: AccountModel , accountId: Long)

    @Query("""UPDATE `transaction` SET category = :icon WHERE category LIKE '%"id":' || :iconId || '%'""")
    suspend fun updateTransactionIcon(icon: IconModel, iconId: Long)


    @Transaction
    suspend fun deleteTransactionAndUpdateAccount(transactionId: Long) {
        val transaction = getTransactionById(transactionId)
        if (transaction.category.type == "INCOME") updateAccountAmount(
            transaction.account!!.id,
            -transaction.amount
        )
        else updateAccountAmount(transaction.account!!.id, transaction.amount)
        if (transaction.accountTo != null) updateAccountAmount(
            transaction.accountTo!!.id,
            -transaction.amountTo!!
        )
        deleteTransaction(transactionId)
    }

    @Query("SELECT IFNULL(SUM(ammount_to_usd), 0.0) FROM `transaction` WHERE category LIKE '%ACCOUNT%'")
    fun getTransferAmount(): Flow<Double>

    @Query("SELECT IFNULL(SUM(ammount_to_usd), 0.0) FROM `transaction` WHERE category LIKE '%EXPENSE%'")
    fun getExpenseAmount(): Flow<Double>

    @Query("SELECT IFNULL(SUM(ammount_to_usd), 0.0) FROM `transaction` WHERE category LIKE '%INCOME%'")
    fun getIncomeAmount(): Flow<Double>

}