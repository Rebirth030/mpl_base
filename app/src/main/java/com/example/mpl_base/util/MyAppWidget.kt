package com.example.mpl_base.util

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.mpl_base.R
import com.example.mpl_base.activities.FalseActivity
import com.example.mpl_base.activities.MainActivity
import com.example.mpl_base.activities.TrueActivity
import com.example.mpl_base.util.NotificationUtil.Companion.sendNotification

/**
 * Implementation of App Widget functionality.
 */
class MyAppWidget : AppWidgetProvider() {

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast.
     * On action Refresh: Updates the widget with a new random number
     * On action Sync: Updates the widget with the number from the intent
     * On action Notify: Sends a notification with the result of the guess
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     *
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val appWidgetId = intent!!.getIntExtra(APP_WIDGET_ID, 0)

        when(intent.action){
            WidgetActionEnum.REFRESH.toString() -> {
                val number = CalcUtil.rng()
                updateAppWidget(context!!, AppWidgetManager.getInstance(context), appWidgetId, number)
            }
            WidgetActionEnum.SYNC.toString() -> {
                val number = intent.getIntExtra(RANDOM_NUMBER, 0)
                updateAppWidget(context!!, AppWidgetManager.getInstance(context), appWidgetId, number)
            }
            WidgetActionEnum.NOTIFY.toString() -> {
                val number = intent.getIntExtra(RANDOM_NUMBER, 0)
                val isPrime = CalcUtil.checkIfPrime(number)
                val guessIsPrime = intent.getBooleanExtra(GUESS_IS_PRIME, false)


                val text = if (isPrime) {
                    String.format( context!!.getString(R.string.answer_text), number, context.getString(R.string.is_text))
                } else {
                    String.format( context!!.getString(R.string.answer_text), number, context.getString(R.string.is_not_text))
                }
                if (isPrime == guessIsPrime)
                {
                    val trueIntent = Intent(context, TrueActivity::class.java)
                    trueIntent.putExtra(RANDOM_NUMBER, number)
                    trueIntent.putExtra(IS_PRIME, isPrime)
                    sendNotification(context, context.getString(R.string.yay), text, R.drawable.ic_launcher_foreground, trueIntent)
                } else {
                    val falseIntent = Intent(context, FalseActivity::class.java)
                    falseIntent.putExtra(RANDOM_NUMBER, number)
                    falseIntent.putExtra(IS_PRIME, isPrime)
                    sendNotification(context, context.getString(R.string.nay), text, R.drawable.ic_launcher_foreground, falseIntent)
                }
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
    }


    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

/**
 * Updates the widget with the given number.
 * Sets the listeners for the buttons.
 * Creates a pending intent to launch MainActivity.
 * Creates a pending intent to refresh the widget.
 * Creates a pending intent to notify the user of the result of the guess.
 *
 * @param context The Context in which the receiver is running.
 * @param appWidgetManager The AppWidgetManager instance
 * @param appWidgetId The id of the widget to update
 * @param number The number to display
 */
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    number: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.my_app_widget)
    views.setTextViewText(R.id.isPrimeQuestion, context.getString(R.string.is_prime_question))
    views.setTextViewText(R.id.appWidgetNumber, number.toString())

    // Create an Intent to launch MainActivity
    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra(RANDOM_NUMBER, number)
    val pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    views.setOnClickPendingIntent(R.id.my_app_widget, pIntent)
    views.setOnClickPendingIntent(R.id.appWidgetBtn, refreshRandomNumber(context, appWidgetId))
    views.setOnClickPendingIntent(R.id.btnTrue, notify(context, appWidgetId, number, true))
    views.setOnClickPendingIntent(R.id.btnFalse, notify(context, appWidgetId, number, false))

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

/**
 * Creates a pending intent to refresh the widget.
 * @param context The Context in which the receiver is running.
 * @param appWidgetId The id of the widget to update
 * @return The pending intent
 */
internal fun refreshRandomNumber(context: Context, appWidgetId: Int) : PendingIntent{
    val intent = Intent(context, MyAppWidget::class.java)
    intent.putExtra(APP_WIDGET_ID, appWidgetId)
    intent.flags = Intent.FLAG_RECEIVER_FOREGROUND
    intent.action = WidgetActionEnum.REFRESH.toString()
    return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

/**
 * Creates a pending intent to notify the user of the result of the guess.
 * @param context The Context in which the receiver is running.
 * @param appWidgetId The id of the widget to update
 * @param number The number to display
 * @param guessIsPrime The guess
 * @return The pending intent
 */
internal fun notify(context: Context, appWidgetId: Int, number: Int, guessIsPrime: Boolean) : PendingIntent{
    println(guessIsPrime)
    val intent = Intent(context, MyAppWidget::class.java)
    intent.putExtra(APP_WIDGET_ID, appWidgetId)
    intent.putExtra(RANDOM_NUMBER, number)
    intent.putExtra(GUESS_IS_PRIME, guessIsPrime)
    intent.flags = Intent.FLAG_RECEIVER_FOREGROUND
    intent.action = WidgetActionEnum.NOTIFY.toString()
    return if (guessIsPrime){
        PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    } else {
        PendingIntent.getBroadcast(context, -appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}