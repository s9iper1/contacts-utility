package com.byteshaft.contacts_utility;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Helpers {

    private static final String link = "http://45.55.212.164/api/add_contact";

    public static int authPostRequest(HashMap<String, String> hashMap) throws IOException {
        URL url;
        url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        String authString = "access_user" + ":" + "secret_supersecret";
        String authStringEncoded = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
        connection.setRequestProperty("Authorization", "Basic " + authStringEncoded);
        String home_address ="";
        if (hashMap.containsKey("home_address")) {
            home_address = hashMap.get("home_address");
        }
        String work_address = "";
        if (hashMap.containsKey("work_address")) {
            work_address = hashMap.get("work_address");
        }
        String org = "";
        if (hashMap.containsKey("org")) {
            work_address = hashMap.get("org");
        }
        String bd = "";
        if (hashMap.containsKey("bd")) {
            bd = hashMap.get("bd");
        }
        String home_email = "";
        if (hashMap.containsKey("home_email")) {
            home_email = hashMap.get("home_email");
        }
        String work_email = "";
        if (hashMap.containsKey("work_email")) {
            work_email = hashMap.get("work_email");
        }
        String title = "";
        if (hashMap.containsKey("title")) {
            title = hashMap.get("title");
        }
        String name = "";
        if (hashMap.containsKey("name")) {
            name = hashMap.get("name");
        }
        String home_phone = "";
        if (hashMap.containsKey("home_phone")) {
            home_phone = hashMap.get("home_phone");
        }
        String work_phone = "";
        if (hashMap.containsKey("work_phone")) {
            work_phone = hashMap.get("work_phone");
        }
        String jsonFormattedData = getJsonObjectString(
                home_address, work_address, org,
                bd, home_email, work_email,
                title, name, home_phone,
                work_phone);
        sendRequestData(connection, jsonFormattedData);
        return connection.getResponseCode();
    }

    private static String getJsonObjectString(String address_home, String address_work,
                                              String company, String date_of_birth, String email_home,
                                              String email_work, String job_title, String name,
                                              String phone_home, String phone_work) {
        return String.format("{\"address_home\": \"%s\", \"address_work\": \"%s\", \"company\": \"%s\"," +
                " \"date_of_birth\": \"%s\", \"email_home\": \"%s\", " +
                "\"email_work\": \"%s\", \"job_title\": \"%s\", \"name\": \"%s\" , " +
                " \"phone_home\": \"%s\", \"phone_work\": \"%s\"}", address_home, address_work, company
        , date_of_birth, email_home, email_work, job_title, name, phone_home, phone_work);
    }

    public static void sendRequestData(HttpURLConnection connection, String body) throws IOException {
        byte[] outputInBytes = body.getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write(outputInBytes);
        os.close();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                AppGlobals.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // ping the google server to check if internet is really working or not
    public static boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    // get default sharedPreferences.
    private static SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(AppGlobals.getContext());
    }

    // Method to get String value from sharedPreference requires key as parameter
    public static boolean containsData(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.contains(key);
    }

    public static void saveContactStatus(String key, boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
}
