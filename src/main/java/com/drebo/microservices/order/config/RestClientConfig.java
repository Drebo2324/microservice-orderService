package com.drebo.microservices.order.config;

import com.drebo.microservices.order.client.InventoryClient;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RestClientConfig {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;
    private final ObservationRegistry observationRegistry;

    @Bean
    public InventoryClient inventoryClient(){
        try {
            RestClient restClient = RestClient.builder()
                    .baseUrl(inventoryServiceUrl)
                    //RestClient internally uses ClientHttpRequestFactory to handle mechanics of http calls
                    //ClientHttpRequestFactory creates instances of ClientHttpRequest -> http request object that RestClient uses to make http calls
                    .requestFactory(getClientRequestFactory())
                    //propagate trace id when calling inventory
                    .observationRegistry(observationRegistry)
                    .build();

            var restClientAdapter = RestClientAdapter.create(restClient);
            var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
            //creates proxy instance for InventoryClient
            //proxy translates method calls using http requests via RestClient
            return httpServiceProxyFactory.createClient(InventoryClient.class);
        } catch (Exception e) {
            log.error("Error creating inventoryClient: {}", e.getMessage());
            throw e;
        }

    }

    private ClientHttpRequestFactory getClientRequestFactory(){
        //settings for ClientHttpRequest used by the RestClient
        //set timeout
        ClientHttpRequestFactorySettings clientHttpRequestFactorySettings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(5))
                .withReadTimeout(Duration.ofSeconds(5));
        return ClientHttpRequestFactories.get(clientHttpRequestFactorySettings);
    }
}
