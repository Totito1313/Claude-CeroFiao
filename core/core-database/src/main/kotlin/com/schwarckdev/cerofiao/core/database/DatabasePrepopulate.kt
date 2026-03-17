package com.schwarckdev.cerofiao.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.schwarckdev.cerofiao.core.database.entity.CategoryEntity
import com.schwarckdev.cerofiao.core.database.entity.CurrencyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabasePrepopulate(
    private val database: () -> CeroFiaoDatabase,
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val db = database()
            db.currencyDao().insertAll(defaultCurrencies)
            db.categoryDao().insertAll(defaultCategories)
        }
    }

    companion object {
        val defaultCurrencies = listOf(
            CurrencyEntity(code = "USD", name = "Dólar Estadounidense", symbol = "$", decimalPlaces = 2, isActive = true, sortOrder = 0),
            CurrencyEntity(code = "VES", name = "Bolívar", symbol = "Bs", decimalPlaces = 2, isActive = true, sortOrder = 1),
            CurrencyEntity(code = "USDT", name = "Tether", symbol = "₮", decimalPlaces = 2, isActive = true, sortOrder = 2),
            CurrencyEntity(code = "EUR", name = "Euro", symbol = "€", decimalPlaces = 2, isActive = true, sortOrder = 3),
            CurrencyEntity(code = "EURI", name = "Euro Informal", symbol = "€", decimalPlaces = 2, isActive = true, sortOrder = 4),
        )

        val defaultCategories = listOf(
            // Expenses
            CategoryEntity(id = "cat_food", name = "Comida", type = "EXPENSE", iconName = "Restaurant", colorHex = "#FF5722", parentId = null, isDefault = true, sortOrder = 0, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_transport", name = "Transporte", type = "EXPENSE", iconName = "DirectionsCar", colorHex = "#2196F3", parentId = null, isDefault = true, sortOrder = 1, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_services", name = "Servicios", type = "EXPENSE", iconName = "Build", colorHex = "#FF9800", parentId = null, isDefault = true, sortOrder = 2, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_recharges", name = "Recargas", type = "EXPENSE", iconName = "PhoneAndroid", colorHex = "#4CAF50", parentId = null, isDefault = true, sortOrder = 3, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_entertainment", name = "Entretenimiento", type = "EXPENSE", iconName = "SportsEsports", colorHex = "#9C27B0", parentId = null, isDefault = true, sortOrder = 4, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_health", name = "Salud", type = "EXPENSE", iconName = "LocalHospital", colorHex = "#F44336", parentId = null, isDefault = true, sortOrder = 5, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_education", name = "Educación", type = "EXPENSE", iconName = "School", colorHex = "#3F51B5", parentId = null, isDefault = true, sortOrder = 6, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_home", name = "Hogar", type = "EXPENSE", iconName = "Home", colorHex = "#795548", parentId = null, isDefault = true, sortOrder = 7, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_clothing", name = "Ropa", type = "EXPENSE", iconName = "Checkroom", colorHex = "#E91E63", parentId = null, isDefault = true, sortOrder = 8, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_tech", name = "Tecnología", type = "EXPENSE", iconName = "Devices", colorHex = "#607D8B", parentId = null, isDefault = true, sortOrder = 9, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_snacks", name = "Snacks", type = "EXPENSE", iconName = "Fastfood", colorHex = "#FFEB3B", parentId = null, isDefault = true, sortOrder = 10, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_other", name = "Otros", type = "EXPENSE", iconName = "MoreHoriz", colorHex = "#9E9E9E", parentId = null, isDefault = true, sortOrder = 11, isActive = true, syncId = null, isDeleted = false),
            // Income
            CategoryEntity(id = "cat_salary", name = "Salario", type = "INCOME", iconName = "Payments", colorHex = "#4CAF50", parentId = null, isDefault = true, sortOrder = 0, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_freelance", name = "Freelance", type = "INCOME", iconName = "Work", colorHex = "#00BCD4", parentId = null, isDefault = true, sortOrder = 1, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_transfer_in", name = "Transferencia", type = "INCOME", iconName = "SwapHoriz", colorHex = "#03A9F4", parentId = null, isDefault = true, sortOrder = 2, isActive = true, syncId = null, isDeleted = false),
            CategoryEntity(id = "cat_other_income", name = "Otros Ingresos", type = "INCOME", iconName = "AttachMoney", colorHex = "#8BC34A", parentId = null, isDefault = true, sortOrder = 3, isActive = true, syncId = null, isDeleted = false),
        )
    }
}
