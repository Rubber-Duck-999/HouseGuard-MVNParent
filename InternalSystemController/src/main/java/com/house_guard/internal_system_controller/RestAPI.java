package com.house_guard.internal_system_controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;

public class RestAPI {

    private Logger _LOGGER;
    private String _ipAddress;
    private String _port;
    private String _guid;
    private URL _url;
    private String[][] _array;
    private final int columns = 3;

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
        this._array = new String[0][0];
        this._ipAddress = ip;
        this._port = port;
        this._guid = guid;
    }

    public String[][] getArray() {
        return this._array;
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

    private void convertStringToArray(String response) {
        try {
            JSONArray arr = new JSONArray(response);
            this._array = new String[arr.length()][columns];
            System.out.println("Length of jsonarray: " + arr.length());
            for(int i = 0; i < arr.length(); i++) {
                System.out.println("Message: " + i + ",  " + 
                    Integer.toString(arr.getJSONObject(i).getInt("_messageNum")) + ", " +
                    arr.getJSONObject(i).getString("_topicMessage") + ", " +
                    arr.getJSONObject(i).getString("_timeSent"));
                this._array[i][0] = Integer.toString(arr.getJSONObject(i).getInt("_messageNum"));
                this._array[i][1] = arr.getJSONObject(i).getString("_topicMessage");
                this._array[i][2] = arr.getJSONObject(i).getString("_timeSent");                  
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
                _LOGGER.warning("Error response code: " + conn.getResponseCode());
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
            System.out.println("REST Connection");
            if (!this.getURL(data)) {
                return false;
            }
            HttpURLConnection conn = (HttpURLConnection) this._url.openConnection();
            conn.setRequestMethod("GET");
            System.out.println("URL: " + this._url);
            int responseCode = conn.getResponseCode();
            InputStream inputStream;
            System.out.println("Response: " + responseCode);
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
        
            System.out.println("Response: " + response.toString());
            convertStringToArray(response.toString());
            in.close();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return true;
    }
}