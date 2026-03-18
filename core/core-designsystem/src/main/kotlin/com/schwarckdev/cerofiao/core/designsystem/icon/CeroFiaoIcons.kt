package com.schwarckdev.cerofiao.core.designsystem.icon

import androidx.annotation.DrawableRes
import com.composables.icons.lucide.ArrowDown
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.ArrowLeftRight
import com.composables.icons.lucide.ArrowUp
import com.composables.icons.lucide.Building
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.CircleArrowDown
import com.composables.icons.lucide.CircleArrowUp
import com.composables.icons.lucide.DollarSign
import com.composables.icons.lucide.Ellipsis
import com.composables.icons.lucide.LayoutDashboard
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Moon
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Receipt
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.Sun
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.Users
import com.composables.icons.lucide.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import dev.oneuiproject.oneui.R as IconsR

object CeroFiaoIcons {
    // Navigation (Lucide ImageVector)
    val Dashboard: ImageVector = Lucide.LayoutDashboard
    val Transactions: ImageVector = Lucide.Receipt
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

    // Transaction types (Lucide ImageVector)
    val Income: ImageVector = Lucide.CircleArrowDown
    val Expense: ImageVector = Lucide.CircleArrowUp
    val Transfer: ImageVector = Lucide.ArrowLeftRight

    // Account types (Lucide ImageVector)
    val Cash: ImageVector = Lucide.Wallet
    val Bank: ImageVector = Lucide.Building
    val DigitalWallet: ImageVector = Lucide.Wallet
    val CryptoExchange: ImageVector = Lucide.DollarSign

    // Features (Lucide ImageVector)
    val ExchangeRate: ImageVector = Lucide.CircleArrowUp
    val Savings: ImageVector = Lucide.Wallet
    val Budget: ImageVector = Lucide.DollarSign
    val Debt: ImageVector = Lucide.Users

    // Theme (Lucide ImageVector)
    val DarkMode: ImageVector = Lucide.Moon
    val LightMode: ImageVector = Lucide.Sun

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
