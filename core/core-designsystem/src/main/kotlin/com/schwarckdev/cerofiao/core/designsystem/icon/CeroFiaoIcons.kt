package com.schwarckdev.cerofiao.core.designsystem.icon

import androidx.annotation.DrawableRes
import com.composables.icons.lucide.ArrowDown
import com.composables.icons.lucide.ArrowDownLeft
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.ArrowLeftRight
import com.composables.icons.lucide.ArrowUp
import com.composables.icons.lucide.ArrowUpRight
import com.composables.icons.lucide.ChartBar
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Building
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.CircleArrowDown
import com.composables.icons.lucide.CircleArrowUp
import com.composables.icons.lucide.CircleDollarSign
import com.composables.icons.lucide.DollarSign
import com.composables.icons.lucide.Ellipsis
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.FileText
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.HandCoins
import com.composables.icons.lucide.CircleHelp
import com.composables.icons.lucide.House
import com.composables.icons.lucide.LayoutDashboard
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.LogOut
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Moon
import com.composables.icons.lucide.Palette
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.PiggyBank
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Receipt
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.Send
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.SlidersHorizontal
import com.composables.icons.lucide.Smartphone
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.Sun
import com.composables.icons.lucide.Target
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.TrendingDown
import com.composables.icons.lucide.TrendingUp
import com.composables.icons.lucide.LockOpen
import com.composables.icons.lucide.Users
import com.composables.icons.lucide.Wallet
import com.composables.icons.lucide.X
import androidx.compose.ui.graphics.vector.ImageVector
import dev.oneuiproject.oneui.R as IconsR

object CeroFiaoIcons {
    // Navigation (Lucide ImageVector) — Figma 4-tab layout
    val Dashboard: ImageVector = Lucide.LayoutDashboard
    val NavHome: ImageVector = Lucide.House
    val Transactions: ImageVector = Lucide.Receipt
    val NavTransactions: ImageVector = Lucide.ArrowLeftRight
    val Accounts: ImageVector = Lucide.Wallet
    val More: ImageVector = Lucide.Ellipsis
    val Settings: ImageVector = Lucide.Settings
    val Add: ImageVector = Lucide.Plus

    // Actions (Lucide ImageVector)
    val Back: ImageVector = Lucide.ArrowLeft
    val Delete: ImageVector = Lucide.Trash2
    val Edit: ImageVector = Lucide.Pencil
    val Search: ImageVector = Lucide.Search
    val Check: ImageVector = Lucide.Check
    val ChevronRight: ImageVector = Lucide.ChevronRight
    val ChevronDown: ImageVector = Lucide.ChevronDown
    val Close: ImageVector = Lucide.X
    val Filter: ImageVector = Lucide.SlidersHorizontal
    val SendIcon: ImageVector = Lucide.Send

    // Visibility
    val Eye: ImageVector = Lucide.Eye
    val EyeOff: ImageVector = Lucide.EyeOff

    // Transaction types (Lucide ImageVector)
    val Income: ImageVector = Lucide.CircleArrowDown
    val Expense: ImageVector = Lucide.CircleArrowUp
    val Transfer: ImageVector = Lucide.ArrowLeftRight
    val ArrowIncome: ImageVector = Lucide.ArrowDownLeft
    val ArrowExpense: ImageVector = Lucide.ArrowUpRight
    val TrendingUp: ImageVector = Lucide.TrendingUp
    val TrendingDown: ImageVector = Lucide.TrendingDown

    // Account types (Lucide ImageVector)
    val Cash: ImageVector = Lucide.Wallet
    val Bank: ImageVector = Lucide.Building
    val DigitalWallet: ImageVector = Lucide.Wallet
    val CryptoExchange: ImageVector = Lucide.DollarSign

    // Features (Lucide ImageVector)
    val ExchangeRate: ImageVector = Lucide.CircleArrowUp
    val Savings: ImageVector = Lucide.PiggyBank
    val Budget: ImageVector = Lucide.DollarSign
    val Debt: ImageVector = Lucide.Users
    val Analytics: ImageVector = Lucide.ChartBar
    val AIChat: ImageVector = Lucide.Sparkles
    val ScanReceipt: ImageVector = Lucide.Camera
    val HandCoins: ImageVector = Lucide.HandCoins
    val Bell: ImageVector = Lucide.Bell
    val Target: ImageVector = Lucide.Target
    val Lock: ImageVector = Lucide.Lock
    val Unlock: ImageVector = Lucide.LockOpen

    // Settings icons
    val DarkMode: ImageVector = Lucide.Moon
    val LightMode: ImageVector = Lucide.Sun
    val Notifications: ImageVector = Lucide.Bell
    val Biometric: ImageVector = Lucide.Shield
    val Haptic: ImageVector = Lucide.Smartphone
    val Globe: ImageVector = Lucide.Globe
    val CurrencyIcon: ImageVector = Lucide.CircleDollarSign
    val Categories: ImageVector = Lucide.Palette
    val ExportData: ImageVector = Lucide.FileText
    val Help: ImageVector = Lucide.CircleHelp
    val Logout: ImageVector = Lucide.LogOut

    // ---- Category icons (OneUI @DrawableRes) ----

    /**
     * Map of icon name -> OneUI drawable resource ID.
     * Used for categories. Access via painterResource() in composables.
     */
    val categoryIconMap: Map<String, Int> = mapOf(
        // Food & Drink
        "Food" to IconsR.drawable.ic_oui_food,
        "Restaurant" to IconsR.drawable.ic_oui_restaurants,

        // Transport
        "Car" to IconsR.drawable.ic_oui_car,

        // Services & Work
        "Settings" to IconsR.drawable.ic_oui_settings,
        "Receipt" to IconsR.drawable.ic_oui_receipt,

        // Communication
        "Phone" to IconsR.drawable.ic_oui_phone,

        // Entertainment
        "Game" to IconsR.drawable.ic_oui_game,
        "Headphones" to IconsR.drawable.ic_oui_headphones,
        "Sports" to IconsR.drawable.ic_oui_sports,

        // Health
        "Health" to IconsR.drawable.ic_oui_health,
        "Heart" to IconsR.drawable.ic_oui_heart,

        // Education
        "Education" to IconsR.drawable.ic_oui_education,
        "School" to IconsR.drawable.ic_oui_school,

        // Home
        "Home" to IconsR.drawable.ic_oui_home,

        // Shopping & Finance
        "Shopping" to IconsR.drawable.ic_oui_shopping,
        "CreditCard" to IconsR.drawable.ic_oui_credit_card,
        "Dollar" to IconsR.drawable.ic_oui_symbol_dollar,

        // Pets
        "Dog" to IconsR.drawable.ic_oui_dog,

        // Gifts
        "Gift" to IconsR.drawable.ic_oui_gift,

        // Calendar & Time
        "Calendar" to IconsR.drawable.ic_oui_calendar,
        "Alarm" to IconsR.drawable.ic_oui_alarm,

        // Favorites
        "Star" to IconsR.drawable.ic_oui_star,

        // Tags
        "Tag" to IconsR.drawable.ic_oui_tag,

        // Save
        "Save" to IconsR.drawable.ic_oui_save,

        // Edit
        "Pencil" to IconsR.drawable.ic_oui_pencil,

        // Close / Delete
        "Close" to IconsR.drawable.ic_oui_close,
        "Delete" to IconsR.drawable.ic_oui_delete,

        // Add
        "AddHome" to IconsR.drawable.ic_oui_add_home,

        // Flashlight
        "Flash" to IconsR.drawable.ic_oui_flashlight,

        // Wifi
        "Wifi" to IconsR.drawable.ic_oui_wifi,

        // Search
        "Search" to IconsR.drawable.ic_oui_search,

        // Apps
        "Apps" to IconsR.drawable.ic_oui_apps,

        // Animal
        "Animal" to IconsR.drawable.ic_oui_animal,
    )

    /**
     * Legacy Material icon name → new OneUI icon name mapping.
     * Handles existing categories in the database that use old Material icon names.
     */
    private val legacyIconNameMap: Map<String, String> = mapOf(
        "Restaurant" to "Restaurant",
        "DirectionsCar" to "Car",
        "Build" to "Settings",
        "PhoneAndroid" to "Phone",
        "SportsEsports" to "Game",
        "LocalHospital" to "Health",
        "School" to "School",
        "Home" to "Home",
        "Checkroom" to "Shopping",
        "Devices" to "Phone",
        "Fastfood" to "Food",
        "MoreHoriz" to "Apps",
        "Payments" to "CreditCard",
        "Work" to "Receipt",
        "SwapHoriz" to "Receipt",
        "AttachMoney" to "Dollar",
        "Category" to "Apps",
    )

    /**
     * All category icons for the icon picker UI.
     * Returns pairs of (name, drawableResId).
     */
    val allCategoryIcons: List<Pair<String, Int>> = categoryIconMap.entries.map { it.key to it.value }

    /**
     * Returns the OneUI drawable resource ID for a category icon name.
     * Supports both new names and legacy Material icon names.
     */
    @DrawableRes
    fun getCategoryIconRes(iconName: String): Int {
        // Try direct match first
        categoryIconMap[iconName]?.let { return it }
        // Try legacy mapping
        val mappedName = legacyIconNameMap[iconName]
        if (mappedName != null) {
            categoryIconMap[mappedName]?.let { return it }
        }
        // Fallback
        return IconsR.drawable.ic_oui_apps
    }
}
