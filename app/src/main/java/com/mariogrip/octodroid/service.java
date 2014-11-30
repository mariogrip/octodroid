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
 *
 * GNU Affero General Public License http://www.gnu.org/licenses/agpl.html
 */

public class service extends IntentService {
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Timer timer2 = new Timer();
    private TimerTask timerTask2;
    private double complete;
    private int intcom;

    public service() {
        super("OctoDroidService");
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
                    util.refreshJson(Activity.ip, "job", Activity.key);
                    util.decodeJsonService();
                    Log.d("OctoDroid Service", "runner" + Activity.printing);
                    if (Activity.printing) {
                        Activity.printing = true;
                        timerTask.cancel();
                        startPrintService();
                        return;
                    }
                    if (util.getData("job", "state").equals("Printing") && !Activity.printing) {
                        Activity.printing = true;
                        timerTask.cancel();
                        startPrintService();
                        return;
                    }
                    if (!util.getData("job", "state").equals("Printing") && Activity.printing) {
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
                .setSmallIcon(R.drawable.octodroid_smal);
        timerTask2 = new TimerTask() {
            @Override
            public void run() {
                Log.d("OctoDroid Service", "runner");
                util.refreshJson(Activity.ip, "job", Activity.key);
                util.decodeJsonService();
                complete = Double.parseDouble(util.getData("job", "completion"));


                if (!util.getData("job", "state").equals("Printing") && Activity.printing) {
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