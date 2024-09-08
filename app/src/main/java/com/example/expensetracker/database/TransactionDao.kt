package com.example.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addTransaction(transactionEntity: TransactionEntity)

    @Update
    abstract suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Delete
    abstract suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM `transactionentity` ORDER BY `date` DESC")
    abstract fun getAllTransactionsByDateDesc() : Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transactionentity` WHERE id=:id")
    abstract fun getTransactionById(id : Int) : Flow<TransactionEntity>

    @Query("SELECT * FROM `transactionentity` WHERE `item` LIKE '%' || :name || '%' ORDER BY `date` DESC")
    abstract fun getAllTransactionsByName(name : String) : Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transactionentity` WHERE `category` LIKE '%' || :category || '%' ORDER BY `date` DESC")
    abstract fun getAllTransactionsByCategory(category : String) : Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transactionentity` WHERE type LIKE '%' || :type || '%' ORDER BY `date` DESC")
    abstract fun getAllTransactionsByType(type : String) : Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transactionentity` WHERE amount LIKE :amount ORDER BY `date` DESC")
    abstract fun getAllTransactionsByAmount(amount : Double) : Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transactionentity` WHERE month LIKE '%' || :month || '%' ORDER BY `date` DESC")
    abstract fun getAllTransactionsByMonth(month : String) : Flow<List<TransactionEntity>>

    @Query("SELECT sum(`amount`) FROM `transactionentity` WHERE type = :type")
    abstract fun getSum(type : String) : Flow<Double>

    @Query("SELECT * FROM `transactionentity` ORDER BY `date` DESC LIMIT 3")
    abstract fun getRecentTransactions() : Flow<List<TransactionEntity>>

    @Query("SELECT min(`date`) FROM `transactionentity`")
    abstract fun getStartDate() : Flow<Long>

    @Query("SELECT max(`date`) FROM `transactionentity`")
    abstract fun getEndDate() : Flow<Long>

    @Query("SELECT * FROM `transactionentity` WHERE `date` BETWEEN :startDate AND :endDate AND `type` = 'Expense' ORDER BY date ASC")
    abstract fun getTransactionsBetweenDates(startDate : Long, endDate : Long) : Flow<List<TransactionEntity>>

    @Query("SELECT sum(`amount`) FROM transactionentity WHERE dayOfMonth = :dayOfMonth AND month = :month AND year = :year AND type = 'Expense'")
    abstract fun getSumAmountOfTransaction(dayOfMonth : Int, month : String, year : Int) : Flow<Int>

    @Query("SELECT sum(amount) FROM transactionentity WHERE category = :category and type = 'Expense'")
    abstract fun getCategoryWiseExpense(category: String) : Flow<Double>
}