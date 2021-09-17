package com.fusionauth.videoteka.config;

import io.fusionauth.client.FusionAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FusionAuthConfig {

    @Value("${fusionAuth.apiKey}")
    private String apiKey;

    @Value("${fusionAuth.baseUrl}")
    private String baseUrl;

    @Bean
    public FusionAuthClient setupClient(){
        FusionAuthClient fusionAuthClient = new FusionAuthClient(apiKey,baseUrl);
        fusionAuthClient.connectTimeout = 10_000;
        fusionAuthClient.readTimeout = 10_000;
        return fusionAuthClient;
    }


    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
