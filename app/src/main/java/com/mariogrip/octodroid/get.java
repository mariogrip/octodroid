package com.mariogrip.octodroid;

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

    public static String toMBGB(double bytes){
        String returnData;
        double fileSizeInKB = bytes / 1024;
        double fileSizeInMB = fileSizeInKB / 1024;
        if (fileSizeInMB <= 1){
            returnData = fileSizeInMB + "MB";
        }else{
            returnData = fileSizeInKB + "KB";
        }
        return returnData;
    }

    public static String getData(String job,String cmd){
        String returnData = new String();
        boolean noerr;
        if (Activity.server_status) {
            try {
                if (job == "job"){
                    JSONObject json = new JSONObject(jsonData_job);
                    //Progress
                    if (cmd == "printTime"){
                        JSONObject printTime_json = new JSONObject(json.getString("progress"));
                        returnData = printTime_json.getString("printTime");
                    }
                    if (cmd == "printTimeLeft"){
                        JSONObject printTime_json = new JSONObject(json.getString("progress"));
                        returnData = printTime_json.getString("printTimeLeft");
                    }
                    if (cmd == "filepos"){
                        JSONObject printTime_json = new JSONObject(json.getString("progress"));

                        returnData = printTime_json.getString("filepos");
                    }
                    if (cmd == "completion"){
                        JSONObject printTime_json = new JSONObject(json.getString("progress"));
                        returnData = printTime_json.getString("completion");
                    }
                    //files
                    if (cmd == "name"){
                        JSONObject printTime_json = new JSONObject(json.getString("file"));
                        returnData = printTime_json.getString("name");
                    }
                    if (cmd == "size"){
                        JSONObject printTime_json = new JSONObject(json.getString("file"));
                        returnData = printTime_json.getString("size");
                    }

                    //job
                    if (cmd == "estimatedPrintTime"){
                        JSONObject printTime_json = new JSONObject(json.getString("job"));
                        returnData = printTime_json.getString("estimatedPrintTime");
                    }
                    //no under
                    if (cmd == "state"){
                        returnData = json.getString("state");
                    }

                }
                if (job == "connetion"){


                }
                if (job == "printer"){


                }
                if (job == "files"){


                }
                if (job == "version"){


                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnData;
}


    public static void refreshJson(String ip){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(ip+"/api/job");
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
        jsonData_job = builder.toString();
    }
}
