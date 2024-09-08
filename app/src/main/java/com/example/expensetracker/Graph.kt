package com.example.expensetracker

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.database.TransactionDatabase
import com.example.expensetracker.database.TransactionRepository


object Graph {
    lateinit var database : TransactionDatabase

    val transactionRepository by lazy {
        TransactionRepository(
            database.getDao()
        )
    }

    fun initializeDatabase(context : Context){
        database = Room.databaseBuilder(
            context = context,
            klass = TransactionDatabase :: class.java,
            name = "transaction-db"
        ).build()
    }
}