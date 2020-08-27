package com.muradit.projectx.Model.others;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NotificationsSender {

    private  String TYPE;

    public NotificationsSender(String TYPE) {
        this.TYPE = TYPE;
    }

    public  void sendNotification(final String send_email, final String content) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    //This is a Simple Logic to Send Notification different Device Programmatically....

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic ZDAxMmVlNWEtZjg1OC00OGZlLTg2ZGQtMzY5MzRlMTgwMTlj");
                        con.setRequestMethod("POST");

                        String strJsonBody;
                        //to specific user
                        if(TYPE.equalsIgnoreCase("spec")) {
                            strJsonBody = "{"
                                    + "\"app_id\": \"9537c3bd-a2bb-4f3a-92b7-f87ee3e8d8ca\",\n"
//                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" +send_email+ "\"}],"
                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                    + "\"data\": {\"foo\": \"bar\"},"
                                    + "\"contents\": {\"en\": \"" + content + "\"}"
                                    + "}";
                        }else{
                            strJsonBody="{ \"app_id\": \"9537c3bd-a2bb-4f3a-92b7-f87ee3e8d8ca\",\n" +
                                    "  \"included_segments\": [\"All\"],\n" +
                                    "  \"data\": {\"foo\": \"bar\"},\n" +
                                    "  \"contents\": {\"en\": \"English Message\"}}";
                        }

                        //to all users
                       /* String strJsonBody="{ \"app_id\": \"9537c3bd-a2bb-4f3a-92b7-f87ee3e8d8ca\",\n" +
                                "  \"included_segments\": [\"All\"],\n" +
                                "  \"data\": {\"foo\": \"bar\"},\n" +
                                "  \"contents\": {\"en\": \"English Message\"}}";*/


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }




}
