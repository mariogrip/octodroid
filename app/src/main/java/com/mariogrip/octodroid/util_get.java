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

    public static void decodeConnections(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "connection", mainActivity.key));
            memory.options_dec = connection_get.getString("options");
            memory.current_dec = connection_get.getString("current");
        } catch (Exception e) {
        }
    }
    public static void decodeFiles(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }
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
            if (memory.MacineState.contains("Operational") || memory.MacineState.contains("Printing") || memory.MacineState.contains("Paused")){
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

    public static void genData() {
        if (!mainActivity.server_status) {
            memory.bedTempCurrent = "0";
            memory.bedTempTarget = "0";
            memory.ExtTempCurrent = "0";
            memory.ExtTempTarget = "0";
            memory.MacineState = "-";
            memory.File = "-";
            memory.Filament = "-";
            memory.EstimatedPrintTime = "-";
            memory.Timelapse = "-";
            memory.Height = "-";
            memory.Printed = "-";
            memory.PrintTime = "-";
            memory.PrintTimeLeft = "-";
            memory.FilePos = "-";
            memory.Completion = "-";
            memory.Size = "-";
            memory.ProgressM = 0;
        }
        boolean noerr;
        if (mainActivity.server_status) {
            try {
                if (jsonData_printer_status) {
                    JSONObject tempBed = new JSONObject(jsonData_printer_printer_temps.getString("bed"));
                    JSONObject tempExt = new JSONObject(jsonData_printer_printer_temps.getString("tool0"));
                    memory.bedTempCurrent = tempBed.getString("actual");
                    memory.bedTempTarget = tempBed.getString("target");
                    memory.ExtTempCurrent = tempExt.getString("actual");
                    memory.ExtTempTarget = tempExt.getString("target");
                }else{
                    memory.bedTempCurrent = "0";
                    memory.bedTempTarget = "0";
                    memory.ExtTempCurrent = "0";
                    memory.ExtTempTarget = "0";
                }


                memory.ProgressM = util.getProgress();
                memory.MacineState = jsonData_job_job.getString("state");
                memory.File = jsonData_job_job_file.getString("name");
                memory.FilePos = jsonData_job_job_progress.getString("filepos");
                memory.Filament = "";
                memory.Size = jsonData_job_job_file.getString("size");
                memory.EstimatedPrintTime = jsonData_job_job_job.getString("estimatedPrintTime");
                memory.Timelapse = "";
                memory.Height = "";
                memory.Printed = "";
                memory.PrintTime = jsonData_job_job_progress.getString("printTime").toString();
                memory.PrintTimeLeft = jsonData_job_job_progress.getString("printTimeLeft");
                memory.Completion = jsonData_job_job_progress.getString("completion");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("OctoDroid", "Exception");
            }

        }
        if (memory.bedTempCurrent.equals("null") || memory.bedTempCurrent.equals("")) {
            memory.bedTempCurrent = "0";
        }
        if (memory.bedTempTarget.equals("null") || memory.bedTempTarget.equals("")) {
            memory.bedTempTarget = "0";
        }
        if (memory.ExtTempCurrent.equals("null") || memory.ExtTempCurrent.equals("")) {
            memory.ExtTempCurrent = "0";
        }
        if (memory.ExtTempTarget.equals("null") || memory.ExtTempTarget.equals("")) {
            memory.ExtTempTarget = "0";
        }
        if (memory.MacineState.equals("null") || memory.MacineState.equals("")) {
            memory.MacineState = "-";
        }
        if (memory.File.equals("null") || memory.File.equals("")) {
            memory.File = "-";
        }
        if (memory.Filament.equals("null") || memory.Filament.equals("")) {
            memory.Filament = "-";
        }
        if (memory.EstimatedPrintTime.equals("null") || memory.EstimatedPrintTime.equals("")) {
            memory.EstimatedPrintTime = "0";
        }
        if (memory.Timelapse.equals("null") || memory.Timelapse.equals("")) {
            memory.Timelapse = "-";
        }
        if (memory.Height.equals("null") || memory.Height.equals("")) {
            memory.Height = "0";
        }
        if (memory.Printed.equals("null") || memory.Printed.equals("")) {
            memory.Printed = "0";
        }
        if (memory.PrintTime.equals("null") || memory.PrintTime.equals("")) {
            memory.PrintTime = "0";
        }
        if (memory.PrintTimeLeft.equals("null") || memory.PrintTimeLeft.equals("")) {
            memory.PrintTimeLeft = "0";
        }
        if (memory.FilePos.equals("null") || memory.FilePos.equals("")) {
            memory.FilePos = "0";
        }
        if (memory.Completion.equals("null") || memory.Completion.equals("")) {
            memory.Completion = "0";
        }
        if (memory.Size.equals("null") || memory.Size.equals("")) {
            memory.Size = "0";
        }
        if (memory.ProgressM.equals("null") || memory.ProgressM.equals("")){
            memory.ProgressM = 0;
        }

    //    if (memory.MacineState.length() > 25) {
     //       memory.MacineState = memory.MacineState.substring(0, 25);
      //      memory.MacineState = memory.MacineState + "...";
      //  }
    }

}