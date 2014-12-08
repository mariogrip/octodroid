package com.mariogrip.octodroid;

import android.util.Log;


import org.json.JSONObject;

/**
 * Created by mariogrip on 02.12.14.
 */
public class util_get extends util {
    protected void getSerialPort() {

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


                JSONObject tempBed = new JSONObject(jsonData_printer_printer_temps.getString("bed"));
                JSONObject tempExt = new JSONObject(jsonData_printer_printer_temps.getString("tool0"));

                memory.bedTempCurrent = tempBed.getString("actual");
                memory.ProgressM = util.getProgress();
                memory.bedTempTarget = tempBed.getString("target");
                memory.ExtTempCurrent = tempExt.getString("actual");
                memory.ExtTempTarget = tempExt.getString("target");
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
            memory.MacineState = "0";
        }
        if (memory.File.equals("null") || memory.File.equals("")) {
            memory.File = "0";
        }
        if (memory.Filament.equals("null") || memory.Filament.equals("")) {
            memory.Filament = "0";
        }
        if (memory.EstimatedPrintTime.equals("null") || memory.EstimatedPrintTime.equals("")) {
            memory.EstimatedPrintTime = "0";
        }
        if (memory.Timelapse.equals("null") || memory.Timelapse.equals("")) {
            memory.Timelapse = "0";
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

        if (memory.MacineState.length() > 25) {
            memory.MacineState = memory.MacineState.substring(0, 25);
            memory.MacineState = memory.MacineState + "...";
        }
    }
}