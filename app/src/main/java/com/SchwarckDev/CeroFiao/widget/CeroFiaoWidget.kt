package com.SchwarckDev.CeroFiao.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils

class CeroFiaoWidget : GlanceAppWidget() {

    companion object {
        val totalBalanceKey = floatPreferencesKey("total_balance_usd")
        val bcvRateKey = floatPreferencesKey("bcv_rate")
        val timestampKey = longPreferencesKey("timestamp")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val balanceUsd = currentState(key = totalBalanceKey) ?: 0f
            val bcvRate = currentState(key = bcvRateKey) ?: 0f
            val timestamp = currentState(key = timestampKey) ?: 0L
            
            WidgetContent(balanceUsd.toDouble(), bcvRate.toDouble(), timestamp)
        }
    }

    @Composable
    private fun WidgetContent(balanceUsd: Double, bcvRate: Double, timestamp: Long) {
        val lastUpdateStr = if (timestamp > 0) {
            DateUtils.formatDisplayDateTime(timestamp)
        } else {
            "Actualizando..."
        }
        
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))
                .padding(16.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            Text(
                text = "CeroFiao Balance",
                style = TextStyle(color = androidx.glance.unit.ColorProvider(Color(0xFFAAAAAA)), fontSize = 12.sp)
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = "$ ${CurrencyFormatter.format(balanceUsd, "USD", false)}",
                style = TextStyle(color = androidx.glance.unit.ColorProvider(Color.White), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            
            Spacer(modifier = GlanceModifier.height(16.dp))
            
            Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.Vertical.CenterVertically) {
                Text(
                    text = "BCV:",
                    style = TextStyle(color = androidx.glance.unit.ColorProvider(Color(0xFFAAAAAA)), fontSize = 12.sp)
                )
                Spacer(modifier = GlanceModifier.width(4.dp))
                Text(
                    text = "${CurrencyFormatter.format(bcvRate, "VES", false)} Bs.",
                    style = TextStyle(color = androidx.glance.unit.ColorProvider(Color(0xFF00FF66)), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                )
            }
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = "Act: $lastUpdateStr",
                style = TextStyle(color = androidx.glance.unit.ColorProvider(Color(0xFF555555)), fontSize = 10.sp)
            )
        }
    }
}
