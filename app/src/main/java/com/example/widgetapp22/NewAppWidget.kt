package com.example.widgetapp22

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val views = RemoteViews(context!!.packageName, R.layout.new_app_widget)
        if (intent?.action == "pic1") {
            views.setImageViewResource(R.id.imageView, R.drawable.picture1)
        } else if (intent?.action == "pic2") {
            views.setImageViewResource(R.id.imageView, R.drawable.picture2)
        }

        val webIntent = Intent(Intent.ACTION_VIEW)
        webIntent.data = Uri.parse("https://google.com")
        val browser = PendingIntent.getActivity(
            context,
            0,
            webIntent,
            FLAG_MUTABLE
        )
        views.setOnClickPendingIntent(R.id.btWeb, browser)

        val serviceIntent = Intent(context, SoundService::class.java).setAction("")
        context.startForegroundService(serviceIntent)

        val pIntent = Intent(context,SoundService::class.java).setAction("Play")
        val pendingPlayIntent = getService(context,0, pIntent, FLAG_MUTABLE)

        val stopIntent = Intent(context, SoundService::class.java).setAction("Stop")
        val pendingStopIntent = getService(context,0, stopIntent, FLAG_MUTABLE)

        val nextIntent = Intent(context, SoundService::class.java).setAction("Next")
        val pendingNextIntent = getService(context,0, nextIntent, FLAG_MUTABLE)

        val prevIntent = Intent(context, SoundService::class.java).setAction("Prev")
        val pendingPrevIntent = getService(context,0, prevIntent, FLAG_MUTABLE)


        views.setOnClickPendingIntent(R.id.btPlay, pendingPlayIntent)
        views.setOnClickPendingIntent(R.id.btStop, pendingStopIntent)
        views.setOnClickPendingIntent(R.id.btNextSong, pendingNextIntent)
        views.setOnClickPendingIntent(R.id.btPrevSong, pendingPrevIntent)

        update(context, views)
        Log.i("widgetOnReceive", "onReceive called")
    }

}

private fun update(context: Context, views: RemoteViews){
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, NewAppWidget::class.java))
    for(id in ids)
        appWidgetManager.partiallyUpdateAppWidget(id, views)
}

private fun imgIntent(context: Context, action: String): PendingIntent? {
    val intent = Intent()
    intent.action = action
    intent.component = ComponentName(context, NewAppWidget::class.java)
    return PendingIntent.getBroadcast(context, 1, intent, FLAG_MUTABLE)
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)

    //Service Intent
    val serviceIntent = Intent(context, SoundService::class.java).setAction("")
    val servicePendingIntent = getService(context, 0, serviceIntent, FLAG_IMMUTABLE)

    views.setOnClickPendingIntent(R.id.btPrevImg, imgIntent(context, "pic1"))
    views.setOnClickPendingIntent(R.id.btNextImg, imgIntent(context, "pic2"))

    views.setOnClickPendingIntent(R.id.btPlay, servicePendingIntent)
    views.setOnClickPendingIntent(R.id.btStop, servicePendingIntent)
    views.setOnClickPendingIntent(R.id.btNextSong, servicePendingIntent)
    views.setOnClickPendingIntent(R.id.btPrevSong, servicePendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}