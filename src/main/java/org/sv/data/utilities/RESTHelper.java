package org.sv.data.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RESTHelper {

    private static CloseableHttpClient httpClient;

    public static String executeGetRequest(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpGet);
    }

    private static String executeRequest(HttpUriRequest request) throws IOException {
        if (Objects.isNull(httpClient)) {
            initializeHttpClient();
        }
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                // Request successful, read and return the response
                return readResponse(response.getEntity());
            } else {
                // Handle error responses, if needed
                throw new IOException("Request failed with status code: " + statusCode);
            }
        }
    }

    public static synchronized void initializeHttpClient() {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpClients.custom().build();
        }
    }

    private static String readResponse(HttpEntity entity) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    public static String executePostRequest(String url, String requestBody) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(requestBody));
        return executeRequest(httpPost);
    }

    public static String executePutRequest(String url, String requestBody) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(requestBody));
        return executeRequest(httpPut);
    }
}
