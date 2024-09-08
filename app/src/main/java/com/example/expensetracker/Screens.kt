package com.example.expensetracker

import androidx.annotation.DrawableRes

sealed class Screens (
    val name : String,
    val route : String,
    @DrawableRes val icon : Int?
) {
    object Home : Screens(
        name = "Home",
        route = "route",
        icon = R.drawable.home
    )

    object Transactions : Screens(
        name = "Transactions",
        route = "transacations",
        icon = R.drawable.history
    )

    object Analytics : Screens(
        name = "Analytics",
        route = "analytics",
        icon = R.drawable.analytics
    )

    object Add_Transaction : Screens(
        name = "Add Transaction",
        route = "add_transaction",
        icon = null
    )

    object Edit_Transaction : Screens(
        name = "Edit Transaction",
        route = "edit_transaction",
        icon = null
    )
}

val categories = listOf(
    "Technology",
    "Food & Dining",
    "Transportation",
    "Clothing",
    "Housing",
    "Health & Wellness",
    "Entertainment & Leisure",
    "Personal Care",
    "Finance & Insurance",
    "Education",
    "Gifts & Donations",
    "Income",
    "Miscellaneous"
)
