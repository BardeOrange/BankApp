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
import com.example.bankapp.data.model.Transaction
import com.example.bankapp.ui.theme.BankBlue
import com.example.bankapp.ui.theme.BankGreen
import com.example.bankapp.viewmodel.BankViewModel
import com.example.bankapp.viewmodel.TransactionsState
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: BankViewModel,
    onBack: () -> Unit
) {
    val transactionsState by viewModel.transactionsState.collectAsState()
    val selectedAccount by viewModel.selectedAccount.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedAccount?.type?.label ?: "Transactions",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BankBlue)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Spatial configuration
            selectedAccount?.let { account ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BankBlue),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Solde disponible", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Text(
                            text = String.format(Locale.FRANCE, "%.2f €", account.balance),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(account.iban, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                }
            }

            when (transactionsState) {
                is TransactionsState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BankBlue)
                    }
                }
                is TransactionsState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            (transactionsState as TransactionsState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is TransactionsState.Success -> {
                    val transactions = (transactionsState as TransactionsState.Success).transactions
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                "Dernières opérations",
                                fontWeight = FontWeight.Bold,
                                color = BankBlue,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        items(transactions) { transaction ->
                            TransactionItem(transaction = transaction)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emote
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (transaction.isCredit) BankGreen.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = transaction.category.icon, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.label,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
                Text(
                    text = "${transaction.category.label} · ${transaction.date.format(formatter)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = String.format(
                    Locale.FRANCE,
                    "%s%.2f €",
                    if (transaction.isCredit) "+" else "",
                    transaction.amount
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (transaction.isCredit) BankGreen else MaterialTheme.colorScheme.error
            )
        }
    }
}