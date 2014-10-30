package com.mariogrip.octodroid;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mariogrip on 30.10.14.
 */

public class service extends IntentService {
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Timer timer2 = new Timer();
    private TimerTask timerTask2;
    private double complete;
    private int intcom;

    public service() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        runner();
    }

    public void runner() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (Activity.server_status) {
                    get.refreshJson(Activity.ip, "job", Activity.key);
                    get.decodeJsonService();
                    Log.d("OctoDroid Service", "runner" + Activity.printing);

                    if (Activity.printing) {
                        Activity.printing = true;
                        timerTask.cancel();
                        startPrintService();
                        return;
                    }
                    if (get.getData("job", "state").equals("Printing") && Activity.printing == false) {
                        Activity.printing = true;
                        timerTask.cancel();
                        startPrintService();
                        return;
                    }
                    if (!get.getData("job", "state").equals("Printing") && Activity.printing == true) {
                        Activity.printing = false;
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 3000);

    }

    protected void startPrintService() {
        final int id = 1;
        final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Log.d("OctoDroid Service", "printService");
        mBuilder.setContentTitle("OctoDroid")
                .setContentText("Print in progress")
                .setSmallIcon(R.drawable.octoprint);

        timerTask2 = new TimerTask() {
            @Override
            public void run() {
                Log.d("OctoDroid Service", "runner");
                get.refreshJson(Activity.ip, "job", Activity.key);
                get.decodeJsonService();
                complete = Double.parseDouble(get.getData("job", "completion"));


                if (!get.getData("job", "state").equals("Printing") && Activity.printing == true) {
                    Activity.printing = false;
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setContentText("Print complete")
                            .setSound(soundUri)
                            .setProgress(0, 0, false);
                    mNotifyManager.notify(id, mBuilder.build());
                    timerTask2.cancel();
                    runner();
                    return;
                }

                mBuilder.setProgress(100, (int) complete, false);
                mNotifyManager.notify(id, mBuilder.build());
            }
        };
        timer2.schedule(timerTask2, 0, 3000);
    }
}