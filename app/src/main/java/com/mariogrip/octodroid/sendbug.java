package com.mariogrip.octodroid;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sendbug extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bug);

        Button up = (Button) findViewById(R.id.button_send_bug);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText errorRaport = (EditText) findViewById(R.id.editText_bug);
                Log.d("OctoDroid", "SendErrRap Pressed");
                if (errorRaport.getText().toString().equals("") || errorRaport.getText().toString() == "" || errorRaport.getText() == null || errorRaport.getText().toString().isEmpty()){
                    Toast.makeText(sendbug.this, "You cannot send a empty bug rapport", Toast.LENGTH_SHORT).show();
                }else {
                    util.sendError("BUG_RAPORT:" + errorRaport.getText().toString().replaceAll("[^A-Za-z0-9()\\[\\]]", "_"));
                    Toast.makeText(sendbug.this, "Thanks for the Bug rapport, we will try to fix the bug", Toast.LENGTH_LONG).show();
                    sendbug.this.finish();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sendbug, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
