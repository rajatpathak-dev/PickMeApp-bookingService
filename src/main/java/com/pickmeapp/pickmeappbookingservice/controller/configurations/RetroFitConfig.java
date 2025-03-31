package com.pickmeapp.pickmeappbookingservice.controller.configurations;

import com.netflix.discovery.EurekaClient;
import com.pickmeapp.pickmeappbookingservice.apis.LocationServiceApi;
import com.pickmeapp.pickmeappbookingservice.apis.PickMeSocketApi;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetroFitConfig {

    @Autowired
    private EurekaClient eurekaClient;


    private String getServiceUrl(String serviceName) {
        return eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi locationServiceApi() {
        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("PICKMEAPP-LOCATIONSERVICE"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);
    }

    @Bean
    public PickMeSocketApi pickMeSocketApi() {
        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("PICKMEAPP-SOCKETSERVICE"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(PickMeSocketApi.class);
    }



}
