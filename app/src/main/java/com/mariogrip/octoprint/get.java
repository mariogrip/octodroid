package com.mariogrip.octoprint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mariogrip on 27.10.14.
 */
public class get extends  Activity{
public static String jsonData;

    public static String getData(String job,String cmd){
        String returnData = new String();
        refreshJson("http://mariogrip.com/OctoPrint", job);
        if (Activity.server_status) {
            try {
                JSONObject json = new JSONObject(jsonData);

                if (cmd == "printTime"){
                 JSONObject printTime_json = new JSONObject(json.getString("progress"));
                    returnData = printTime_json.getString("printTime");
                }
                if (cmd == "printTimeLeft"){

                }
                if (cmd == "name"){

                }
                if (cmd == "size"){

                }
                if (cmd == "filepos"){

                }
                if (cmd == "completion"){

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnData;
}


    public static void refreshJson(String ip, String job){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(ip+"/api/"+job);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                Activity.server_status = true;
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Activity.server_status = false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonData = builder.toString();
    }
}
