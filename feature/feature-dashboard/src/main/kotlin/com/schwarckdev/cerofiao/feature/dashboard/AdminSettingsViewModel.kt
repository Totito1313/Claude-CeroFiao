package com.schwarckdev.cerofiao.feature.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.designsystem.theme.AccentPreset
import com.schwarckdev.cerofiao.core.designsystem.theme.BodyFontOption
import com.schwarckdev.cerofiao.core.designsystem.theme.FontOption
import com.schwarckdev.cerofiao.core.designsystem.theme.ThemeMode
import com.schwarckdev.cerofiao.core.designsystem.theme.ThemePreferencesManager
import com.schwarckdev.cerofiao.core.designsystem.theme.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSettingsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val preferencesManager = ThemePreferencesManager.getInstance(application)

    val uiState: StateFlow<UserPreferences> = preferencesManager.preferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    // ─── Theme ───
    fun setThemeMode(mode: ThemeMode) { viewModelScope.launch { preferencesManager.setThemeMode(mode) } }
    fun setAccentPreset(preset: AccentPreset) { viewModelScope.launch { preferencesManager.setAccentPreset(preset) } }
    fun setTitleFont(font: FontOption) { viewModelScope.launch { preferencesManager.setTitleFont(font) } }
    fun setBodyFont(font: BodyFontOption) { viewModelScope.launch { preferencesManager.setBodyFont(font) } }

    // ─── Shadow ───
    fun setShadowEnabled(enabled: Boolean) { viewModelScope.launch { preferencesManager.setShadowEnabled(enabled) } }
    fun setShadowAlpha(alpha: Float) { viewModelScope.launch { preferencesManager.setShadowAlpha(alpha) } }
    fun setShadowBlur(blur: Float) { viewModelScope.launch { preferencesManager.setShadowBlur(blur) } }
    fun setShadowOffsetY(offsetY: Float) { viewModelScope.launch { preferencesManager.setShadowOffsetY(offsetY) } }

    // ─── Glass ───
    fun setGlassBlur(blur: Float) { viewModelScope.launch { preferencesManager.setGlassBlur(blur) } }
    fun setGlassTintAlpha(alpha: Float) { viewModelScope.launch { preferencesManager.setGlassTintAlpha(alpha) } }
    fun setGlassBorderOpacity(opacity: Float) { viewModelScope.launch { preferencesManager.setGlassBorderOpacity(opacity) } }
    fun setGlassNoise(noise: Float) { viewModelScope.launch { preferencesManager.setGlassNoise(noise) } }

    // ─── Card ───
    fun setCardCornerRadius(radius: Float) { viewModelScope.launch { preferencesManager.setCardCornerRadius(radius) } }
    fun setCardBgOpacity(opacity: Float) { viewModelScope.launch { preferencesManager.setCardBgOpacity(opacity) } }
    fun setCardBorderWidth(width: Float) { viewModelScope.launch { preferencesManager.setCardBorderWidth(width) } }
    fun setCardBorderOpacity(opacity: Float) { viewModelScope.launch { preferencesManager.setCardBorderOpacity(opacity) } }

    // ─── Float Menus ───
    fun setContextualNavBarOffsetX(offsetX: Float) { viewModelScope.launch { preferencesManager.setContextualNavBarOffsetX(offsetX) } }
    fun setContextualNavBarOffsetY(offsetY: Float) { viewModelScope.launch { preferencesManager.setContextualNavBarOffsetY(offsetY) } }
    fun setCornerMenuOffsetX(offsetX: Float) { viewModelScope.launch { preferencesManager.setCornerMenuOffsetX(offsetX) } }
    fun setCornerMenuOffsetY(offsetY: Float) { viewModelScope.launch { preferencesManager.setCornerMenuOffsetY(offsetY) } }

    // ─── Reset ───
    fun resetToDefaults() { viewModelScope.launch { preferencesManager.resetToDefaults() } }
}
