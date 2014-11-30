package com.mariogrip.octodroid;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mariogrip on 27.10.14.
 *
 * GNU Affero General Public License http://www.gnu.org/licenses/agpl.html
 */
public class util extends  Activity{

    //Converts Bytes to Mega/Giga Bytes
    public static String toMBGB(double bytes){
        //Checks if the app has contact with the server.
        if (!Activity.server_status){
            return "-/-";
        }
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
        if (!Activity.server_status){
            return "00:00:00";
        }
        int hours = (int) biggy / 3600;
        int remainder = (int) biggy - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
    }
    private static JSONObject jsonData_job_job;

    private static JSONObject jsonData_job_job_progress;
    private static JSONObject jsonData_job_job_file;
    private static JSONObject jsonData_job_job_job;
    private static JSONObject jsonData_printer_printer;
    private static JSONObject jsonData_printer_printer_temps;
    private static boolean jsonData_printer_status;

    public static void decodeJson(){
        if (!Activity.server_status){
            return;
        }
        try {
            jsonData_job_job = new JSONObject(jsonData_job);
            jsonData_job_job_progress = new JSONObject(jsonData_job_job.getString("progress"));
            jsonData_job_job_job = new JSONObject(jsonData_job_job.getString("job"));
            jsonData_job_job_file = new JSONObject(jsonData_job_job_job.getString("file"));
            if (jsonData_printer.equals("Printer is not operational")){
                jsonData_printer_status = false;
            }else{
                jsonData_printer_status = true;
                jsonData_printer_printer = new JSONObject(jsonData_printer);
                jsonData_printer_printer_temps = new JSONObject(jsonData_printer_printer.getString("temps"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void decodeJsonService(){
        if (!Activity.server_status){
            return;
        }
        try {
            jsonData_job_job = new JSONObject(jsonData_job);
            jsonData_job_job_progress = new JSONObject(jsonData_job_job.getString("progress"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static String getData(String job,String cmd){
        if (!Activity.server_status){
            return "-";
        }
        String returnData = new String();
        boolean noerr;
        if (Activity.server_status) {
            try {
                if (job == "job") {
                    //Progress
                    if (cmd == "printTime") {
                        returnData = jsonData_job_job_progress.getString("printTime");
                    }
                    if (cmd == "printTimeLeft") {
                        returnData = jsonData_job_job_progress.getString("printTimeLeft");
                    }
                    if (cmd == "filepos") {
                        returnData = jsonData_job_job_progress.getString("filepos");
                    }
                    if (cmd == "completion") {
                        returnData = jsonData_job_job_progress.getString("completion");
                    }
                    //job
                    if (cmd == "name") {
                        String string1 = jsonData_job_job_file.getString("name");
                        if (string1.equals("null") || string1.equals("")){
                            returnData = "-";
                        }else{
                            returnData = string1;
                        }

                    }
                    if (cmd == "size") {
                        String string1 = jsonData_job_job_file.getString("size");
                        if (string1.equals("null") || string1.equals("")) {
                            returnData = "0";
                        } else {
                            returnData = string1;
                        }
                    }
                    if (cmd == "estimatedPrintTime") {
                        String string1 = jsonData_job_job_job.getString("estimatedPrintTime");
                        if (string1.equals("null") || string1.equals("")) {
                            returnData = "0";
                        } else {
                            returnData = string1;
                        }

                    }
                    //no under
                    if (cmd == "state") {
                        if (jsonData_job_job.getString("state").equals("")) {
                            returnData = "-";
                        } else {
                            returnData = jsonData_job_job.getString("state");
                        }
                    }

                }
                if (job == "printer") {
                    if (jsonData_printer_status) {

                        if (cmd == "Bactual") {
                            JSONObject temp = new JSONObject(jsonData_printer_printer_temps.getString("bed"));
                            String string1 = temp.getString("actual");
                            if (string1.equals("null") || string1.equals("")) {
                                returnData = "0";
                            } else {
                                returnData = string1;
                            }
                        }
                        if (cmd == "Btarget") {

                            JSONObject temp = new JSONObject(jsonData_printer_printer_temps.getString("bed"));
                            String string1 = temp.getString("target");
                            if (string1.equals("null") || string1.equals("")) {
                                returnData = "0";
                            } else {
                                returnData = string1;
                            }
                        }
                        if (cmd == "actual") {
                            JSONObject temp = new JSONObject(jsonData_printer_printer_temps.getString("tool0"));
                            String string1 = temp.getString("actual");
                            if (string1.equals("null") || string1.equals("")) {
                                returnData = "0";
                            } else {
                                returnData = string1;
                            }
                        }
                        if (cmd == "target") {
                            JSONObject temp = new JSONObject(jsonData_printer_printer_temps.getString("tool0"));
                            String string1 = temp.getString("target");
                            if (string1.equals("null") || string1.equals("")) {
                                returnData = "0";
                            } else {
                                returnData = string1;
                            }
                        }
                    }else{
                        returnData = "0";
                    }
                    }
                    if (job == "connetion") {
                    }
                    if (job == "files") {
                    }
                    if (job == "version") {
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    Log.e("OctoDroid", "Exception");
                }

        }

        if (returnData.equals("null") || returnData.equals("")){
            returnData = "0";
        }
        if (returnData.length() > 15){
            if (cmd == "state"){
                returnData = returnData.substring(0, 15);
                returnData = returnData + "...";
            }
        }
        if (returnData.length() > 20){
            returnData = returnData.substring(0, 20);
            returnData = returnData + "...";
        }
        return returnData;
}
    public static int getProgress(){
        int returnData = 0;
        double acom = 0;
        if (Activity.server_status) {
            JSONObject json = null;
            try {
            json = new JSONObject(jsonData_job);
            JSONObject printTime_json = new JSONObject(json.getString("progress"));
                String comp = printTime_json.getString("completion");
                if (comp.equals("") || comp.equals("null")) {
                    acom = 0;
                }else{
                    try {
                        acom = Double.parseDouble(comp);
                    }catch (NumberFormatException e){
                        acom = 0;
                    }
                }
               if (acom < 1){
                    returnData = 0;
                }else{
                   returnData = (int) acom;
               }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("OctoDroid","printStackTrace");
            }

        }
        return returnData;
    }

    public static void refreshJson(String ip, String api, String key){
        boolean nono = false;
        if (ip == null || ip.equals("")){

        }else {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet;
            if (ip.startsWith("http://")){
                httpGet = new HttpGet(ip + "/api/"+api + "?apikey=" + key);
            }else{
                httpGet = new HttpGet("http://"+ ip + "/api/"+api + "?apikey=" + key);
            }
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (ClientProtocolException e) {
                Log.e("OctoDroid", "ClientProtocolException");
                Log.d("OctoDroid", ip);
                nono = true;
                Activity.server_status = false;
                return;
            } catch (IOException e) {
                Log.e("OctoDroid", "IOException");
                Log.d("OctoDroid", ip);
                nono = true;
                Activity.server_status = false;
                return;
            } catch (IllegalStateException e){
                Log.e("OctoDroid", "IllegalStateException");
                Log.d("OctoDroid", ip);
                nono = true;
                Activity.server_status = false;
                return;
            }
            if (api.equals("job")){
                 jsonData_job = builder.toString();
            }
            if (api.equals("printer")){
                jsonData_printer = builder.toString();
            }
            if (!nono) {
                Activity.server_status = true;
            }
        }
    }
    public static void sendcmd(String ip ,String api, String cmd, List<NameValuePair> value){
        Log.e("OctoDroid", api + " " + ip + " " + key + " " + cmd + "" + value);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost;
        if (ip.startsWith("http://")){
            httpPost = new HttpPost(ip + "/api/"+api);
        }else{
            httpPost = new HttpPost("http://"+ ip + "/api/"+api);
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(value));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 204) {
                Log.e("OctoDroid", "204");
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                Log.e("OctoDroid", "205");
            }else{            Log.e("OctoDroid", statusCode + "");}
        } catch (ClientProtocolException e) {
            Log.e("OctoDroid", "ClientProtocolException");
        } catch (IOException e) {
            Log.e("OctoDroid", "IOException");
        } catch (IllegalStateException e){
            Log.e("OctoDroid", "IllegalStateException");
        }

    }
    protected static void sendError(String er){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet;
        httpGet = new HttpGet("http://mariogrip.com/OctoPrint/api/error.php?e=" + er);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
            }
        } catch (ClientProtocolException e) {
            Log.e("OctoDroid", "ClientProtocolException");
        } catch (IOException e) {
            Log.e("OctoDroid", "IOException");
        } catch (IllegalStateException e){
            Log.e("OctoDroid", "IllegalStateException");
        }

    }

    protected static void goX(String value){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("apikey", key));
        nameValuePairs.add(new BasicNameValuePair("command", "jog"));
        nameValuePairs.add(new BasicNameValuePair("x", value));
    }
    protected static void goZ(String value){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("apikey", key));
        nameValuePairs.add(new BasicNameValuePair("command", "jog"));
        nameValuePairs.add(new BasicNameValuePair("z", value));
    }
    protected static void goY(String value){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("apikey", key));
        nameValuePairs.add(new BasicNameValuePair("command", "jog"));
        nameValuePairs.add(new BasicNameValuePair("z", value));
    }
    protected static void goHome(){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("apikey", key));
        nameValuePairs.add(new BasicNameValuePair("command", "jog"));
        nameValuePairs.add(new BasicNameValuePair("home", ""));
    }
    protected static void goHomeY(){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("apikey", key));
        nameValuePairs.add(new BasicNameValuePair("command", "jog"));
        nameValuePairs.add(new BasicNameValuePair("home", ""));
    }
    protected static void goHomeZ(){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("apikey", key));
        nameValuePairs.add(new BasicNameValuePair("command", "jog"));
        nameValuePairs.add(new BasicNameValuePair("home", ""));
    }
    protected static void goHomeX(){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("apikey", key));
        nameValuePairs.add(new BasicNameValuePair("command", "jog"));
        nameValuePairs.add(new BasicNameValuePair("home", ""));
    }
}
