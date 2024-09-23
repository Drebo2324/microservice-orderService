package com.drebo.microservices.order.stub;

import lombok.experimental.UtilityClass;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

//call static method. No instantiation
@UtilityClass
public class InventoryStub {

    public void stubInventoryCall(String sku, Integer quantity){
        stubFor(get(urlEqualTo("/api/inventory?sku=" + sku + "&quantity=" + quantity))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));
    }
}
