package com.example.expensetracker

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun ExpenseTrackerApp() {

    val BottomNavigationListItems = listOf(
        Screens.Home,
        Screens.Transactions,
        Screens.Analytics
    )

    val navController = rememberNavController()

    val viewModel : MainViewModel = viewModel()

    val currentScreen = viewModel.currentScreen.value

    val topBar : @Composable () -> Unit = {
        if(currentScreen != Screens.Home) {
            TopBar(title =  viewModel.screenStack.last().name) {
                navController.navigateUp()
                viewModel.navigateBack()
            }
        }
    }

    val bottomBar : @Composable () -> Unit = {
        BottomNavigation (
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomNavigationListItems.forEach {
                screen ->
                BottomNavigationItem(
                    selected = currentScreen.route == screen.route,
                    onClick = {
                        if(screen.route != currentScreen.route) {
                            navController.navigate(screen.route)
                            viewModel.changeScreen(screen)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon!!),
                            contentDescription = screen.name
                        )
                    },
                    label = { Text(text = screen.name) }
                )
            }
        }
    }

    Scaffold (
        topBar = topBar ,
        bottomBar = bottomBar,
        floatingActionButton = {
            if(currentScreen == Screens.Home || currentScreen == Screens.Transactions || currentScreen == Screens.Analytics) {
                FAB () {
                    navController.navigate(Screens.Add_Transaction.route)
                    viewModel.changeScreen(Screens.Add_Transaction)
                }
            }
        },

    ) {

        Navigation(navController = navController, viewModel = viewModel, paddingValues = it)

        val activity = LocalContext.current as? Activity
        BackHandler {
            if(viewModel.screenStack.size == 1){
                activity?.moveTaskToBack(true)
            }
            navController.navigateUp()
            viewModel.navigateBack()
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title : String,
    onNavIconClicked : () -> Unit
){
    CenterAlignedTopAppBar(
        title = { Text(text = title, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = { onNavIconClicked() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.android_purple)
        ),
    )
}

@Composable
fun FAB(
    onFabClick : () -> Unit
){
    FloatingActionButton(
        onClick = {
            onFabClick()
        },
        Modifier
            .padding(22.dp)
            .background(colorResource(id = R.color.android_purple), CircleShape)
            .size(70.dp),
        shape = CircleShape,
        containerColor = colorResource(id = R.color.android_purple)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction", tint = Color.White)
    }
}