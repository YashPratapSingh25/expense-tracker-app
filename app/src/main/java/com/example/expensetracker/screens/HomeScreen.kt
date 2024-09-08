package com.example.expensetracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expensetracker.MainViewModel
import com.example.expensetracker.R
import com.example.expensetracker.Screens

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_gray))
            .padding(20.dp),
    ) {
        LazyColumn {
            item {
                NavBar(name = "Yash")
                BalanceCard(viewModel)
                TransactionCard(viewModel){
                    navController.navigate(Screens.Transactions.route)
                    viewModel.changeScreen(Screens.Transactions)
                }
            }
        }
    }
}

@Composable
fun NavBar(
    name : String,
){
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello,",
                fontSize = 40.sp
            )
            Text(
                text = name,
                fontSize = 50.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Account Settings",
            Modifier.size(60.dp)
        )
    }
}

@Composable
fun BalanceCard(viewModel : MainViewModel) {
    
    @Composable
    fun BalanceCardItem(
        type : String,
        amount : String
    ){
        Row (
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Text(
                text = "$type : ",
                fontSize = 30.sp
            )
            Text(
                text = amount,
                color = if (type == "Expense"){
                    colorResource(id = R.color.red)
                } else if (type == "Income") {
                    colorResource(id = R.color.green)
                }
                else{
                    colorResource(id = R.color.android_purple)
                },
                fontSize = 30.sp
            )
        }
    }
    
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
            .clickable { },
        shape = RoundedCornerShape(25.dp)
    ) {
        Column (
            modifier = Modifier.padding(25.dp)
        ) {
            val totalIncome = viewModel.getSum("Income").collectAsState(initial = 0.0).value
            BalanceCardItem(type = "Income", amount = totalIncome.toString())
            val totalExpense = viewModel.getSum("Expense").collectAsState(initial = 0.0).value
            BalanceCardItem(type = "Expense", amount = totalExpense.toString())
            BalanceCardItem(type = "Balance", amount = (totalIncome - totalExpense).toString())
        }
    }
}

@Composable
fun TransactionCard(
    viewModel: MainViewModel,
    onCardClick : () -> Unit
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCardClick()
            },
        shape = RoundedCornerShape(25.dp)
    ) {

        val recentTransactions = viewModel.getRecentTransactions().collectAsState(initial = listOf()).value

        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Recent Transactions",
                fontSize = 25.sp,
                modifier = Modifier.padding(25.dp),

            )
            Divider(color = Color.Black)
            if(recentTransactions.isNotEmpty()){
                recentTransactions.forEach { transaction ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = transaction.item,
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = transaction.category,
                                fontSize = 15.sp,
                                color = Color.Gray
                            )
                        }
                        val sign = if(transaction.type == "Income") "+" else "-"
                        Text(
                            text = "$sign ${transaction.amount}",
                            color = if (transaction.type == "Income") colorResource(id = R.color.green) else colorResource(
                                id = R.color.red
                            ),
                            fontSize = 20.sp,
                        )
                    }
                    Text(
                        text = if (transaction.type == "Income") {
                            "Received on ${transaction.dayOfMonth} ${transaction.month}, ${transaction.year}"
                        } else {
                            "Paid on ${transaction.dayOfMonth} ${transaction.month}, ${transaction.year}"
                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        fontSize = 15.sp,
                    )
                    Divider(color = Color.Black)
                }
            }
        }
    }
}