package com.example.expensetracker

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.expensetracker.screens.AddTransactionScreen
import com.example.expensetracker.screens.AnalyticsScreen
import com.example.expensetracker.screens.EditTransactionScreen
import com.example.expensetracker.screens.HomeScreen
import com.example.expensetracker.screens.TransactionsScreen

@Composable
fun Navigation(
    navController : NavController,
    viewModel: MainViewModel,
    paddingValues : PaddingValues
) {

    NavHost(
        navController = navController as NavHostController,
        startDestination = Screens.Home.route,
    ) {
        composable(Screens.Home.route){
            HomeScreen(
                navController,
                viewModel
            )
        }
        composable(Screens.Transactions.route){
            TransactionsScreen(
                navController,
                viewModel
            )
        }
        composable(Screens.Analytics.route){
            AnalyticsScreen(
                viewModel
            )
        }
        composable(Screens.Add_Transaction.route){
            AddTransactionScreen(
                navController,
                viewModel
            )
        }
        composable(
            route = Screens.Edit_Transaction.route + "/{id}",
            arguments = listOf(
                navArgument(name = "id"){
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ){
            val id = it.arguments!!.getInt("id")
            EditTransactionScreen(
                navController = navController,
                viewModel = viewModel,
                transactionId = id
            )
        }
    }
}