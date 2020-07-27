package com.house_guard.internal_system_controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class RestAPI {

    private Logger _LOGGER;
    private String _ipAddress;
    private String _port;
    private String _guid;
    private URL _url;
    private List<DataResponse> _list;

    public void setPort(String port) {
        this._port = port;
    }

    public void setIPAddress(String IPAddress) {
        this._ipAddress = IPAddress;
    }

    public void setGUID(String guid) {
        this._guid = guid;
    }

    public RestAPI(Logger logger, String ip, String port, String guid) {
        this._LOGGER = logger;
        this._list = new ArrayList<DataResponse>();
        this._ipAddress = ip;
        this._port = port;
        this._guid = guid;
    }

    public List<DataResponse> getList() {
        return this._list;
    }

    private Boolean getURL(RequestData data) {
        try {
            this._url = new URL("http://" + _ipAddress + ":" + _port + "/data?" + "guid=" + this._guid
                    + "&time_from=" + data.getTime_From() + "&time_to=" + data.getTime_To()
                    + "&event_type_id=" + data.getEventTypeId());
            return true;
        } catch (MalformedURLException e) {
            System.out.println("Failure in url");
            return false;
        }
    }

    private void convertStringToList(String response) {
        try {
            
        JSONArray arr = new JSONArray(response);
        _list = new ArrayList<DataResponse>();
        for(int i = 0; i < arr.length(); i++){
            DataResponse local = new DataResponse(arr.getJSONObject(i).getInt("_id"),
                    arr.getJSONObject(i).getInt("_messageNum"),
                    arr.getJSONObject(i).getInt("_totalMessage"),
                    arr.getJSONObject(i).getString("_topicMessage"),
                    arr.getJSONObject(i).getString("_timeSent"));
            _list.add(local);
        }
        } catch (JSONException e) {
            _LOGGER.warning("Error in conversion of response");
        }
    }

    public boolean testData(RequestData data) {
        try {
            if (!this.getURL(data)) {
                return false;
            }
            HttpURLConnection conn = (HttpURLConnection) this._url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                return false;
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return true;
    }

    public boolean getLogs(RequestData data) {
        try {
            if (!this.getURL(data)) {
                return false;
            }
            HttpURLConnection conn = (HttpURLConnection) this._url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = conn.getInputStream();
            } else {
                return false;
            }
        
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));
        
            StringBuilder response = new StringBuilder();
            String currentLine;
        
            while ((currentLine = in.readLine()) != null) 
                response.append(currentLine);
        
            in.close();
        

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return false;
    }
}