package com.example.expensetracker.database

import kotlinx.coroutines.flow.Flow

class TransactionRepository (private val transactionDao : TransactionDao) {

    suspend fun addTransaction(transaction : TransactionEntity){
        transactionDao.addTransaction(transaction)
    }

    suspend fun updateTransaction(transaction : TransactionEntity){
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction : TransactionEntity){
        transactionDao.deleteTransaction(transaction)
    }

    fun getAllTransactionsByDateDesc() : Flow<List<TransactionEntity>>{
        return transactionDao.getAllTransactionsByDateDesc()
    }

    fun getTransactionById(id : Int) : Flow<TransactionEntity>{
        return transactionDao.getTransactionById(id)
    }

    fun getAllTransactionsByName(name : String) : Flow<List<TransactionEntity>>{
        return transactionDao.getAllTransactionsByName(name)
    }

    fun getAllTransactionsByCategory(category : String) : Flow<List<TransactionEntity>>{
        return transactionDao.getAllTransactionsByCategory(category)
    }

    fun getAllTransactionsByType(type : String) : Flow<List<TransactionEntity>>{
        return transactionDao.getAllTransactionsByType(type)
    }

    fun getAllTransactionByAmount(amount : Double) : Flow<List<TransactionEntity>>{
        return transactionDao.getAllTransactionsByAmount(amount)
    }

    fun getAllTransactionsByMonth(month : String) : Flow<List<TransactionEntity>>{
        return transactionDao.getAllTransactionsByMonth(month)
    }

    fun getSum(type : String) : Flow<Double>{
        return transactionDao.getSum(type)
    }

    fun getRecentTransactions() : Flow<List<TransactionEntity>>{
        return transactionDao.getRecentTransactions()
    }

    fun getStartDate() : Flow<Long>{
        return transactionDao.getStartDate()
    }

    fun getEndDate() : Flow<Long>{
        return transactionDao.getEndDate()
    }

    fun getTransactionsBetweenDates(startDate : Long, endDate : Long) : Flow<List<TransactionEntity>>{
        return transactionDao.getTransactionsBetweenDates(startDate, endDate)
    }

    fun getSumAmountOfTransaction(dayOfMonth : Int, month : String, year : Int) : Flow<Int>{
        return transactionDao.getSumAmountOfTransaction(dayOfMonth, month, year)
    }

    fun getCategoryWiseExpense(category: String) : Flow<Double>{
        return transactionDao.getCategoryWiseExpense(category)
    }
}