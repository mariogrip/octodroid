package com.mariogrip.octodroid;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mariogrip on 01.12.14.
 *
 * This is the file where the information is stored
 */
public class memory extends mainActivity {
    static class temp {
        static class current {
            protected static float[] Bed = {0};
            protected static float[] Ext = {0};

        }

        static class target {
            public static float[] Bed = {0};
            public static float[] Ext = {0};
        }
    }
    static class job {
        static class file{
            protected static String name = "";
            protected static String origin = "";
            protected static float size = 0;
            protected static float date = 0;
        }
        static class progress {
            protected static float completion = 0;
            protected static int filepos = 0;
            protected static int printTime = 0;
            protected static int PrintTimeLeft = 0;
        }
        static class filament {
            protected static int lenght = 0;
            protected static float volume = 0;
        }
        protected static int estimatedPrintTime = 0;

    }

    //Booleans
    public static boolean connected = false;




    //TODO remove all single string variables
    //This is temporary OLD NAMES!
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

    //Single Json strings
    protected static String options_dec;
    protected static String current_dec;
    protected static String files_dec;
}
