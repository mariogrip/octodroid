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
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

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
                if (mainActivity.server_status) {
                    util.refreshJson(mainActivity.ip, "job", mainActivity.key);
                    util.decodeJsonService();
                    Log.d("OctoDroid Service", "runner" + mainActivity.printing);
                    if (mainActivity.printing) {
                        mainActivity.printing = true;
                        timerTask.cancel();
                        startPrintService();
                        return;
                    }
                    if (util.getData("job", "state").equals("Printing") && !mainActivity.printing) {
                        mainActivity.printing = true;
                        timerTask.cancel();
                        startPrintService();
                        return;
                    }
                    if (!util.getData("job", "state").equals("Printing") && mainActivity.printing) {
                        mainActivity.printing = false;
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 7000);

    }

    protected void startPrintService() {
        final int id = 1;
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        Log.d("OctoDroid Service", "StartprintService");
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentTitle("OctoDroid")
                .setContentText("Printing")
                .setOngoing(true)
                .setSmallIcon(R.drawable.octodroid_smal);
        mNotifyManager.notify(id, mBuilder.build());

        timerTask2 = new TimerTask() {
            @Override
            public void run() {
                Log.d("OctoDroid Service", "startPrintService timertask");
                try {
                    util.refreshJson(mainActivity.ip, "job", mainActivity.key);
                    util.decodeJsonService();
                    complete = Double.parseDouble(util.getData("job", "completion"));
                }catch (Exception e){
                    complete = 0;
                }
                if (!util.getData("job", "state").equals("Printing")) {
                    Log.d("OctoDroid Service", "startPrintService stopping");
                    mainActivity.printing = false;
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setContentText("Print complete")
                            .setSound(soundUri)
                            .setProgress(0, 0, false);
                    mNotifyManager.notify(id, mBuilder.build());
                    timerTask2.cancel();
                    runner();
                    return;
                }
                Log.d("OctoDroid Service", "startPrintService Notify" + complete );
               mBuilder.setProgress(100, (int) complete, false).setContentText("Printing (" + (int) complete + "%)");
               mNotifyManager.notify(id, mBuilder.build());
            }
        };
        timer2.schedule(timerTask2, 0, 4000);
    }
}