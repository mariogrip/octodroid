package com.mariogrip.octodroid.iu;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mariogrip.octodroid.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class gcode extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcode);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        Log.d("test", "Test");
        webSettings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        String javaSript="<h1>Hello</h1>";
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.loadUrl("http://gcode.joewalnes.com");
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                StringBuilder buf=new StringBuilder();
                InputStream json= null;
                try {
                    json = getAssets().open("15mm_cube.gcode");
                    BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
                    String str;
                    while ((str=in.readLine()) != null) {
                        buf.append(str + "\n");
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //view.loadUrl("javascript:openGCodeFromText('" + buf +"')");
                //view.loadUrl("javascript:openGCodeFromPath('file:///android_asset/15mm_cube.gcode')");
                Log.d("Hello","Doing This then");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gcode, menu);
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
    class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
