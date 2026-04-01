package com.example.bankapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bankapp.ui.screens.DashboardScreen
import com.example.bankapp.ui.screens.LoginScreen
import com.example.bankapp.ui.screens.TransactionsScreen
import com.example.bankapp.viewmodel.AuthState
import com.example.bankapp.viewmodel.BankViewModel

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val TRANSACTIONS = "transactions"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: BankViewModel = viewModel()
) {
    val authState by viewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                viewModel = viewModel,
                onAccountSelected = { account ->
                    viewModel.loadTransactions(account)
                    navController.navigate(Routes.TRANSACTIONS)
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.TRANSACTIONS) {
            TransactionsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}