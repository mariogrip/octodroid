package com.mariogrip.octodroid;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
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
        Double fileSizeInKB = bytes / 1024;
        Double fileSizeInMB = fileSizeInKB / 1024;
        Double fileSizeInKBformat = roundDown5(fileSizeInKB);
        Double fileSizeInMBformat = roundDown5(fileSizeInMB);
        if (fileSizeInMB >= 1){
            returnData = fileSizeInMBformat.toString() + "MB";
        }else{
            returnData = fileSizeInKBformat.toString() + "KB";
        }
        return returnData;
    }
    public static double roundDown5(double d) {
        return (long) (d * 1e2) / 1e2;
    }
    public static String toHumanRead(double biggy)
    {
        int hours = (int) biggy / 3600;
        int remainder = (int) biggy - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return hours + ":" + mins + ":" + secs;
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
                    //file
                    if (cmd == "name"){
                        JSONObject printTime_json = new JSONObject(json.getString("job"));
                        JSONObject printTime_json2 = new JSONObject(printTime_json.getString("file"));
                        returnData = printTime_json2.getString("name");
                    }
                    if (cmd == "size"){
                        JSONObject printTime_json = new JSONObject(json.getString("job"));
                        JSONObject printTime_json2 = new JSONObject(printTime_json.getString("file"));
                        String string1 = printTime_json2.getString("size");
                        if (string1.equals("null") || string1.equals("")){
                            returnData = "0";
                        }else{
                            returnData = string1;
                        }
                    }

                    //job
                    if (cmd == "estimatedPrintTime"){
                        JSONObject printTime_json = new JSONObject(json.getString("job"));
                        String string1 = printTime_json.getString("estimatedPrintTime");
                        if (string1.equals("null") || string1.equals("")){
                            returnData = "0";
                        }else{
                            returnData = string1;
                        }

                    }
                    //no under
                    if (cmd == "state"){
                        if (json.getString("state").equals("")){
                            returnData = "-";
                        }else{
                            returnData = json.getString("state");
                        }
                    }

                }
                if (job == "connetion"){


                }
                if (job == "printer"){
                    JSONObject json = new JSONObject(jsonData_printer);
                    JSONObject printTime_json = new JSONObject(json.getString("temps"));

                    if (cmd == "Bactual") {
                        JSONObject temp = new JSONObject(printTime_json.getString("bed"));
                        String string1 = temp.getString("actual");
                        if (string1.equals("null") || string1.equals("")) {
                            returnData = "0";
                        } else {
                            returnData = string1;
                        }
                    }
                    if (cmd == "Btarget"){

                        JSONObject temp = new JSONObject(printTime_json.getString("bed"));
                        String string1 = temp.getString("target");
                        if (string1.equals("null") || string1.equals("")){
                            returnData = "0";
                        }else{
                            returnData = string1;
                        }
                    }
                    if (cmd == "actual") {
                        JSONObject temp = new JSONObject(printTime_json.getString("tool0"));
                        String string1 = temp.getString("actual");
                        if (string1.equals("null") || string1.equals("")) {
                            returnData = "0";
                        } else {
                            returnData = string1;
                        }
                    }
                    if (cmd == "target"){
                        JSONObject temp = new JSONObject(printTime_json.getString("tool0"));
                        String string1 = temp.getString("target");
                        if (string1.equals("null") || string1.equals("")){
                            returnData = "0";
                        }else{
                            returnData = string1;
                        }
                    }



                }
                if (job == "files"){


                }
                if (job == "version"){


                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (returnData.equals("null") || returnData.equals("")){
            returnData = "0";
        }
        if (returnData.length() > 20){
            returnData = returnData.substring(0, 20);
            returnData = returnData + "...";
        }
        return returnData;
}
    public static int getProgress(){
        int returnData = 0;
        if (Activity.server_status) {
            JSONObject json = null;
            try {
            json = new JSONObject(jsonData_job);
            JSONObject printTime_json = new JSONObject(json.getString("progress"));
                double acom = Double.parseDouble(printTime_json.getString("completion"));
               if (acom < 0){
                    returnData = 0;
                }else{
                   returnData = (int) acom;
               }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return returnData;
    }

    public static void refreshJson(String ip, String api){
        if (ip == null || ip.equals("")){

        }else {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet;
            if (ip.startsWith("http://")){
                httpGet = new HttpGet(ip + "/api/"+api);
            }else{
                httpGet = new HttpGet("http://"+ ip + "/api/"+api);
            }
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
                Log.e("OctoDroid", "ClientProtocolException");
                Log.d("OctoDroid", ip);
                Activity.server_status = false;
            } catch (IOException e) {
                Log.e("OctoDroid", "IOException");
                Log.d("OctoDroid", ip);
                Activity.server_status = false;
            } catch (IllegalStateException e){
                Log.e("OctoDroid", "IllegalStateException");
                Log.d("OctoDroid", ip);
                Activity.server_status = false;
            }
            if (api.equals("job")){
                 jsonData_job = builder.toString();
            }
            if (api.equals("printer")){
                jsonData_printer = builder.toString();
            }
        }
    }
}
