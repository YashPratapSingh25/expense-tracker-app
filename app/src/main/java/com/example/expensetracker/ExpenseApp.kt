package com.example.expensetracker

import android.app.Application
import com.github.mikephil.charting.utils.Utils

class ExpenseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.initializeDatabase(this)
        Utils.init(this)
    }
}