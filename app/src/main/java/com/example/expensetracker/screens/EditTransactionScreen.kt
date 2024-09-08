package com.example.expensetracker.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.MainViewModel
import com.example.expensetracker.R
import com.example.expensetracker.database.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun EditTransactionScreen(
    navController: NavController,
    viewModel : MainViewModel,
    transactionId : Int
) {
    val transactionById = viewModel.getTransactionById(transactionId).collectAsState(initial = null).value

    transactionById?.let { transaction ->
        var name by rememberSaveable{ mutableStateOf(transaction.item) }
        var description by rememberSaveable{ mutableStateOf(transaction.description) }
        var category by rememberSaveable{ mutableStateOf(transaction.category) }
        var type by rememberSaveable{ mutableStateOf(transaction.type) }
        var amount by rememberSaveable{ mutableStateOf(transaction.amount.toString()) }
        var date by rememberSaveable{ mutableStateOf(transaction.date) }
        var dayOfMonth by rememberSaveable{ mutableStateOf(transaction.dayOfMonth) }
        var month by rememberSaveable{ mutableStateOf(transaction.month) }
        var year by rememberSaveable{ mutableStateOf(transaction.year) }

        @Composable
        fun CustomOutlinedTextField(string : String, label : String, onStringChange : (String) -> Unit){

            OutlinedTextField(
                modifier = when (label) {
                    "Description (Optional)" -> {
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    }
                    "Amount" -> {
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    }
                    else -> {
                        Modifier.fillMaxWidth()
                    }
                },
                textStyle = when (label) {
                    "Transaction Name" -> {
                        TextStyle(
                            fontSize = 20.sp
                        )
                    }
                    "Amount" -> {
                        TextStyle(
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                    else -> {
                        TextStyle(
                            fontSize = 17.sp
                        )
                    }
                },
                value = string,
                onValueChange = { onStringChange(it) },
                label = { Text(text = label, fontSize = 20.sp, color = Color.Gray) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = colorResource(id = R.color.android_purple)
                ),
                keyboardOptions = if(label == "Amount") KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.light_gray))
                .padding(20.dp)

        ) {

            CustomOutlinedTextField(string = name, label = "Transaction Name"){
                nameChange ->
                name = nameChange
            }
            Spacer(modifier = Modifier.height(18.dp))
            CustomOutlinedTextField(string = description, label = "Description (Optional)"){
                descriptionChange ->
                description = descriptionChange
            }
            val categories = listOf(
                "Technology",
                "Food & Dining",
                "Transportation",
                "Utilities",
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

            var isCategoryExpanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(10))
                    .clickable { isCategoryExpanded = true }
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = category,
                        fontSize = 20.sp,
                        color = if(category == "Category") Color.Gray else Color.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = isCategoryExpanded,
                    onDismissRequest = { isCategoryExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach {
                        DropdownMenuItem(onClick = {
                            category = it
                            isCategoryExpanded = false
                        }) {
                            Text(text = it)
                        }
                    }
                }
            }

            val typeList = listOf("Income", "Expense")
            var isTypeExpanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(10))
                    .clickable { isTypeExpanded = true }
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = type,
                        fontSize = 20.sp,
                        color = if(type == "Type") Color.Gray else Color.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = isTypeExpanded,
                    onDismissRequest = { isTypeExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    typeList.forEach {
                        DropdownMenuItem(onClick = {
                            type = it
                            isTypeExpanded = false
                        }) {
                            Text(text = it)
                        }
                    }
                }
            }

            val calendar = Calendar.getInstance()
            val context = LocalContext.current

            val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())

            var formattedDate by rememberSaveable{ mutableStateOf(sdf.format(date)) }

            val datePickerDialog = DatePickerDialog(
                context,
                {
                    _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    formattedDate = sdf.format(calendar.time)
                    date = calendar.timeInMillis

                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
                    month = monthFormat.format(calendar.time)
                    year = calendar.get(Calendar.YEAR)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(10))
                    .clickable { datePickerDialog.show() }
            ){
                Text(
                    text = formattedDate,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Center)
                )
            }

            CustomOutlinedTextField(string = amount, label = "Amount"){
                    amountChange ->
                amount = amountChange
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if(
                            name == "" ||
                            category == "Category" ||
                            type == "Type" ||
                            formattedDate == "" ||
                            amount.toDoubleOrNull() == null
                        ){
                            Toast.makeText(context, "Please enter the fields properly.", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.updateTransaction(
                                TransactionEntity(
                                    id = transactionId,
                                    item = name,
                                    description = description,
                                    category = category,
                                    type = type,
                                    date = date,
                                    dayOfMonth = dayOfMonth,
                                    month = month,
                                    year = year,
                                    amount = amount.toDouble()
                                )
                            )
                            navController.navigateUp()
                            viewModel.navigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.android_purple),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Update Transaction", color = Color.White)
                }
                Button(
                    onClick = {
                        viewModel.deleteTransaction(
                            TransactionEntity(
                                id = transactionId,
                                item = name,
                                description = description,
                                category = category,
                                type = type,
                                date = date,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                amount = amount.toDouble()
                            )
                        )
                        navController.navigateUp()
                        viewModel.navigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.red)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Delete Transaction", color = Color.White)
                }
            }

        }
    }
}