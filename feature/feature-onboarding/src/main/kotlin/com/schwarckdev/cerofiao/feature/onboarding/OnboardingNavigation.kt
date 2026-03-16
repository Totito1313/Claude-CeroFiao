package com.schwarckdev.cerofiao.feature.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object OnboardingRoute

fun NavGraphBuilder.onboardingScreen(
    onOnboardingComplete: () -> Unit,
) {
    composable<OnboardingRoute> {
        OnboardingScreen(
            onOnboardingComplete = onOnboardingComplete,
        )
    }
}
