package com.example.bankapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BankBlue = Color(0xFF003189)
val BankLightBlue = Color(0xFF1565C0)
val BankRed = Color(0xFFD32F2F)
val BankGreen = Color(0xFF2E7D32)
val BankGray = Color(0xFFF5F5F5)

private val BankColorScheme = lightColorScheme(
    primary = BankBlue,
    onPrimary = Color.White,
    primaryContainer = BankLightBlue,
    secondary = BankRed,
    background = BankGray,
    surface = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun BankAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BankColorScheme,
        typography = Typography(),
        content = content
    )
}