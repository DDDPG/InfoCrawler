package com.crawlerdemo.webmagic.isoBili;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Data
public class BiliUnitRepo {
    private String title;
    private String url;
    private String audioSourceUrl;

    void generateAudioSourceUrl() throws IOException {
        String htmlContent = fetchHTML(this.url);
    }

    private String fetchHTML(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
        }
        return htmlContent.toString();
    }
}
