package com.house_guard.internal_system_controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestAPI {

    private String _ipAddress;
    private String _port;
    private String _guid;

    public void setPort(String port) {
        this._port = port;
    }

    public void setIPAddress(String IPAddress) {
        this._ipAddress = IPAddress;
    }

    public void setGUID(String guid) {
        this._guid = guid;
    }

    public RestAPI(String ip, String port, String guid) {
        this._ipAddress = ip;
        this._port = port;
        this._guid = guid;
    }

    public void getData(RequestData data) {
        try {
            URL url = new URL("http://" +
            _ipAddress + ":" +
            _port + "/data?" +
            "guid=" + this._guid + 
            "&id=" + data.getRequest_Id() +
            "&time_from=" + data.getTime_From() + 
            "&time_to=" + data.getTime_To() + 
            "&event_type_id=" + data.getTime_To());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            System.out.println(conn.getResponseMessage());
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }
}