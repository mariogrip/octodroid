package com.mariogrip.octodroid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mariogrip on 02.12.14.
 *
 *  GNU Affero General Public License http://www.gnu.org/licenses/agpl.html
 */
public class util_get extends util {

    /**
     * This file is used to get information out of json blocks that was processed at Util_decode
     *
     * Functions:
     *  ""WIP""
     */

    public static String getSerialPort() {
        try {
            return new JSONObject(memory.options_dec).getString("ports").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getBaudrates() {
        try {
            return new JSONObject(memory.options_dec).getString("baudrates").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getCurrentSerialPort() {
        try {
            return new JSONObject(memory.current_dec).getString("port").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getCurrentBaudrates() {
        try {
            return new JSONObject(memory.current_dec).getString("baudrate").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static boolean isConnected() {
        Boolean ReturnValue = false;
        try {
            if (memory.connection.current.getState().contains("Operational") || memory.connection.current.getState().contains("Printing") || memory.connection.current.getState().contains("Paused")){
                ReturnValue = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    return ReturnValue;
    }

    public static List<String[]> getFiles() {
        List<String[]> reString = new ArrayList<String[]>(){};
        try {
            ArrayList<String> listdata = new ArrayList<String>();
            JSONArray jArray = new JSONArray(memory.files_dec);
            for (int i=0;i<jArray.length();i++){
                Log.d("octo", jArray.get(i).toString());
                String[] stringtoadd = {new JSONObject(jArray.get(i).toString()).getString("name"), new JSONObject(jArray.get(i).toString()).getString("origin")};
                reString.add(stringtoadd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reString;
    }

}