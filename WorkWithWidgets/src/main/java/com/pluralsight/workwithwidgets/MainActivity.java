package com.pluralsight.workwithwidgets;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            setupButtons(rootView);
            return rootView;
        }

        public void setupButtons(View rootView) {
            rootView.findViewById(R.id.btnChangeWidget).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnChangeWidgetOnClick((Button) v);
                }
            });
            rootView.findViewById(R.id.btnStartAlarmManager).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnStartAlarmManagerOnClick((Button) v);
                }
            });
            rootView.findViewById(R.id.btnStopAlarmManager).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnStopAlarmManagerOnClick((Button) v);
                }
            });
        }

        private void btnChangeWidgetOnClick(Button v) {
            Context context = getActivity();

            // application-triggered widget update

            // 1. retrieve AppWidgetManager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            // get all widget ids that are implemented by our particular component
            // used to distinguish widget by their class/component
            ComponentName appWidgetComponentName = new ComponentName(context, AKittyWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidgetComponentName);

            // 2. Replace RemoteViews just as you do in provider's onUpdate
            // loop through widgets and update them (replace)
            for (int index = 0; index < appWidgetIds.length; index++) {
                int appWidgetId = appWidgetIds[index];
                LogHelper.log(String.format("Widget ID:%d", appWidgetId));

                RemoteViews appWidgetViews = AKittyWidget.getWidgetRemoteViews(context,appWidgetId);
                // update views
                appWidgetViews.setCharSequence(R.id.txtTitleText, "setText", "MEOW!!");
                // findViewById(R.id.txtTitleText).setText("Meow!!");

                appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
            }
        }

        private void btnStartAlarmManagerOnClick(Button v) {
            Context context = getActivity();
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = AKittyWidget.getExplicitUpdatePendingIntent(context);

            // configure alarm manager
            long currentTimeMillis = System.currentTimeMillis();
            long intervalTimeMillis = AKittyWidget.EXPLICIT_UPDATE_INTERVAL;
            am.setInexactRepeating(AlarmManager.RTC,
                    currentTimeMillis + intervalTimeMillis,
                    intervalTimeMillis,
                    pendingIntent);

        }

        private void btnStopAlarmManagerOnClick(Button v) {
            Context context = getActivity();
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // cancel operation, which is represented by PendingIntent
            PendingIntent pendingIntent = AKittyWidget.getExplicitUpdatePendingIntent(context);
            am.cancel(pendingIntent);
        }

        private static PendingIntent getMyWidgetAlarmPendingIntent(Context context) {
            PendingIntent pendingIntent = null;

            return pendingIntent;
        }
    }

}
