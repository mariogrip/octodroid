package com.mariogrip.octodroid;

import org.json.JSONObject;

/**
 * Created by mariogrip on 04.02.15.
 */
public class util_decode extends util {

    /**
     * This is the file where all the json is decoded to smaller json blocks and then to java strings where we can use to display information
     *
     * Functions:
     * decodeConnections_V1() || return String
     * decodeFiles_V1()       || return String
     *                  ""WIP""
     *
     *
     * Functions used form other classes:
     * getResponse(IP, API URL, API KEY) || return String
     */

    //Common API call:
    public static void decodeVersions(){
        try {

        } catch (Exception e) {
        }
    }
    public static void decodeConnections(){
        try {

        } catch (Exception e) {
        }
    }
    public static void decodeFiles(){
        try {

        } catch (Exception e) {
        }
    }
    public static void decodeJob(){
        try {

        } catch (Exception e) {
        }
    }
    public static void decodePrinter(){
        try {

        } catch (Exception e) {
        }
    }


    //Api version V0.1:
    public static void decodeConnections_V1(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "connection", mainActivity.key));
            memory.options_dec = connection_get.getString("options");
            memory.current_dec = connection_get.getString("current");
        } catch (Exception e) {
        }
    }
    public static void decodeFiles_V1(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }
    public static void decodeVersions_V1(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }
    public static void decodeJob_V1(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }
    public static void decodePrinter_V1(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }


    //Api version v0.2?? (DEVEL)
    public static void decodeConnections_V2(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "connection", mainActivity.key));
            memory.options_dec = connection_get.getString("options");
            memory.current_dec = connection_get.getString("current");
        } catch (Exception e) {
        }
    }
    public static void decodeFiles_V2(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }
    public static void decodeVersions_V2(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }
    public static void decodeJob_V2(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }
    public static void decodePrinter_V2(){
        try {
            JSONObject connection_get = new JSONObject(getResponse(mainActivity.ip, "files", mainActivity.key));
            memory.files_dec = connection_get.getString("files");
        } catch (Exception e) {
        }
    }



}
