package com.pluralsight.workwithwidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/* Updating Widgets:
 1 - Widget provider update period ( xml provider info xml, minimum is 30 minutes (1800000 ms) )
 2 - Application triggered update
 3 - AlarmManager triggered update period
 */

// WidgetProvider is similar to the BroadcastReceiver
public class AKittyWidget extends AppWidgetProvider {
    public static final String ACTION_EXPLICIT_UPDATE = "com.pluralsight.action.EXPLICIT_UPDATE";
    public static final long EXPLICIT_UPDATE_INTERVAL = 5000; // 5 sec. NEVER USE SUCH SMALL INTERVAL

    static PendingIntent getExplicitUpdatePendingIntent(Context context) {
        Intent intent = getExplicitUpdateIntent(context);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        return pendingIntent;
    }

    static Intent getExplicitUpdateIntent(Context context) {
        Intent intent = new Intent(context, AKittyWidget.class);
        intent.setAction(ACTION_EXPLICIT_UPDATE);
        return intent;
    }

    public void onReceive(Context context, Intent intent) {
        // check action and handle it
        String action = intent.getAction();
        LogHelper.log(String.format("Received action: %s", action));

        if (action.equals(ACTION_EXPLICIT_UPDATE)) {
            // explicitly update widgets
            doExplicitUpdate(context, intent);
        } else {
            super.onReceive(context, intent);
        }
    }

    private void doExplicitUpdate(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName appWidgetComponentName = new ComponentName(context, AKittyWidget.class);

        // try to get widget id that needs to be updated
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        // whether we should update all widgets or single one (config)
        int[] appWidgetIds = appWidgetId!=AppWidgetManager.INVALID_APPWIDGET_ID?
                new int[]{appWidgetId}:
                appWidgetManager.getAppWidgetIds(appWidgetComponentName);

        // Achtung! Check that ids exist at least 1 instance is added to the home screen
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            // trigger default onUpdate
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    // widget requires updating or instance is added to the home screen.
    // APPWIDGET_UPDATE - broadcast received
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        LogHelper.log(String.format("Widget ID Count:%d", appWidgetIds.length));

        // loop through existing widgets
        for (int index = 0; index < appWidgetIds.length; index++) {
            int appWidgetId = appWidgetIds[index];
            LogHelper.log(String.format("Widget ID:%d", appWidgetId));

            RemoteViews appWidgetViews = getWidgetRemoteViews(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
        }

    }

    public static RemoteViews getWidgetRemoteViews(Context context, int appWidgetId) {
        Intent button1Intent = new Intent(context, ShowImageActivity.class);
        button1Intent.putExtra(ShowImageActivity.EXTRA_IMAGE_RESOURCE_ID, R.drawable.fwankwin_chair_kitkat);
        PendingIntent button1PendingIntent = PendingIntent.getActivity(context, 0, button1Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent button2Intent = new Intent(context, ShowImageActivity.class);
        button1Intent.putExtra(ShowImageActivity.EXTRA_IMAGE_RESOURCE_ID, R.drawable.fwankwin_gracie_basket);
        PendingIntent button2PendingIntent = PendingIntent.getActivity(context, 0, button2Intent, PendingIntent.FLAG_UPDATE_CURRENT);


        // determine which widget to show
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

        boolean use2Buttons=options.getBoolean(AKittyWidgetConfigActivity.USE_2_BUTTONS,true);
        int resourceId = use2Buttons? R.layout.a_simple_kitty_widget: R.layout.a_simple_kitty_widget_1_button;

        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(),
                resourceId);
        appWidgetViews.setOnClickPendingIntent(R.id.btn1, button1PendingIntent);

        return appWidgetViews;
    }

    // an instance of the widget was removed from the home screen
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    // the FIRST INSTANCE of the widget was added to the home screen
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    // the LAST INSTANCE of the widhet was removed from the home screen
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
