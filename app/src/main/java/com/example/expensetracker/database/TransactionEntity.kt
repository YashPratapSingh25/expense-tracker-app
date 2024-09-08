package com.example.expensetracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val item : String = "",
    val description : String = "",
    val category : String,
    val type : String,
    val date : Long,
    val dayOfMonth : Int,
    val month : String,
    val year : Int,
    val amount : Double
)
