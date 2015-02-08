package com.mariogrip.octodroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mariogrip.octodroid.qr.IntentIntegrator;
import com.mariogrip.octodroid.qr.IntentResult;

/**
 * Created by mariogrip on 08.02.15.
 */
public class welcome extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        Button qr = (Button) findViewById(R.id.button_QR);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(welcome.this);
                integrator.initiateScan();
            }
        });
        Button skip = (Button) findViewById(R.id.button_Skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memory.skipWelcom = true;
                Intent main = new Intent(welcome.this, mainActivity.class);
                welcome.this.startActivity(main);
            }
        });
        Button check = (Button) findViewById(R.id.button_check_con);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ippp = (EditText) findViewById(R.id.editText_welcome_IP);
                EditText KEY = (EditText) findViewById(R.id.editText_welcome_API);

                if (ippp.getText().toString().equals("")){
                    Toast.makeText(welcome.this, "Please fill in IP address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (KEY.getText().toString().equals("")){
                    Toast.makeText(welcome.this, "Please fill in API key", Toast.LENGTH_SHORT).show();
                    return;
                }
                    new CheckCon().execute(new String[]{ippp.getText().toString(), KEY.getText().toString()});
            }
        });
    }
    public class CheckCon extends AsyncTask<String, Void, String[]>{
        private ProgressDialog prog;
        private String ip1;
        private String key1;
        @Override
        protected void onPreExecute() {
            prog = ProgressDialog.show(welcome.this, "Please Wait...", "Trying Connection....", true);
            prog.setCancelable(false);
        }

        @Override
        protected String[] doInBackground(String... ip) {
            String[] returnThis = new String[]{""};
            switch (util.isPingServer(ip[0])){
                case 0:
                    returnThis = new String[]{ip[0], "true", ip[1]};
                    break;
                case 1:
                    returnThis = new String[]{ip[0], "false", ip[1]};
                    break;
                case 2:
                    returnThis = new String[]{ip[0], "false", ip[1]};
                    break;
            }
            return returnThis;
        }

        @Override
        protected void onPostExecute(String[] ipp) {
            prog.dismiss();
            ip1 = ipp[0];
            key1 = ipp[2];
            super.onPostExecute(ipp);
            if (ipp[1].contains("false")){
                AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                builder.setTitle("Failed");
                builder.setMessage("OctoDroid could not connect to " + ip1 + "\n\nPlease check https://github.com/mariogrip/octodroid/wiki for more info");
                builder.setPositiveButton("Save IP anyway", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(welcome.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ip", ip1);
                        editor.putString("api", key1);
                        editor.commit();
                        Intent i = new Intent(welcome.this, mainActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                builder.setTitle("Success");
                builder.setMessage("OctoDroid could successfully connect to " + ip1 + "\n\nGo to the next step to try to send a json response to octoprint");
                builder.setPositiveButton("Next Step ->", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(welcome.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ip", ip1);
                        editor.putString("api", key1);
                        editor.commit();
                        new CheckCon2().execute(new String[]{ip1, key1});
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
    }

    public class CheckCon2 extends AsyncTask<String, Void, String[]>{
        private ProgressDialog prog;
        private String ip1;
        private String key1;
        @Override
        protected void onPreExecute() {
            prog = ProgressDialog.show(welcome.this, "Please Wait...", "Trying Json Response....", true);
            prog.setCancelable(false);
        }

        @Override
        protected String[] doInBackground(String... ip) {
            String[] returnThis = new String[]{""};
            String check = util.checkIPWithServer(ip[0], ip[1]);
            if (check.contains("null")){
                returnThis = new String[]{ip[0], "false", ip[1]};
            }else{
                returnThis = new String[]{ip[0],check , ip[1]};
            }
            return returnThis;
        }

        @Override
        protected void onPostExecute(String[] ipp) {
            prog.dismiss();
            ip1 = ipp[0];
            key1 = ipp[2];
            super.onPostExecute(ipp);
            if (ipp[1].contains("false")){
                AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                builder.setTitle("Failed");
                builder.setMessage("OctoDroid could not send json request (with API) to " + ip1 + "\n\nPlease check https://github.com/mariogrip/octodroid/wiki for more info");
                builder.setPositiveButton("Save anyway", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(welcome.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ip", ip1);
                        editor.putString("api", key1);
                        editor.commit();
                        Intent i = new Intent(welcome.this, mainActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                builder.setTitle("Success");
                builder.setMessage("OctoDroid could successfully send json response to " + ip1 + "\n\nGo to next step to test the API. NOTE: next step will home x and y axis.");
                builder.setPositiveButton("Next Step ->", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(welcome.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ip", ip1);
                        editor.putString("api", key1);
                        new CheckCon3().execute(new String[]{ip1, key1});
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
    }

    public class CheckCon3 extends AsyncTask<String, Void, String[]>{
        private ProgressDialog prog;
        private String ip1;
        private String key1;
        @Override
        protected void onPreExecute() {
            prog = ProgressDialog.show(welcome.this, "Please Wait...", "Trying Json Response with API....", true);
            prog.setCancelable(false);
        }

        @Override
        protected String[] doInBackground(String... ip) {
            String[] returnThis = new String[]{""};
            if (!util.checkAPIWithServer(ip[0], ip[1])){
                returnThis = new String[]{ip[0], "false", ip[1]};
            }else{
                returnThis = new String[]{ip[0],"true" , ip[1]};
            }
            return returnThis;
        }

        @Override
        protected void onPostExecute(String[] ipp) {
            prog.dismiss();
            ip1 = ipp[0];
            key1 = ipp[2];
            super.onPostExecute(ipp);
            if (ipp[1].contains("false")){
                AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                builder.setTitle("Failed");
                builder.setMessage("OctoDroid could not use that API key  " + ip1 + "\n\nPlease check https://github.com/mariogrip/octodroid/wiki for more info");
                builder.setPositiveButton("Save anyway", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(welcome.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ip", ip1);
                        editor.putString("api", key1);
                        editor.commit();
                        Intent i = new Intent(welcome.this, mainActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                builder.setTitle("Success");
                builder.setMessage("Did that home your x/y axis on you printer?");
                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                        builder.setTitle("Success");
                        builder.setMessage("The setup is complete, You can now enjoy OctoDroid");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(welcome.this);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("ip", ip1);
                                editor.putString("api", key1);
                                editor.commit();
                                Intent i = new Intent(welcome.this, mainActivity.class);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("Try Agian", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog2 = builder.create();
                        dialog2.show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
                        builder.setTitle("Failed");
                        builder.setMessage("Setup not complete, API not working. OctoDroid will be able to display information, but you cannot control or set temperature. \n\nmore info: https://github.com/mariogrip/octodroid/wiki");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(welcome.this);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("ip", ip1);
                                editor.putString("api", key1);
                                editor.commit();
                                Intent i = new Intent(welcome.this, mainActivity.class);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("Try agian", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog2 = builder.create();
                        dialog2.show();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            EditText qredit = (EditText) findViewById(R.id.editText_welcome_API);
            qredit.setText(scanResult.getContents());
        }
        // else continue with any other code you need in the method
    }
}
