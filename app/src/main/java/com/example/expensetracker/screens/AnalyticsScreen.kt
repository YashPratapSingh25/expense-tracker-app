package com.example.expensetracker.screens

import android.app.DatePickerDialog
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.expensetracker.MainViewModel
import com.example.expensetracker.R
import com.example.expensetracker.categories
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: MainViewModel
) {
    val sdf = SimpleDateFormat("dd MMMM", Locale.getDefault())

    val startDateInDb = viewModel.getStartDate().collectAsState(initial = null).value
    var startDate by rememberSaveable { mutableStateOf(0L) }
    var formattedStartDate by rememberSaveable { mutableStateOf(sdf.format(startDate)) }
    startDateInDb?.let {
        startDate = it
        formattedStartDate = sdf.format(it)
    }

    val endDateInDb = viewModel.getEndDate().collectAsState(initial = null).value
    var endDate by rememberSaveable { mutableStateOf(0L) }
    var formattedEndDate by rememberSaveable { mutableStateOf(sdf.format(endDate)) }
    endDateInDb?.let {
        endDate = it
        formattedEndDate = sdf.format(it)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_gray))
            .padding(20.dp),
    ) {
        item {
            Card {
                val transactions =
                    viewModel.getTransactionsBetweenDates(startDate, endDate).collectAsState(
                        initial = listOf()
                    ).value

                if (transactions.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        Text(
                            text = "Expense History",
                            modifier = Modifier
                                .padding(start = 36.dp, top = 20.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        var sumOfAmount = mutableListOf<Double>()
                        var dates = mutableListOf<Int>()

                        for (transaction in transactions) {
                            val amount = viewModel.getSumAmountOfTransaction(
                                transaction.dayOfMonth,
                                transaction.month,
                                transaction.year
                            ).collectAsState(initial = 0.0).value.toDouble()
                            if (amount != 0.0) {
                                sumOfAmount.add(amount)
                                sumOfAmount = sumOfAmount.distinct().toMutableList()
                            }
                            val date = transaction.dayOfMonth
                            dates.add(date)
                            dates = dates.distinct().toMutableList()
                        }

                        val entries = mutableListOf<Entry>()
                        for (i in sumOfAmount.indices) {
                            val entry = Entry(dates[i].toFloat(), sumOfAmount[i].toFloat())
                            entries.add(entry)
                        }

                        if (entries.isNotEmpty()) {
                            val dataSet = LineDataSet(entries, "Expenses").apply {
                                color = Color.RED
                                lineWidth = 4f
                            }
                            val lineData = LineData(dataSet)

                            AndroidView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                factory = { context ->
                                    LineChart(context).apply {
                                        data = lineData
                                        animateXY(1000, 1000)

                                        xAxis.apply {
                                            position = XAxis.XAxisPosition.BOTTOM
                                            labelCount = entries.size
                                            granularity = 1f
                                            setDrawGridLines(true)
                                        }

                                        axisLeft.apply {
                                            setDrawGridLines(true)
                                            axisMinimum = 0f
                                        }

                                        axisRight.isEnabled = false

                                        description.isEnabled = false
                                        legend.isEnabled = true

                                    }
                                }
                            )
                        }

                    }
                } else {
                    Text("No transactions found")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            val totalExpense = viewModel.getSum("Expense").collectAsState(initial = 0.0).value
            val categoryWiseExpense = mutableListOf<Double>()
            categories.forEach {
                val expense: Double? =
                    viewModel.getCategoryWiseExpense(it).collectAsState(initial = 0.0).value
                if (expense != 0.0 && expense != null) {
                    categoryWiseExpense.add(expense)
                } else {
                    categoryWiseExpense.add(0.0)
                }

            }
            Card {
                if (categoryWiseExpense.isNotEmpty() && totalExpense != 0.0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        Text(
                            text = "Category Wise Expense History",
                            modifier = Modifier
                                .padding(start = 36.dp, top = 20.dp, bottom = 20.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        val pieEntry = mutableListOf<PieEntry>()

                        for (i in categoryWiseExpense.indices) {
                            if (categoryWiseExpense[i] == 0.0) continue
                            val percent = ((categoryWiseExpense[i] / totalExpense) * 100).toFloat()
                            val label = categories[i]
                            val entry = PieEntry(percent, label)
                            pieEntry.add(entry)
                        }


                        val pieDataSet = PieDataSet(pieEntry, "Category Wise Expense").apply {
                            setColors(
                                intArrayOf(
                                    Color.parseColor("#E57373"), // Red
                                    Color.parseColor("#81C784"), // Green
                                    Color.parseColor("#64B5F6"), // Blue
                                    Color.parseColor("#FFF176"), // Yellow
                                    Color.parseColor("#FFB74D"), // Orange
                                    Color.parseColor("#9575CD"), // Purple
                                    Color.parseColor("#4DD0E1"), // Cyan
                                    Color.parseColor("#F06292"), // Pink
                                    Color.parseColor("#4DB6AC"), // Teal
                                    Color.parseColor("#FF8A65"), // Deep Orange
                                    Color.parseColor("#DCE775"), // Lime
                                    Color.parseColor("#7986CB"), // Indigo
                                    Color.parseColor("#A1887F")  // Brown
                                ), 255
                            )
                            valueTextSize = 15f
                            valueTextColor = Color.BLACK
                        }

                        val pieData = PieData(pieDataSet)


                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                PieChart(context).apply {
                                    data = pieData
                                    animateXY(1000, 1000)
                                    centerText = "Expenses"
                                    setDrawEntryLabels(false)
                                    description.isEnabled = false

                                    legend.apply {
                                        textSize = 10f
                                        isEnabled = true
                                        verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                                        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                                        orientation = Legend.LegendOrientation.VERTICAL
                                        textSize = 12f
                                        form = Legend.LegendForm.CIRCLE
                                        formSize = 12f
                                        xEntrySpace = 10f
                                        yEntrySpace = 10f
                                        formToTextSpace = 8f
                                    }

                                    invalidate()
                                }
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}