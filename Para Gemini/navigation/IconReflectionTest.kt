package com.SchwarckDev.cerofiao.core.designsystem.components.navigation

import androidx.compose.ui.graphics.vector.ImageVector

fun getAllIcons(): List<Pair<String, ImageVector>> {
    return CeroFiaoIcons.javaClass.methods.filter { it.returnType == ImageVector::class.java }
        .map { it.name.removePrefix("get") to it.invoke(CeroFiaoIcons) as ImageVector }
}