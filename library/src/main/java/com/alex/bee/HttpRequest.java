package com.alex.bee;

import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alex on 2/7/14.
 */
public class HttpRequest {

    // TODO by Alex.handle response
    public static String post(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpURLConnection connection = client.open(new URL(url));
        OutputStream out = null;
        InputStream in = null;
        try {
            // Write the request.
            connection.setRequestMethod("POST");
            out = connection.getOutputStream();
            out.close();

            // Read the response.
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected HTTP response: "
                        + connection.getResponseCode() + " " + connection.getResponseMessage());
            }
            in = connection.getInputStream();
            return readFirstLine(in);
        } finally {
            // Clean up.
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    public static String readFirstLine(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        return reader.readLine();
    }
}
