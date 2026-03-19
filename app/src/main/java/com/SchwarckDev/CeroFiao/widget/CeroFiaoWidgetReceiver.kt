package com.SchwarckDev.CeroFiao.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class CeroFiaoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = CeroFiaoWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        enqueueUpdateWork(context)
    }
    
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        enqueueUpdateWork(context)
    }
    
    private fun enqueueUpdateWork(context: Context) {
        val request = OneTimeWorkRequestBuilder<WidgetUpdateWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "UpdateCeroFiaoWidgetWork",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
