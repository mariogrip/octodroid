package com.mariogrip.octodroid;

import android.annotation.TargetApi;
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
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class Activity extends ActionBarActivity {
    public static String jsonData_job;
    public static String jsonData_connetion;
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
    }

    public void runner(){
        timerTask = new TimerTask() {
            @Override
            public void run() {

                Activity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        get.refreshJson("http://mariogrip.com/OctoPrint");
                        TextView texttime = (TextView)findViewById(R.id.textView11_time);
                        TextView textpri = (TextView)findViewById(R.id.textView16_printed);
                        TextView textest = (TextView)findViewById(R.id.textView13_est);
                        TextView texthei = (TextView)findViewById(R.id.textView15_hei);
                        TextView textfile = (TextView)findViewById(R.id.textView11_file);
                        TextView textmaci = (TextView)findViewById(R.id.textView10_maci);
                        TextView texttarT = (TextView)findViewById(R.id.textView18_tar_t);
                        TextView textcurT = (TextView)findViewById(R.id.textView18_cur_T);
                        TextView textBcur = (TextView)findViewById(R.id.textView18_Bcur_T);
                        TextView textBtar = (TextView)findViewById(R.id.textView18_Btar_T);
                        TextView textprinttime = (TextView)findViewById(R.id.textView17_print_time);
                        TextView textfila = (TextView)findViewById(R.id.textView12_fila);

                        if (get.getData("job","filepos").toString() == "null" || get.getData("job","size").toString() == "null" || get.getData("job","size").toString() == "" ){
                            textpri.setText("-/-");
                        }else{
                            Log.d("test",get.getData("job","size"));
                           textpri.setText(get.toMBGB(Double.parseDouble(get.getData("job","filepos").toString())).toString() + "/" + get.toMBGB(Double.parseDouble(get.getData("job","size").toString())).toString());
                        }
                        texttime.setText(get.toHumanRead(Double.parseDouble(get.getData("job","printTimeLeft"))));
                        textest.setText(get.toHumanRead(Double.parseDouble(get.getData("job","estimatedPrintTime").toString())));
                        texthei.setText("-");
                        textfile.setText(get.getData("job","name").toString());
                        textmaci.setText("-");
                        texttarT.setText("-");
                        textcurT.setText("-");
                        textBcur.setText("-");
                        textBtar.setText("-");
                        textfila.setText("-");
                        textprinttime.setText(get.toHumanRead(Double.parseDouble(get.getData("job","printTime").toString())));
                    }
                });
                }

        };
        timer.schedule(timerTask, 0, 10000);



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

                break;

        }

    }
}
