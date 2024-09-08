package com.example.expensetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TransactionEntity :: class],
    version = 2
)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun getDao() : TransactionDao
}