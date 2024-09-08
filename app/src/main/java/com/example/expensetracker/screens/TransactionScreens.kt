package com.example.expensetracker.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.MainViewModel
import com.example.expensetracker.R
import com.example.expensetracker.Screens
import com.example.expensetracker.database.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val transactions = remember { mutableStateOf(listOf<TransactionEntity>()) }


    @Composable
    fun onQueryChange(query : String, filter : String, context : Context){
        if(filter == "Name"){
            transactions.value = viewModel.getAllTransactionsByName(query).collectAsState(initial = listOf()).value
        }else if(filter == "Category"){
            transactions.value = viewModel.getAllTransactionsByCategory(query).collectAsState(initial = listOf()).value
        }else if(filter == "Type"){
            transactions.value = viewModel.getAllTransactionsByType(query).collectAsState(initial = listOf()).value
        }else if(filter == "Amount"){
            if(query.toDoubleOrNull() != null){
                transactions.value = viewModel.getAllTransactionsByAmount(query.toDoubleOrNull()!!).collectAsState(
                    initial = listOf()
                ).value
            }else if(query != ""){
                Toast.makeText(context, "Please enter proper amount value", Toast.LENGTH_SHORT).show()
            }else{
                transactions.value = viewModel.getAllTransactions().collectAsState(initial = listOf()).value
            }
        }else if(filter == "Month"){
            transactions.value = viewModel.getAllTransactionsByMonth(query).collectAsState(initial = listOf()).value
        }else if(filter == "Name"){
            transactions.value = viewModel.getAllTransactions().collectAsState(initial = listOf()).value
        }
    }


    val query = rememberSaveable { mutableStateOf("") }
    val filter = rememberSaveable{ mutableStateOf("Name") }
    val isExpanded = remember{ mutableStateOf(false) }
    val filtersCategory = listOf("Name", "Category", "Type", "Amount", "Month")
    val context = LocalContext.current
    onQueryChange(query = query.value, filter = filter.value, context = context)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_gray))
            .padding(20.dp)
    ) {
        item {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query.value,
                    onValueChange = {
                        query.value = it
                    },
                    placeholder = { Text(text = "Search by ${filter.value}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.9f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isExpanded.value = true }, modifier = Modifier.weight(0.1f)) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_filter_list_24),
                                contentDescription = "Open Filter",
                                tint = Color.Black,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = isExpanded.value,
                    onDismissRequest = { isExpanded.value = false },
                    modifier = Modifier
                        .background(Color.White)
                        .fillParentMaxWidth()
                ) {
                    filtersCategory.forEach {
                        DropdownMenuItem(
                            onClick = {
                                filter.value = it
                                isExpanded.value = false
                            }
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = it)
                                if (filter.value == it){
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Black)
                                }
                            }
                        }

                        Divider(color = Color.LightGray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        items(transactions.value) { transactionEntry ->

            val date = Date(transactionEntry.date)
            val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            val formattedDate = sdf.format(date)

            val sign = if (transactionEntry.type == "Income") "+" else "-"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screens.Edit_Transaction.route + "/${transactionEntry.id}")
                        viewModel.changeScreen(Screens.Edit_Transaction)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = transactionEntry.item,
                            fontSize = 20.sp,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = transactionEntry.category,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = "$sign ${transactionEntry.amount}",
                        color = if (transactionEntry.type == "Income") colorResource(id = R.color.green) else colorResource(
                            id = R.color.red
                        ),
                        fontSize = 20.sp,
                    )
                }
                Text(
                    text = if (transactionEntry.type == "Income") {
                        "Received on $formattedDate"
                    } else {
                        "Paid on $formattedDate"
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    fontSize = 15.sp,
                )
                Divider()
            }

        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}