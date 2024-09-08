package com.example.expensetracker

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.database.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _currentScreen : MutableState<Screens> = mutableStateOf(Screens.Home)
    val currentScreen : State<Screens> = _currentScreen

    private val _screenStack : MutableList<Screens> = mutableListOf(Screens.Home)
    val screenStack : List<Screens> = _screenStack

    fun changeScreen(newScreen : Screens){
        if(newScreen == Screens.Home){
            _screenStack.clear()
        }
        _currentScreen.value = newScreen
        _screenStack.add(newScreen)

    }

    fun navigateBack(){
        if(_screenStack.size > 1){
            _screenStack.removeLast()
            _currentScreen.value = _screenStack.last()
        }
    }

    private val _transactionRepository = Graph.transactionRepository

    fun addTransaction(transactionEntity: TransactionEntity){
        viewModelScope.launch {
            _transactionRepository.addTransaction(transactionEntity)
        }
    }

    fun updateTransaction(transactionEntity: TransactionEntity){
        viewModelScope.launch {
            _transactionRepository.updateTransaction(transactionEntity)
        }
    }

    fun deleteTransaction(transactionEntity: TransactionEntity){
        viewModelScope.launch {
            _transactionRepository.deleteTransaction(transactionEntity)
        }
    }

    fun getTransactionById(id : Int) : Flow<TransactionEntity> {
        return _transactionRepository.getTransactionById(id)
    }

    fun getAllTransactions() : Flow<List<TransactionEntity>>{
        return _transactionRepository.getAllTransactionsByDateDesc()
    }

    fun getAllTransactionsByName(name : String) : Flow<List<TransactionEntity>> {
        return _transactionRepository.getAllTransactionsByName(name)
    }


    fun getAllTransactionsByCategory(category : String) : Flow<List<TransactionEntity>>{
        return _transactionRepository.getAllTransactionsByCategory(category)
    }

    fun getAllTransactionsByType(type : String) : Flow<List<TransactionEntity>>{
        return _transactionRepository.getAllTransactionsByType(type)
    }

    fun getAllTransactionsByAmount(amount : Double) : Flow<List<TransactionEntity>>{
        return _transactionRepository.getAllTransactionByAmount(amount)
    }

    fun getAllTransactionsByMonth(month : String) : Flow<List<TransactionEntity>>{
        return _transactionRepository.getAllTransactionsByMonth(month)
    }

    fun getSum(type : String) : Flow<Double>{
        return _transactionRepository.getSum(type)
    }

    fun getRecentTransactions() : Flow<List<TransactionEntity>>{
        return _transactionRepository.getRecentTransactions()
    }

    fun getStartDate() : Flow<Long>{
        return _transactionRepository.getStartDate()
    }

    fun getEndDate() : Flow<Long>{
        return _transactionRepository.getEndDate()
    }

    fun getTransactionsBetweenDates(startDate: Long, endDate: Long): Flow<List<TransactionEntity>> {
        return _transactionRepository.getTransactionsBetweenDates(startDate, endDate)
    }

    fun getSumAmountOfTransaction(dayOfMonth : Int, month : String, year : Int) : Flow<Int>{
        return _transactionRepository.getSumAmountOfTransaction(dayOfMonth, month, year)
    }

    fun getCategoryWiseExpense(category: String) : Flow<Double>{
        return _transactionRepository.getCategoryWiseExpense(category)
    }
}