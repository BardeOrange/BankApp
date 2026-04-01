package com.example.bankapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankapp.data.model.Account
import com.example.bankapp.data.model.AccountType
import com.example.bankapp.ui.theme.BankBlue
import com.example.bankapp.ui.theme.BankGreen
import com.example.bankapp.viewmodel.AccountsState
import com.example.bankapp.viewmodel.BankViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: BankViewModel,
    onAccountSelected: (Account) -> Unit,
    onLogout: () -> Unit
) {
    val accountsState by viewModel.accountsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalance,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("MonBanque", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BankBlue),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = "Déconnexion", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (accountsState) {
                is AccountsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BankBlue
                    )
                }
                is AccountsState.Error -> {
                    Text(
                        text = (accountsState as AccountsState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is AccountsState.Success -> {
                    val accounts = (accountsState as AccountsState.Success).accounts
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // Header de bienvenue
                            Text(
                                text = "Bonjour, ${accounts.firstOrNull()?.ownerName ?: ""}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = BankBlue
                            )
                            Text(
                                text = "Vos comptes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        items(accounts) { account ->
                            AccountCard(account = account, onClick = { onAccountSelected(account) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountCard(account: Account, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Visuals
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BankBlue.copy(alpha = 0.1f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (account.type) {
                            AccountType.CHECKING -> Icons.Filled.CreditCard
                            AccountType.SAVINGS -> Icons.Filled.Savings
                            AccountType.BUSINESS -> Icons.Filled.Business
                        },
                        contentDescription = null,
                        tint = BankBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.type.label,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = account.iban.take(18) + "...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format(Locale.FRANCE, "%.2f €", account.balance),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (account.balance >= 0) BankGreen else MaterialTheme.colorScheme.error
                )
                Text(text = account.currency, fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}