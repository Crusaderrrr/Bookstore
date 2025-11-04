package com.bookstore.app.service;

import java.io.IOException;
import java.util.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaypalService {
    @Value("${paypal.client.id}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.api.base}")
    private String apiBase;

    public String getAccessToken() throws IOException {
        String auth =
                Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        HttpPost post = new HttpPost(apiBase + "/v1/oauth2/token");
        post.setHeader("Authorization", "Basic " + auth);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new StringEntity("grant_type=client_credentials"));
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(post);
            String json = EntityUtils.toString(response.getEntity());
            return new JSONObject(json).getString("access_token");
        }
    }

    public boolean captureOrder(String orderId) throws IOException {
        String token = getAccessToken();
        HttpPost post = new HttpPost(apiBase + "/v2/checkout/orders/" + orderId + "/capture");
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(post);
            String json = EntityUtils.toString(response.getEntity());
            JSONObject result = new JSONObject(json);
            return "COMPLETED".equals(
                    result.optJSONArray("purchase_units").optJSONObject(0)
                            .optJSONObject("payments").optJSONArray("captures")
                            .optJSONObject(0).optString("status"));
        }
    }
}