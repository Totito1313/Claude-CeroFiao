package com.schwarckdev.cerofiao.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

object CeroFiaoIcons {
    // Navigation
    val Dashboard = Icons.Default.Dashboard
    val Transactions = Icons.Default.Receipt
    val Accounts = Icons.Default.AccountBalanceWallet
    val More = Icons.Default.MoreHoriz
    val Settings = Icons.Default.Settings
    val Add = Icons.Default.Add

    // Transaction types
    val Income = Icons.Default.ArrowDownward
    val Expense = Icons.Default.ArrowUpward
    val Transfer = Icons.Default.SwapHoriz

    // Account types
    val Cash = Icons.Default.Wallet
    val Bank = Icons.Default.AccountBalance
    val DigitalWallet = Icons.Default.AccountBalanceWallet
    val CryptoExchange = Icons.Default.CurrencyExchange

    // Features
    val ExchangeRate = Icons.AutoMirrored.Filled.TrendingUp
    val Savings = Icons.Default.Savings
    val Budget = Icons.Default.Payments

    // Theme
    val DarkMode = Icons.Default.DarkMode
    val LightMode = Icons.Default.LightMode

    // Category icon mapping
    val categoryIconMap: Map<String, ImageVector> = mapOf(
        "Restaurant" to Icons.Default.Restaurant,
        "DirectionsCar" to Icons.Default.DirectionsCar,
        "Build" to Icons.Default.Build,
        "PhoneAndroid" to Icons.Default.PhoneAndroid,
        "SportsEsports" to Icons.Default.SportsEsports,
        "LocalHospital" to Icons.Default.LocalHospital,
        "School" to Icons.Default.School,
        "Home" to Icons.Default.Home,
        "Checkroom" to Icons.Default.Checkroom,
        "Devices" to Icons.Default.Devices,
        "Fastfood" to Icons.Default.Fastfood,
        "MoreHoriz" to Icons.Default.MoreHoriz,
        "Payments" to Icons.Default.Payments,
        "Work" to Icons.Default.Work,
        "SwapHoriz" to Icons.Default.SwapHoriz,
        "AttachMoney" to Icons.Default.AttachMoney,
        "Category" to Icons.Default.Category,
    )

    fun getCategoryIcon(iconName: String): ImageVector {
        return categoryIconMap[iconName] ?: Icons.Default.Category
    }
}
