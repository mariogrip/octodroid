package com.mariogrip.octodroid;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mariogrip on 04.02.15.
 */
public class util_decode extends util {

    /**
     * This is the file where all the json is decoded to smaller json blocks and then to java strings where we can use to display information
     * <p/>
     * Functions:
     * decodeConnections_V1() || return String
     * decodeFiles_V1()       || return String
     * ""WIP""
     * <p/>
     * <p/>
     * Functions used form other classes:
     * getResponse(IP, API URL, API KEY) || return String
     */

    //Common API call:
    //TODO ADD DIFFERENT VERSION OF API
    public static void decodeVersions() {
        try {
            decodeVersions_V1();
        } catch (Exception e) {
        }
    }

    public static void decodeConnections() {
        try {
            decodeConnections_V1();
        } catch (Exception e) {
        }
    }

    public static void decodeFiles() {
        try {
            decodeFiles_V1();
        } catch (Exception e) {
        }
    }

    public static void decodeJob() {
        try {
            decodeJob_V1();
        } catch (Exception e) {
        }
    }

    public static void decodePrinter() {
        try {
            decodePrinter_V1();
        } catch (Exception e) {
        }
    }

    //Api version V0.1:
    public static void decodeConnections_V1() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "connection", mainActivity.key));
            memory.options_dec = connection_get.getString("options");
            memory.current_dec = connection_get.getString("current");

            memory.connection.current.state = new JSONObject(memory.current_dec).getString("state");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }

    public static void decodeFiles_V1() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }

    public static void decodeVersions_V1() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }

    public static void decodeJsonService() {
        if (!memory.isServerUp()) {
            return;
        }
        try {
            JSONObject obj1 = new JSONObject();
            obj1 = new JSONObject(getResponse(mainActivity.ip, "job", mainActivity.key));
            memory.progress_dec = new JSONObject(obj1.getString("progress"));
            memory.job.progress.completion = Float.parseFloat(memory.progress_dec.getString("completion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void decodeJob_V1() {
        JSONObject obj1 = new JSONObject();
        try {
            obj1 = new JSONObject(getResponse(mainActivity.ip, "job", mainActivity.key));

            try {
                memory.progress_dec = new JSONObject(obj1.getString("progress"));

                try {

                    memory.job.progress.completion = Float.parseFloat(memory.progress_dec.getString("completion"));
                } catch (Exception e) {
                    util.logD("1=" + e.toString());

                }
                try {
                    memory.job.progress.filepos = Integer.parseInt(memory.progress_dec.getString("filepos"));
                } catch (Exception e) {
                    util.logD("2=" + e.toString());

                }
                try {
                    memory.job.progress.printTime = Integer.parseInt(memory.progress_dec.getString("printTime"));
                } catch (Exception e) {
                    util.logD("3=" + e.toString());

                }
                try {
                    memory.job.progress.PrintTimeLeft = Integer.parseInt(memory.progress_dec.getString("printTimeLeft"));

                } catch (Exception e) {
                    util.logD("4=" + e.toString());

                }

            } catch (Exception e) {
                util.logD("5=" + e.toString());

            }
            try {
                memory.job_dec = new JSONObject(obj1.getString("job"));
                try {

                    memory.job.estimatedPrintTime = Integer.parseInt(memory.job_dec.getString("estimatedPrintTime"));
                } catch (Exception e) {
                    util.logD("6=" + e.toString());

                }

                try {
                    memory.file_dec = new JSONObject(memory.job_dec.getString("file"));

                    try {
                        memory.job.file.name = memory.file_dec.getString("name");
                    } catch (Exception e) {
                        util.logD("7=" + e.toString());

                    }
                    try {
                        memory.job.file.origin = memory.file_dec.getString("origin");
                    } catch (Exception e) {
                        util.logD("8=" + e.toString());

                    }
                    try {
                        memory.job.file.size = Integer.parseInt(memory.file_dec.getString("size"));
                    } catch (Exception e) {
                        util.logD("9=" + e.toString());

                    }
                    try {
                        memory.job.file.date = Float.parseFloat(memory.file_dec.getString("date"));
                    } catch (Exception e) {
                        util.logD("10=" + e.toString());

                    }

                } catch (Exception e) {
                    util.logD("11=" + e.toString());

                }

                try {
                    memory.fillament_dec = new JSONObject(memory.job_dec.getString("filament"));

                    try {

                        memory.job.filament.lenght = Integer.parseInt(memory.fillament_dec.getString("length"));
                    } catch (Exception e) {
                        util.logD("12=" + e.toString());

                    }
                    try {
                        memory.job.filament.volume = Float.parseFloat(memory.fillament_dec.getString("volume"));
                    } catch (Exception e) {
                        util.logD("13=" + e.toString());

                    }

                } catch (Exception e) {
                    util.logD("14=" + e.toString());

                }

            } catch (Exception e) {
                util.logD("15=" + e.toString());

            }


        } catch (Exception e) {
            util.logD("16=" + e.toString());

        }

    }

    //gets printer temps and adds them to memory instance
    public static void decodePrinter_V1() {
        try {
            JSONObject obj1 = new JSONObject(getResponse(mainActivity.ip, "printer", mainActivity.key));
            try {
                jsonData_printer_printer_temps = new JSONObject(obj1.getString("temps"));
            } catch (Exception e) {
                jsonData_printer_printer_temps = new JSONObject(obj1.getString("temperature"));
            }
            try {
                JSONObject tempBed = new JSONObject(jsonData_printer_printer_temps.getString("bed"));
                try {
                    memory.temp.current.Bed[0] = Float.parseFloat(tempBed.getString("actual"));
                } catch (Exception e) {
                    util.logD(e.toString());
                }
                try {
                    memory.temp.target.Bed[0] = Float.parseFloat(tempBed.getString("target"));
                } catch (Exception e) {
                    util.logD(e.toString());
                }
            } catch (Exception e) {
                util.logD(e.toString());
            }
            try {
                JSONObject tempExt = new JSONObject(jsonData_printer_printer_temps.getString("tool0"));
                try {
                    memory.temp.current.Ext[0] = Float.parseFloat(tempExt.getString("actual"));
                } catch (Exception e) {
                    util.logD(e.toString());
                }
                try {
                    memory.temp.target.Ext[0] = Float.parseFloat(tempExt.getString("target"));
                } catch (Exception e) {
                    util.logD(e.toString());
                }
            } catch (Exception e) {
                util.logD(e.toString());
            }

        } catch (Exception e) {
            util.logD(e.toString());
        }


    }

    public static void decodeTempHistory() {
        //obj2 = history object
        //http://192.168.1.146/api/printer?history=true&apikey=FE31E3C049E14371916C2B24BC85117B


        try {
            JSONObject response = new JSONObject(getResponse(mainActivity.ip, "printer?history=true&", mainActivity.key));
            try {
                response = response.getJSONObject("temperature");
            }catch (Exception e){
                response = response.getJSONObject("temps");
            }

            JSONArray History = response.getJSONArray("history");

            HistoryObject temperatureEntry;
            JSONObject tempEntryJSON, bed, tool;
            ArrayList<HistoryObject> HistoryArray = new ArrayList<>();
            for (int index = 0; index < History.length(); index++) {
                 temperatureEntry = new HistoryObject();
                 tempEntryJSON = History.getJSONObject(index);

                try {
                    temperatureEntry.setBedActTemp(Float.parseFloat(new JSONObject(tempEntryJSON.getString("tool0")).getString("target")));
                }catch (Exception e){
                    e.printStackTrace();
                }
                temperatureEntry.setBedTargetTemp(Float.parseFloat(new JSONObject(tempEntryJSON.getString("tool0")).getString("actual")));
                temperatureEntry.setHotEndActTemp(Float.parseFloat(new JSONObject(tempEntryJSON.getString("bed")).getString("actual")));
                temperatureEntry.setHotEndTargetTemp(Float.parseFloat(new JSONObject(tempEntryJSON.getString("bed")).getString("target")));
                Log.i("From Json", new JSONObject(tempEntryJSON.getString("bed")).getString("target").toString());
                temperatureEntry.setTime(tempEntryJSON.getInt("time"));

                HistoryArray.add(index, temperatureEntry);
            }
        memory.temp.history.HistoryArray = HistoryArray;
        } catch (JSONException e) {
           e.printStackTrace();
        }


    }


    //Api version v0.2?? (DEVEL)
    public static void decodeConnections_V2() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "connection", mainActivity.key));
            memory.options_dec = connection_get.getString("options");
            memory.current_dec = connection_get.getString("current");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }

    public static void decodeFiles_V2() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }

    public static void decodeVersions_V2() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }

    public static void decodeJob_V2() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }

    public static void decodePrinter_V2() {
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
            util.logD(e.toString());
        }
    }


}
