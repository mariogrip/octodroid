package com.mariogrip.octodroid;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mariogrip.octodroid.iu.controls;
import com.mariogrip.octodroid.iu.main_card;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mariogrip on 27.10.14.
 *
 * GNU Affero General Public License http://www.gnu.org/licenses/agpl.html
 */
public class Activity extends ActionBarActivity {
    protected int pos;
    private String[] nawTitle;
    private DrawerLayout nawlay;
    private ListView nawList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static String jsonData_job;
    public static String jsonData_connetion;
    public static String jsonData_printer;
    protected SharedPreferences prefs;
    private util get_class;
    private boolean senderr = false;
    protected static boolean running = false;
    protected static boolean printing;
    protected static boolean push = true;
    protected static boolean servicerunning =false;
    public static String ip;
    public static String key;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Timer timer2 = new Timer();
    private TimerTask timerTask2;
    public static boolean server_status = false;
    private static final int RESULT_SETTINGS = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO Remove StrictMode and add AsyncTask!
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.nawdraw);
        mTitle = mDrawerTitle = getTitle();
        nawTitle = getResources().getStringArray(R.array.nawbars);
        nawlay = (DrawerLayout) findViewById(R.id.drawer_layout);
        nawList = (ListView) findViewById(R.id.left_drawer);
        nawList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.nawlist, nawTitle));
        nawList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                nawlay,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        nawlay.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(Activity.this);
        ip = prefs.getString("ip", "localhost");
        key = prefs.getString("api", "0");
        senderr = prefs.getBoolean("err", true);
        push = prefs.getBoolean("push", true);
        running = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Configure");
        builder.setMessage("Do you want to configure OctoDroid?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences sharedPref = Activity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("setup", false);
                editor.commit();
                Intent i = new Intent(Activity.this, settings.class);
                startActivityForResult(i, RESULT_SETTINGS);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        SharedPreferences sharedPref = Activity.this.getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("setup", true)){
            dialog.show();
        }
        logD("Done!");
    }
    public void startrunner(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Activity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        TextView textmaci = (TextView) findViewById(R.id.textView10_maci);
                     //   textmaci.setText("Offline");
                    }
                 });
               runner();
                logD("Done startrunner()");
            }
        };
        new Thread(runnable).start();
    }
    public void startservice(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logD("Starting Service");
                if (push) {
                    if (!servicerunning) {
                        servicerunning = true;
                        Intent mServiceIntent = new Intent(Activity.this, service.class);
                        Activity.this.startService(mServiceIntent);
                    }
                }else{
                    if (servicerunning){
                        logD("starting runner");
                        servicerunning = false;
                        Intent mServiceIntent = new Intent(Activity.this, service.class);
                        Activity.this.stopService(mServiceIntent);
                    }
                }
                logD("Done startservice()");
            }
        };
        new Thread(runnable).start();
    }

    public void servererr(){
        timerTask2 = new TimerTask() {
            @Override
            public void run() {
                util.refreshJson(ip, "job", key);
                if (server_status){
                    runner();
                    timerTask2.cancel();
                    return;
                }
            }

        };
        timer2.schedule(timerTask2, 0, 10000);
    }

    public void logD(String e){
        Log.d("OctoDroid",e);
    }
    public void runner(){
        util.refreshJson(ip, "job", key);
        if (running){
            logD("Stopping runner, Might started twice");
            return;
        }
        if (!running){
            logD("OneRunStarted");
            running = true;
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Activity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (!server_status){
                            TextView textmaci = (TextView) findViewById(R.id.textView10_maci);
                            textmaci.setText("Cannot connect to\n" + ip);
                            logD("Server Error");
                            running = false;
                            servererr();
                            timerTask.cancel();
                            return;
                        }
                        switch (pos){
                            case 0:
                        util.refreshJson(ip, "printer", key);
                        util.decodeJson();
                        logD("Running runner");
                        if (server_status) {
                            Log.d("test123", util.getData("job", "printTime"));
                            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
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

                            if (util.getData("job", "filepos").toString() == "null" || util.getData("job", "size").toString() == "null" || util.getData("job", "size").toString() == "") {
                                textpri.setText(" " + "-/-");
                            } else {
                                textpri.setText(" " + util.toMBGB(Double.parseDouble(util.getData("job", "filepos").toString())).toString() + "/" + util.toMBGB(Double.parseDouble(util.getData("job", "size").toString())).toString());
                            }
                            texttime.setText(" " + util.toHumanRead(Double.parseDouble(util.getData("job", "printTimeLeft"))));
                            textest.setText(" " + util.toHumanRead(Double.parseDouble(util.getData("job", "estimatedPrintTime").toString())));
                            texthei.setText(" " + "-");
                            textfile.setText(" " + util.getData("job", "name").toString());
                            textmaci.setText(" " + util.getData("job", "state").toString());
                            texttarT.setText(" " + util.getData("printer", "target") + "°C");
                            textcurT.setText(" " + util.getData("printer", "actual") + "°C");
                            textBcur.setText(" " + util.getData("printer", "Bactual") + "°C");
                            textBtar.setText(" " + util.getData("printer", "Btarget") + "°C");
                            textfila.setText(" " + "-");
                            texttimel.setText(" " + "-");
                            textprinttime.setText(" " + util.toHumanRead(Double.parseDouble(util.getData("job", "printTime").toString())));
                            progress.setProgress(util.getProgress());
                        }else{
                            TextView textmaci = (TextView) findViewById(R.id.textView10_maci);
                            textmaci.setText("Cannot connect to\n" + ip);
                        }
                         break;
                            case 2:
                                ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar_controls);
                                TextView texttime = (TextView) findViewById(R.id.printTimeControls);
                                texttime.setText(" " + util.toHumanRead(Double.parseDouble(util.getData("job", "printTimeLeft"))));
                                progress.setProgress(util.getProgress());
                                break;
                            default:
                                break;

                        }
                    }
                });
                }

        };
        timer.schedule(timerTask, 0, 3000);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = nawlay.isDrawerOpen(nawList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, settings.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }
        if (id == R.id.action_bug) {
            Intent i1 = new Intent(this, sendbug.class);
            startActivity(i1);
            return true;
        }
        if (id == R.id.action_git){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mariogrip/octodroid"));
            startActivity(browserIntent);
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
                key = prefs.getString("api", "0");
                push = prefs.getBoolean("push", true);
                if (push) {
                    if (!servicerunning) {
                        servicerunning = true;
                        Intent mServiceIntent = new Intent(this, service.class);
                        this.startService(mServiceIntent);
                    }
                }else{
                    if (servicerunning){
                        servicerunning = false;
                        Intent mServiceIntent = new Intent(this, service.class);
                        this.stopService(mServiceIntent);
                    }
                }
                break;

        }

    }
    public void onPause(){
        super.onPause();
        if (running) {
            running = false;
            timerTask.cancel(); //TODO CRASH NullPointerException
        }
    }
    public void onResume(){
        super.onResume();
        servicerunning = false;
        startservice();
        startrunner();
    }
    public void onStop(){
        super.onStop();
        if (running) {
            running = false;
            timerTask.cancel();
        }
    }
    public void onStart(){
        super.onStart();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        pos = position;
        Fragment fragment = new main_card();
        switch (position) {
            case 0:
                fragment = new main_card();
                break;
            case 1:
                fragment = new PlanetFragment();
                pos = 0;
                position = 0;
                Intent i = new Intent(this, settings.class);
                startActivityForResult(i, RESULT_SETTINGS);
                //fragment = new controls();
                break;
            case 2:
                fragment = new controls();
                break;
            case 3:
                fragment = new main_card();
                break;
            default:
                break;
        }
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        // update selected item and title, then close the drawer
        nawList.setItemChecked(position, true);
        setTitle(nawTitle[position]);
        nawlay.closeDrawer(nawList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        public PlanetFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView;
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String naws = getResources().getStringArray(R.array.nawbars)[i];
            rootView = inflater.inflate(R.layout.status_tab, container, false);
            getActivity().setTitle(naws);

            return rootView;
        }
    }

}
