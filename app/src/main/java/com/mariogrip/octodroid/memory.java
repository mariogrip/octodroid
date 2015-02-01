package com.mariogrip.octodroid;

import org.json.JSONObject;

/**
 * Created by mariogrip on 01.12.14.
 */
public class memory extends util {
    protected static String bedTempCurrent = "0";
    public static String bedTempTarget = "0";
    protected static String ExtTempCurrent = "0";
    public static String ExtTempTarget = "0";
    protected static String MacineState = "";
    protected static String File = "";
    protected static String Filament = "";
    protected static String EstimatedPrintTime = "0";
    protected static String Timelapse = "";
    protected static String Height = "";
    protected static String Printed = "";
    protected static String PrintTime = "0";
    protected static String PrintTimeLeft = "0";
    protected static String FilePos = "";
    protected static String Completion = "";
    protected static String Size = "0";
    protected static Integer ProgressM = 0;


    //

    public static boolean connected = false;
    protected static String options_dec;
    protected static String current_dec;
    protected static String files_dec;
}
