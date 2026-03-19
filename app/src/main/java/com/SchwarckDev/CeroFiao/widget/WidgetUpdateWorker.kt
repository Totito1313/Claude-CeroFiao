package com.SchwarckDev.CeroFiao.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.schwarckdev.cerofiao.core.data.repository.WidgetDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WidgetUpdateWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val widgetDataRepository: WidgetDataRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val data = widgetDataRepository.getWidgetData()
            
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(CeroFiaoWidget::class.java)
            
            glanceIds.forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[CeroFiaoWidget.totalBalanceKey] = data.totalBalanceUsd.toFloat()
                    prefs[CeroFiaoWidget.bcvRateKey] = data.bcvRate.toFloat()
                    prefs[CeroFiaoWidget.timestampKey] = data.timestampMs
                }
                CeroFiaoWidget().update(context, glanceId)
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
