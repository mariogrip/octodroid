package com.mariogrip.octodroid;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class Activity extends ActionBarActivity {
    public static String jsonData_job;
    public static String jsonData_connetion;
    public static String jsonData_printer;
    protected SharedPreferences prefs;
    private get get_class;
    protected String ip;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    public static boolean server_status = false;
    private static final int RESULT_SETTINGS = 1;
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Just for testing, allow network access in the main thread
        //NEVER use this is productive code
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_tab);
        prefs = PreferenceManager.getDefaultSharedPreferences(Activity.this);
        ip = prefs.getString("ip", "localhost");
        get_class = new get();
        Log.d("OctoPrint","test");
        runner();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setMessage("I understand that this application is experimental and might crash. There are some functions that do not work like push notifications. Please report bugs!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Activity.this.finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void runner(){
        timerTask = new TimerTask() {
            @Override
            public void run() {

                Activity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        get.refreshJson(ip, "job");
                        get.refreshJson(ip, "printer");

                        if (server_status) {
                            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
                            progress.setProgress(get.getProgress());
                            TextView texttime = (TextView) findViewById(R.id.textView11_time);
                            TextView textpri = (TextView) findViewById(R.id.textView16_printed);
                            TextView textest = (TextView) findViewById(R.id.textView13_est);
                            TextView texthei = (TextView) findViewById(R.id.textView15_hei);
                            TextView textfile = (TextView) findViewById(R.id.textView11_file);
                            TextView textmaci = (TextView) findViewById(R.id.textView10_maci);
                            TextView texttarT = (TextView) findViewById(R.id.textView18_tar_t);
                            TextView textcurT = (TextView) findViewById(R.id.textView18_cur_T);
                            TextView textBcur = (TextView) findViewById(R.id.textView18_Bcur_T);
                            TextView textBtar = (TextView) findViewById(R.id.textView18_Btar_T);
                            TextView textprinttime = (TextView) findViewById(R.id.textView17_print_time);
                            TextView textfila = (TextView) findViewById(R.id.textView12_fila);
                            TextView texttimel = (TextView) findViewById(R.id.textView14_timel);

                            if (get.getData("job", "filepos").toString() == "null" || get.getData("job", "size").toString() == "null" || get.getData("job", "size").toString() == "") {
                                textpri.setText(" " + "-/-");
                            } else {
                                textpri.setText(" " + get.toMBGB(Double.parseDouble(get.getData("job", "filepos").toString())).toString() + "/" + get.toMBGB(Double.parseDouble(get.getData("job", "size").toString())).toString());
                            }
                            texttime.setText(" " + get.toHumanRead(Double.parseDouble(get.getData("job", "printTimeLeft"))));
                            textest.setText(" " + get.toHumanRead(Double.parseDouble(get.getData("job", "estimatedPrintTime").toString())));
                            texthei.setText(" " + "-");
                            textfile.setText(" " + get.getData("job", "name").toString());
                            textmaci.setText(" " + get.getData("job", "state").toString());
                            texttarT.setText(" " + get.getData("printer", "target") + "°C");
                            textcurT.setText(" " + get.getData("printer", "actual") + "°C");
                            textBcur.setText(" " + get.getData("printer", "Bactual") + "°C");
                            textBtar.setText(" " + get.getData("printer", "Btarget") + "°C");
                            textfila.setText(" " + "-");
                            texttimel.setText(" " + "-");
                            textprinttime.setText(" " + get.toHumanRead(Double.parseDouble(get.getData("job", "printTime").toString())));
                        }
                    }
                });
                }

        };
        timer.schedule(timerTask, 0, 3000);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, settings.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                prefs = PreferenceManager.getDefaultSharedPreferences(Activity.this);
                ip = prefs.getString("ip", "localhost");
                break;

        }

    }
}
