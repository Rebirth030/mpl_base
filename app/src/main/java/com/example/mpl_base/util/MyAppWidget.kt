package com.example.mpl_base.util

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.mpl_base.R
import com.example.mpl_base.activities.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class MyAppWidget : AppWidgetProvider() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val appWidgetId = intent!!.getIntExtra(APP_WIDGET_ID, 0)

        when(intent.action){
            WidgetActionEnum.REFRESH.toString() -> {
                val number = CalcUtil.rng()
                updateAppWidget(context!!, AppWidgetManager.getInstance(context), appWidgetId, number)
            }
        }
        super.onReceive(context, intent)
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,0)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    number: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.my_app_widget)
    views.setTextViewText(R.id.appWidgetTitle, context.getString(R.string.appwidget_title))
    views.setTextViewText(R.id.appWidgetNumber, number.toString())

    views.setOnClickPendingIntent(R.id.appWidgetBtn, refreshRandomNumber(context, appWidgetId))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun refreshRandomNumber(context: Context, appWidgetId: Int) : PendingIntent{
    val intent = Intent(context, MyAppWidget::class.java)
    intent.putExtra(APP_WIDGET_ID, appWidgetId)
    intent.flags = Intent.FLAG_RECEIVER_FOREGROUND
    intent.action = WidgetActionEnum.REFRESH.toString()
    return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}