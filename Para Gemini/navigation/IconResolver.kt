package com.SchwarckDev.cerofiao.core.designsystem.components.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Resolves an `iconKey` string (stored in entities) to the corresponding
 * [ImageVector] from [CeroFiaoIcons].
 *
 * Falls back to [CeroFiaoIcons.Box] when no match is found.
 */
fun resolveIconKey(iconKey: String): ImageVector = when (iconKey) {
    // Category icons
    "ic_food"     -> CeroFiaoIcons.Food
    "ic_shopping" -> CeroFiaoIcons.Shopping
    "ic_car"      -> CeroFiaoIcons.Car
    "ic_home"     -> CeroFiaoIcons.Home
    "ic_bolt"     -> CeroFiaoIcons.Bolt
    "ic_game"     -> CeroFiaoIcons.Game
    "ic_laptop"   -> CeroFiaoIcons.Laptop
    "ic_heart"    -> CeroFiaoIcons.Heart
    "ic_sim"      -> CeroFiaoIcons.Sim
    "ic_work"     -> CeroFiaoIcons.Work
    "ic_box"      -> CeroFiaoIcons.Box

    // Generic / account icons (extensible)
    "ic_wallet"   -> CeroFiaoIcons.Scan     // TODO: Add a proper wallet icon
    "ic_tag"      -> CeroFiaoIcons.Box       // Legacy fallback

    else          -> CeroFiaoIcons.Box       // Default fallback
}
