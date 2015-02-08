package com.mariogrip.octodroid;

import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by mariogrip on 27.10.14.
 *
 * GNU Affero General Public License http://www.gnu.org/licenses/agpl.html
 */
public abstract class util extends mainActivity {

    /**
     * This is the file where utils and tools are placed
     *
     * Functions:
     *  ""WIP""
     *
     */
    public static String arrayToJsonArray(String[] i){
        StringBuilder ReturnThis = new StringBuilder();
        ReturnThis.append("[");
        for (String g : i){
            ReturnThis.append("\"" + g + "\",");
        }
        if (ReturnThis.toString().endsWith(",")){
            ReturnThis =  new StringBuilder(ReturnThis.substring(0,ReturnThis.length()-1));
        }
        ReturnThis.append("]");
        return ReturnThis.toString();
    }
    public static String[] oneLinetoArray(String i){
        String[] ReturnThis;
        if (i.contains("\n")){
            ReturnThis = i.split("\n");
        }else{
            ReturnThis = new String[]{i};
        }
        return ReturnThis;
    }

    public static String[] getSavedCommandsIDs(SharedPreferences prefs){
        String FromPrefs = prefs.getString("cmdID", "none");
        String[] ReturnThis;
        if (FromPrefs.contains("|")){
            ReturnThis = FromPrefs.split("\\|");
        }else{
            ReturnThis = new String[]{FromPrefs};
        }
        for (String i : ReturnThis) {
            logD("On GetSave "+i);
        }
        return ReturnThis;
    }

    public static boolean SaveCommands(String name, String command, SharedPreferences sharedPref){
        try {
            SharedPreferences.Editor editor = sharedPref.edit();
            String FromPrefs = sharedPref.getString("cmdID", "none");
            String randomString = getRandom();
            while (FromPrefs.contains(randomString)) {
                randomString = getRandom();
                util.logD("getting new string");
            }
            if (FromPrefs.contains("none")) {
                editor.putString("cmdID", randomString);
                editor.commit();
                editor.putString(randomString, name + "|" + command + "|" + randomString);
                editor.commit();
                return true;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(FromPrefs).append("|").append(randomString);
            editor.putString("cmdID", sb.toString());
            editor.commit();
            editor.putString(randomString, name + "|" + command + "|" + randomString);
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean RemoveCommands(String id, SharedPreferences sharedPref){
        try {
            SharedPreferences.Editor editor = sharedPref.edit();
            String FromPrefs = sharedPref.getString("cmdID", "none");

            String theString = FromPrefs.replace(id,"");
            while (theString.contains("||")){
                theString = theString.replace("||", "|");
            }
            while (theString.startsWith("|")){
                theString = theString.substring(1);
            }
            while (theString.endsWith("|")){
                theString = theString.substring(0,theString.length()-1);
            }
            if (theString.equals("")){
                theString = "none";
            }

            util.logD("Remove "+theString);

            if (FromPrefs.contains("none")) {
                editor.putString("cmdID", theString);
                editor.commit();
                editor.putString(id, "");
                editor.commit();
                return true;
            }
            editor.putString("cmdID", theString);
            editor.commit();
            editor.putString(id, "");
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String[] getCommandInfo(String id, SharedPreferences prefs){
        String FromPrefs = prefs.getString(id, "none");
        String[] ReturnThis;
        if (!FromPrefs.contains("none")){
            ReturnThis = FromPrefs.split("\\|");
        }else{
            ReturnThis = new String[]{FromPrefs};
        }

        for (String i : ReturnThis){
            util.logD("On GetINFo "+i);
        }
        return ReturnThis;
    }

    public static String getRandom(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (new Random().nextFloat() * (rightLimit - leftLimit));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    public static String[] jsonArraytoStringArray(String input){
        String input2 = input.replaceAll("[\\[\\]\" ]","");
        String[] array = input2.split(",", -1);
        return array;
    }


    //Converts Bytes to Mega/Giga Bytes
    public static String toMBGB(double bytes){
        try {
            //Checks if the app has contact with the server.
            if (!mainActivity.server_status) {
                return "-/-";
            }
            String returnData;
            Double fileSizeInKB = bytes / 1024;
            Double fileSizeInMB = fileSizeInKB / 1024;
            Double fileSizeInKBformat = roundDown5(fileSizeInKB);
            Double fileSizeInMBformat = roundDown5(fileSizeInMB);
            if (fileSizeInMB >= 1) {
                returnData = fileSizeInMBformat.toString() + "MB";
            } else {
                returnData = fileSizeInKBformat.toString() + "KB";
            }
            return returnData;
        }catch (Exception e){
            return "0MB";
        }
    }
    public static double roundDown5(double d) {
        return (long) (d * 1e2) / 1e2;
    }
    public static String toHumanRead(double biggy)
    {
        try {
            if (!mainActivity.server_status) {
                return "00:00:00";
            }
            int hours = (int) biggy / 3600;
            int remainder = (int) biggy - hours * 3600;
            int mins = remainder / 60;
            remainder = remainder - mins * 60;
            int secs = remainder;

            return String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
        }catch (Exception e){
            return "00:00:00";
        }
    }
    protected static JSONObject jsonData_job_job;
    protected static JSONObject jsonData_job_job_progress;
    protected static JSONObject jsonData_job_job_file;
    protected static JSONObject jsonData_job_job_job;
    protected static JSONObject jsonData_printer_printer;
    protected static JSONObject jsonData_printer_printer_temps;
    protected static boolean jsonData_printer_status;

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }



    public static void decodeJsonService(){
        if (!mainActivity.server_status){
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
        if (!mainActivity.server_status){
            return "-";
        }
        String returnData = new String();
        boolean noerr;
        if (mainActivity.server_status) {
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
                        if (cmd == "options"){

                        }
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
            JSONObject json = null;
            try {
                float acom = memory.job.progress.completion;
               if (acom < 1){
                    returnData = 0;
                }else{
                   returnData = (int) acom;
               }

            } catch (Exception e) {
                e.printStackTrace();
                returnData = 0;
                Log.e("OctoDroid","printStackTrace");
            }
        return returnData;
    }

    public static void refreshJson(String ip, String api, String key){
        try {
            boolean nono = false;
            if (ip == null || ip.equals("")) {

            } else {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet;
                if (ip.startsWith("http://")) {
                    httpGet = new HttpGet(ip + "/api/" + api + "?apikey=" + key);
                } else {
                    httpGet = new HttpGet("http://" + ip + "/api/" + api + "?apikey=" + key);
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
                    mainActivity.server_status = false;
                    return;
                } catch (IOException e) {
                    Log.e("OctoDroid", "IOException");
                    Log.d("OctoDroid", ip);
                    nono = true;
                    mainActivity.server_status = false;
                    return;
                } catch (IllegalStateException e) {
                    Log.e("OctoDroid", "IllegalStateException");
                    Log.d("OctoDroid", ip);
                    nono = true;
                    mainActivity.server_status = false;
                    return;
                }
                if (api.equals("job")) {
                    jsonData_job = builder.toString();
                }
                if (api.equals("printer")) {
                    jsonData_printer = builder.toString();
                }
                if (!nono) {
                    mainActivity.server_status = true;
                }
            }
        }catch (Exception e){

        }
    }
    public static void sendcmd(String ip ,String api, String cmd, String value){
        Log.e("OctoDroid", api + " " + ip + " " + key + " " + cmd + "" + value);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost;
        if (ip.startsWith("http://")){
            httpPost = new HttpPost(ip + "/api/"+cmd);
        }else{
            httpPost = new HttpPost("http://"+ ip + "/api/"+cmd);
        }
        try {
            httpPost.addHeader("X-Api-Key", api);
            httpPost.addHeader("content-type", "application/json");
            httpPost.setEntity(new StringEntity(value));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 204) {
                Log.e("OctoDroid", "204");
                HttpEntity entity = response.getEntity();
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

    public static String getResponse(String ip, String api, String key){
        boolean nono = false;
        String retu = "";
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
                httpGet.addHeader("X-Api-Key", key);
                httpGet.addHeader("content-type", "application/json");
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
                mainActivity.server_status = false;
                return "";
            } catch (IOException e) {
                Log.e("OctoDroid", "IOException");
                Log.d("OctoDroid", ip);
                nono = true;
                mainActivity.server_status = false;
                return "";
            } catch (IllegalStateException e){
                Log.e("OctoDroid", "IllegalStateException");
                Log.d("OctoDroid", ip);
                nono = true;
                mainActivity.server_status = false;
                return "";
            }
            retu= builder.toString();

            if (!nono) {
                mainActivity.server_status = true;
            }
        }
        return retu;
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
    public static void logD(String e){
        Log.d("OctoDroid",e);
    }

    public static boolean doGeneralCheckIp(){

        if (memory.user.getIp().length() < 1){
            return false;
        }
        return true;
    }
    public static boolean doGeneralCheckApi(){
        if (memory.user.getApi().length() < 1){
            return false;
        }
        return true;
    }
    public static boolean doGeneralCheckIp(String ip){
        if (ip.length() < 1){
            return false;
        }
        return true;
    }
    public static boolean doGeneralCheckApi(String key){
        if (key.length() < 1){
            return false;
        }
        return true;
    }
    public static int isPingServer(){
        try {
            InetAddress address = InetAddress.getByName(memory.user.getIp()+"/api/version");
            address.isReachable(3000);
            return 0;
        }
        catch (UnknownHostException e) {
            System.err.println("Cannot find the server");
            return 1;
        }
        catch (IOException e) {
            logD("Cannot reach server");
            return 2;
        }
    }
    public static int isPingServer(String ip){
        try {
            InetAddress address = InetAddress.getByName(ip+"/api/version");
            address.isReachable(3000);
            return 0;
        }
        catch (UnknownHostException e) {
            System.err.println("Cannot find the server");
            return 1;
        }
        catch (IOException e) {
            logD("Cannot reach server");
            return 2;
        }
    }

    public static boolean isPingServerTrue(){
        try {
            InetAddress address = InetAddress.getByName(ip+"/api/version");
            address.isReachable(3000);
            return true;
        }
        catch (UnknownHostException e) {
            System.err.println("Cannot find the server");
            return false;
        }
        catch (IOException e) {
            logD("Cannot reach server");
            return false;
        }
    }
    public static String checkIPWithServer(String ip, String api){
        try {
            JSONObject connection_get = new JSONObject(getResponse(ip, "connection", api));
            memory.connection.current.state = connection_get.getJSONObject("current").getString("state");
            logD(memory.connection.current.state);
            return memory.connection.current.state;
        } catch (Exception e) {
            util.logD(e.toString());
            return "null";
        }
    }
    public static boolean checkAPIWithServer(String ip, String api){
            try {
                String sendvalue = "{\n" +
                        "  \"command\": \"home\",\n" +
                        "  \"axes\": [\"x\", \"y\"]\n" +
                        "}";
                sendcmd(ip, api, "printer/printhead", sendvalue);
                return true;
            }catch (Exception e){
                logD("Failed checkAPI");
                return false;
            }
    }

}
