package com.mariogrip.octodroid;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mariogrip on 01.12.14.
 *
 * This is the file where the information is stored
 */
public class memory extends mainActivity {
    public static class temp {
        public static class current {
            protected static float[] Bed = {0};
            protected static float[] Ext = {0};

            public static float[] getBed() {
                return Bed;
            }

            public static float[] getExt() {
                return Ext;
            }
        }

        public static class target {
            protected static float[] Bed = {0};
            protected static float[] Ext = {0};

            public static float[] getBed() {
                return Bed;
            }

            public static float[] getExt() {
                return Ext;
            }
        }
    }
    public static class job {
        public static class file{
            protected static String name = "";
            protected static String origin = "";
            protected static float size = 0;
            protected static float date = 0;

            public static String getName() {
                return name;
            }

            public static float getSize() {
                return size;
            }

            public static String getOrigin() {
                return origin;
            }

            public static float getDate() {
                return date;
            }
        }
        public static class progress {
            protected static float completion = 0;
            protected static int filepos = 0;
            protected static int printTime = 0;
            protected static int PrintTimeLeft = 0;

            public static float getCompletion() {
                return completion;
            }

            public static int getPrintTimeLeft() {
                return PrintTimeLeft;
            }

            public static int getFilepos() {
                return filepos;
            }

            public static int getPrintTime() {
                return printTime;
            }
        }
        public static class filament {
            protected static int lenght = 0;
            protected static float volume = 0;

            public static int getLenght() {
                return lenght;
            }

            public static float getVolume() {
                return volume;
            }
        }
        protected static int estimatedPrintTime = 0;

        public static int getEstimatedPrintTime() {
            return estimatedPrintTime;
        }
    }
    public static class connection {
        public static class current {
            protected static String state = "";
            protected static String port = "";
            protected static String baudrate = "";

            public static String getState() {
                return state;
            }

            public static String getBaudrate() {
                return baudrate;
            }

            public static String getPort() {
                return port;
            }
        }
        public static class options {
            protected static String[] ports = {""};
            protected static String[] baudrates = {""};
            protected static String portPreference = "";
            protected static String baudratePreference = "";
            protected static String autoconnect = "";

            public static String[] getPorts() {
                return ports;
            }

            public static String[] getBaudrates() {
                return baudrates;
            }

            public static String getPortPreference() {
                return portPreference;
            }

            public static String getBaudratePreference() {
                return baudratePreference;
            }

            public static String getAutoconnect() {
                return autoconnect;
            }
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }

    //Booleans
    public static boolean isConnected = false;
    public static boolean ServerUp = false;
    public static boolean skipWelcom = false;

    public static boolean isServerUp() {
        return ServerUp;
    }

    public static void setServerUp(boolean ServerUp) {
        memory.ServerUp = ServerUp;
    }

    //User Settings
    public static class user{
        private static String ip = "none";
        private static String api = "none";
        private static boolean useBasicAuth;
        private static String userName;
        private static String password;

        public static String getIp() {
            return ip;
        }

        public static void setIp(String ip) {
            user.ip = ip;
        }

        public static String getApi() {
            return api;
        }

        public static void setApi(String api) {
            user.api = api;
        }


        public static boolean getUseBasicAuth() {
            return useBasicAuth;
        }

        public static void setUseBasicAuth(boolean useBasicAuth) {
            user.useBasicAuth = useBasicAuth;
        }

        public static String getUserName() {
            return userName;
        }

        public static void setUserName(String userName) {
            user.userName = userName;
        }

        public static String getPassword() {
            return password;
        }

        public static void setPassword(String password) {
            user.password = password;
        }
    }

    //Single Json strings
    protected static String options_dec;
    protected static String current_dec;
    protected static String files_dec;
    protected static JSONObject file_dec;
    protected static JSONObject job_dec;
    protected static JSONObject progress_dec;
    protected static JSONObject fillament_dec;
}
