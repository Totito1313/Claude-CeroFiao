package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class CategoryType {
    INCOME,
    EXPENSE,
}

@Serializable
data class Category(
    val id: String,
    val name: String,
    val type: CategoryType,
    val iconName: String,
    val colorHex: String,
    val parentId: String? = null,
    val isDefault: Boolean = false,
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
)

object DefaultCategories {
    val defaults = listOf(
        Category(id = "cat_food", name = "Comida", type = CategoryType.EXPENSE, iconName = "Restaurant", colorHex = "#FF5722", isDefault = true, sortOrder = 0),
        Category(id = "cat_transport", name = "Transporte", type = CategoryType.EXPENSE, iconName = "DirectionsCar", colorHex = "#2196F3", isDefault = true, sortOrder = 1),
        Category(id = "cat_services", name = "Servicios", type = CategoryType.EXPENSE, iconName = "Build", colorHex = "#FF9800", isDefault = true, sortOrder = 2),
        Category(id = "cat_recharges", name = "Recargas", type = CategoryType.EXPENSE, iconName = "PhoneAndroid", colorHex = "#4CAF50", isDefault = true, sortOrder = 3),
        Category(id = "cat_entertainment", name = "Entretenimiento", type = CategoryType.EXPENSE, iconName = "SportsEsports", colorHex = "#9C27B0", isDefault = true, sortOrder = 4),
        Category(id = "cat_health", name = "Salud", type = CategoryType.EXPENSE, iconName = "LocalHospital", colorHex = "#F44336", isDefault = true, sortOrder = 5),
        Category(id = "cat_education", name = "Educación", type = CategoryType.EXPENSE, iconName = "School", colorHex = "#3F51B5", isDefault = true, sortOrder = 6),
        Category(id = "cat_home", name = "Hogar", type = CategoryType.EXPENSE, iconName = "Home", colorHex = "#795548", isDefault = true, sortOrder = 7),
        Category(id = "cat_clothing", name = "Ropa", type = CategoryType.EXPENSE, iconName = "Checkroom", colorHex = "#E91E63", isDefault = true, sortOrder = 8),
        Category(id = "cat_tech", name = "Tecnología", type = CategoryType.EXPENSE, iconName = "Devices", colorHex = "#607D8B", isDefault = true, sortOrder = 9),
        Category(id = "cat_snacks", name = "Snacks", type = CategoryType.EXPENSE, iconName = "Fastfood", colorHex = "#FFEB3B", isDefault = true, sortOrder = 10),
        Category(id = "cat_other", name = "Otros", type = CategoryType.EXPENSE, iconName = "MoreHoriz", colorHex = "#9E9E9E", isDefault = true, sortOrder = 11),
        Category(id = "cat_salary", name = "Salario", type = CategoryType.INCOME, iconName = "Payments", colorHex = "#4CAF50", isDefault = true, sortOrder = 0),
        Category(id = "cat_freelance", name = "Freelance", type = CategoryType.INCOME, iconName = "Work", colorHex = "#00BCD4", isDefault = true, sortOrder = 1),
        Category(id = "cat_transfer_in", name = "Transferencia", type = CategoryType.INCOME, iconName = "SwapHoriz", colorHex = "#03A9F4", isDefault = true, sortOrder = 2),
        Category(id = "cat_other_income", name = "Otros Ingresos", type = CategoryType.INCOME, iconName = "AttachMoney", colorHex = "#8BC34A", isDefault = true, sortOrder = 3),
    )
}
