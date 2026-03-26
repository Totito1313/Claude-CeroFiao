package com.schwarckdev.cerofiao.core.designsystem.theme

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.compose.ui.unit.dp

// ═══════════════════════════════════════════════════════════════
// ThemePreferencesManager — DataStore-backed theme preferences
// ═══════════════════════════════════════════════════════════════

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")


@SuppressLint("StaticFieldLeak")
class ThemePreferencesManager(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ACCENT_PRESET = stringPreferencesKey("accent_preset")
        val TITLE_FONT = stringPreferencesKey("title_font")
        val BODY_FONT = stringPreferencesKey("body_font")
        // Shadow
        val SHADOW_ENABLED = booleanPreferencesKey("shadow_enabled")
        val SHADOW_ALPHA = floatPreferencesKey("shadow_alpha")
        val SHADOW_BLUR = floatPreferencesKey("shadow_blur")
        val SHADOW_OFFSET_Y = floatPreferencesKey("shadow_offset_y")
        // Glass
        val GLASS_BLUR = floatPreferencesKey("glass_blur")
        val GLASS_TINT_ALPHA = floatPreferencesKey("glass_tint_alpha")
        val GLASS_BORDER_OPACITY = floatPreferencesKey("glass_border_opacity")
        val GLASS_NOISE = floatPreferencesKey("glass_noise")
        // Card
        val CARD_CORNER_RADIUS = floatPreferencesKey("card_corner_radius")
        val CARD_BG_OPACITY = floatPreferencesKey("card_bg_opacity")
        val CARD_BORDER_WIDTH = floatPreferencesKey("card_border_width")
        val CARD_BORDER_OPACITY = floatPreferencesKey("card_border_opacity")
        // Float Menus
        val FLOAT_CONTEXTUAL_OFFSET_X = floatPreferencesKey("float_contextual_offset_x")
        val FLOAT_CONTEXTUAL_OFFSET_Y = floatPreferencesKey("float_contextual_offset_y")
        val FLOAT_CORNER_OFFSET_X = floatPreferencesKey("float_corner_offset_x")
        val FLOAT_CORNER_OFFSET_Y = floatPreferencesKey("float_corner_offset_y")
        // Onboarding
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val preferencesFlow: Flow<UserPreferences> = context.themeDataStore.data.map { prefs ->
        UserPreferences(
            themeMode = ThemeMode.entries.find { it.name == prefs[Keys.THEME_MODE] } ?: ThemeMode.Auto,
            accentPreset = AccentPreset.fromId(prefs[Keys.ACCENT_PRESET] ?: "cyan"),
            titleFont = FontOption.fromId(prefs[Keys.TITLE_FONT] ?: "anton"),
            bodyFont = BodyFontOption.fromId(prefs[Keys.BODY_FONT] ?: "oneui"),
            shadowConfig = ShadowConfig(
                enabled = prefs[Keys.SHADOW_ENABLED] ?: true,
                alpha = prefs[Keys.SHADOW_ALPHA] ?: 0.25f,
                blurRadius = (prefs[Keys.SHADOW_BLUR] ?: 25f).dp,
                offsetY = (prefs[Keys.SHADOW_OFFSET_Y] ?: 4f).dp
            ),
            glassConfig = GlassConfig(
                blurIntensity = (prefs[Keys.GLASS_BLUR] ?: 20f).dp,
                tintAlpha = prefs[Keys.GLASS_TINT_ALPHA] ?: 0.15f,
                borderOpacity = prefs[Keys.GLASS_BORDER_OPACITY] ?: 0.3f,
                noiseFactor = prefs[Keys.GLASS_NOISE] ?: 0.05f
            ),
            cardConfig = CardConfig(
                cornerRadius = (prefs[Keys.CARD_CORNER_RADIUS] ?: 25f).dp,
                backgroundOpacity = prefs[Keys.CARD_BG_OPACITY] ?: 1f,
                borderWidth = (prefs[Keys.CARD_BORDER_WIDTH] ?: 1f).dp,
                borderOpacity = prefs[Keys.CARD_BORDER_OPACITY] ?: 0.15f
            ),
            floatMenuConfig = FloatMenuConfig(
                contextualNavBarOffsetX = (prefs[Keys.FLOAT_CONTEXTUAL_OFFSET_X] ?: 16f).dp,
                contextualNavBarOffsetY = (prefs[Keys.FLOAT_CONTEXTUAL_OFFSET_Y] ?: 90f).dp,
                cornerMenuOffsetX = (prefs[Keys.FLOAT_CORNER_OFFSET_X] ?: 16f).dp,
                cornerMenuOffsetY = (prefs[Keys.FLOAT_CORNER_OFFSET_Y] ?: 80f).dp
            ),
            isOnboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false
        )
    }

    // ─── Theme ───
    suspend fun setThemeMode(mode: ThemeMode) {
        context.themeDataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setAccentPreset(preset: AccentPreset) {
        context.themeDataStore.edit { it[Keys.ACCENT_PRESET] = preset.id }
    }

    suspend fun setTitleFont(font: FontOption) {
        context.themeDataStore.edit { it[Keys.TITLE_FONT] = font.id }
    }

    suspend fun setBodyFont(font: BodyFontOption) {
        context.themeDataStore.edit { it[Keys.BODY_FONT] = font.id }
    }

    // ─── Shadow ───
    suspend fun setShadowEnabled(enabled: Boolean) {
        context.themeDataStore.edit { it[Keys.SHADOW_ENABLED] = enabled }
    }

    suspend fun setShadowAlpha(alpha: Float) {
        context.themeDataStore.edit { it[Keys.SHADOW_ALPHA] = alpha }
    }

    suspend fun setShadowBlur(blur: Float) {
        context.themeDataStore.edit { it[Keys.SHADOW_BLUR] = blur }
    }

    suspend fun setShadowOffsetY(offsetY: Float) {
        context.themeDataStore.edit { it[Keys.SHADOW_OFFSET_Y] = offsetY }
    }

    // ─── Glass ───
    suspend fun setGlassBlur(blur: Float) {
        context.themeDataStore.edit { it[Keys.GLASS_BLUR] = blur }
    }

    suspend fun setGlassTintAlpha(alpha: Float) {
        context.themeDataStore.edit { it[Keys.GLASS_TINT_ALPHA] = alpha }
    }

    suspend fun setGlassBorderOpacity(opacity: Float) {
        context.themeDataStore.edit { it[Keys.GLASS_BORDER_OPACITY] = opacity }
    }

    suspend fun setGlassNoise(noise: Float) {
        context.themeDataStore.edit { it[Keys.GLASS_NOISE] = noise }
    }

    // ─── Card ───
    suspend fun setCardCornerRadius(radius: Float) {
        context.themeDataStore.edit { it[Keys.CARD_CORNER_RADIUS] = radius }
    }

    suspend fun setCardBgOpacity(opacity: Float) {
        context.themeDataStore.edit { it[Keys.CARD_BG_OPACITY] = opacity }
    }

    suspend fun setCardBorderWidth(width: Float) {
        context.themeDataStore.edit { it[Keys.CARD_BORDER_WIDTH] = width }
    }

    suspend fun setCardBorderOpacity(opacity: Float) {
        context.themeDataStore.edit { it[Keys.CARD_BORDER_OPACITY] = opacity }
    }

    // ─── Float Menus ───
    suspend fun setContextualNavBarOffsetX(offsetX: Float) {
        context.themeDataStore.edit { it[Keys.FLOAT_CONTEXTUAL_OFFSET_X] = offsetX }
    }

    suspend fun setContextualNavBarOffsetY(offsetY: Float) {
        context.themeDataStore.edit { it[Keys.FLOAT_CONTEXTUAL_OFFSET_Y] = offsetY }
    }

    suspend fun setCornerMenuOffsetX(offsetX: Float) {
        context.themeDataStore.edit { it[Keys.FLOAT_CORNER_OFFSET_X] = offsetX }
    }

    suspend fun setCornerMenuOffsetY(offsetY: Float) {
        context.themeDataStore.edit { it[Keys.FLOAT_CORNER_OFFSET_Y] = offsetY }
    }

    // ─── Onboarding ───
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.themeDataStore.edit { it[Keys.ONBOARDING_COMPLETED] = completed }
    }

    // ─── Reset ───
    suspend fun resetToDefaults() {
        context.themeDataStore.edit { it.clear() }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemePreferencesManager? = null

        fun getInstance(context: Context): ThemePreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ThemePreferencesManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
