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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.example.expensetracker.MainViewModel
import com.example.expensetracker.R
import com.example.expensetracker.Screens
import com.example.expensetracker.categories
import com.example.expensetracker.database.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddTransactionScreen(
    navController : NavHostController,
    viewModel : MainViewModel
) {

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

        var name by rememberSaveable { mutableStateOf("") }
        var description by rememberSaveable { mutableStateOf("") }

        CustomOutlinedTextField(string = name, label = "Transaction Name"){
            nameChange ->
            name = nameChange
        }
        Spacer(modifier = Modifier.height(18.dp))
        CustomOutlinedTextField(string = description, label = "Description (Optional)"){
            descriptionChange ->
            description = descriptionChange
        }

        var categoryText by rememberSaveable{ mutableStateOf("Category") }
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
                    text = categoryText,
                    fontSize = 20.sp,
                    color = if(categoryText == "Category") Color.Gray else Color.Black
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
                        categoryText = it
                        isCategoryExpanded = false
                    }) {
                        Text(text = it)
                    }
                }
            }
        }

        val typeList = listOf("Income", "Expense")
        var typeText by rememberSaveable{ mutableStateOf("Type") }
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
                    text = typeText,
                    fontSize = 20.sp,
                    color = if(typeText == "Type") Color.Gray else Color.Black
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
                        typeText = it
                        isTypeExpanded = false
                    }) {
                        Text(text = it)
                    }
                }
            }
        }

        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        var selectedDate by rememberSaveable { mutableStateOf("") }
        var selectedDateInLong by rememberSaveable { mutableStateOf<Long?>(null) }

        var DayOfMonth by rememberSaveable{ mutableStateOf(0) }
        var Month by rememberSaveable{ mutableStateOf("") }
        var Year by rememberSaveable{ mutableStateOf(0) }

        val datePickerDialog = DatePickerDialog(
            context,
            {
                _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
                selectedDate = sdf.format(calendar.time)
                selectedDateInLong = calendar.timeInMillis

                DayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val monthTextFormat = SimpleDateFormat("MMMM", Locale.getDefault())
                Month = monthTextFormat.format(calendar.time)
                Year = calendar.get(Calendar.YEAR)
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
                text = if(selectedDate == "") "Select Date" else selectedDate,
                fontSize = 20.sp,
                color = if(selectedDate == "") Color.Gray else Color.Black,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center)
            )
        }


        var amount by rememberSaveable{ mutableStateOf("") }
        CustomOutlinedTextField(string = amount, label = "Amount"){
            amountChange ->
            amount = amountChange
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        FloatingActionButton(
            onClick = {
                if(
                    name == "" ||
                    categoryText == "Category" ||
                    typeText == "Type" ||
                    selectedDate == "" ||
                    amount.toDoubleOrNull() == null
                ){
                    Toast.makeText(context, "Please enter the fields properly.", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.addTransaction(
                        TransactionEntity(
                            item = name,
                            description = description,
                            category = categoryText,
                            type = typeText,
                            date = selectedDateInLong!!,
                            dayOfMonth = DayOfMonth,
                            month = Month,
                            year = Year,
                            amount = amount.toDouble()
                        )
                    )
                    navController.navigateUp()
                    viewModel.navigateBack()
                }
            },
            Modifier
                .size(70.dp)
                .align(Alignment.End),
            shape = CircleShape,
            containerColor = colorResource(id = R.color.android_purple)

        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Add Transaction", tint = Color.White)
        }
    }
}