package com.house_guard.internal_system_controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Vector;

public class RestAPI {

    private String _ipAddress;
    private String _port;
    private String _guid;
    private URL _url;

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
            if (conn.getResponseCode() != 200) {
                System.out.println("Error on call");
                return false;
            }
            InputStream response = this._url.openStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
                ObjectMapper map = new ObjectMapper();
                List<DataResponse> responses = Arrays.asList(map.readValue(responseBody, DataResponse[].class));
                responses.stream().forEach(x -> System.out.println(x));
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return false;
    }
}