package com.dapr.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.client.DaprClient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/publish")
public class ProducerController {

    private final DaprClient daprClient;
    private static final String PUBSUB = "pubsub";
    private final ObjectMapper objectMapper;

    public ProducerController(DaprClient daprClient,ObjectMapper objectMapper) {
        this.daprClient = daprClient;
        this.objectMapper = objectMapper;
    }

    // 1. RAW MESSAGE
     @PostMapping("/raw")
    @ResponseStatus(HttpStatus.OK)
    public String publishRaw() {
        Message message = new Message("RAW-001","Laptop",75000);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("rawPayload", "true");
        metadata.put("content-type", "application/json");
        daprClient.publishEvent(PUBSUB,"orders-raw",message,metadata).block();
        System.out.println("RAW MESSAGE PUBLISHED");
        System.out.println(message);
        return "Raw message published";
    }



    // 2. DAPR-GENERATED CLOUD EVENT
    @PostMapping("/dapr-cloud-event")
    @ResponseStatus(HttpStatus.OK)
    public String publishDaprCloudEvent() {
        Message message = new Message("DAPR-001","Mobile",5000);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("content-type", "application/json");
        daprClient.publishEvent(PUBSUB,"orders-dapr-cloud-event",message, metadata).block();
        System.out.println("DAPR-GENERATED CLOUD EVENT PUBLISHED");
        System.out.println(message);
        return "Dapr-generated CloudEvent published";
    }

    // 3. DAPR-GENERATED CLOUD EVENT WITH CUSTOM CLOUD EVENT ATTRIBUTES

    @PostMapping("/custom-metadata")
    @ResponseStatus(HttpStatus.OK)
    public String publishWithCustomMetadata() {
        Message message = new Message("CUSTOM-001","Tablet",35000);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("cloudevent.id",UUID.randomUUID().toString());
        metadata.put("cloudevent.source","payment-service");
        metadata.put("cloudevent.type","com.example.order.created");
        metadata.put("content-type","application/json");
        daprClient.publishEvent(PUBSUB,"orders-custom-cloud-event",message,metadata).block();
        System.out.println("DAPR CLOUD EVENT WITH CUSTOM METADATA PUBLISHED");
        System.out.println(message);
        return "Custom metadata CloudEvent published";
    }


    // 4. APPLICATION-CREATED CLOUD EVENT
    @PostMapping("/custom-cloud-event")
    @ResponseStatus(HttpStatus.OK)
    public String publishCustomCloudEvent() throws JsonProcessingException {
        Message message = new Message("APP-OWN-001","Headphones",12000);
        Map<String, Object> cloudEvent = new HashMap<>();
        cloudEvent.put("specversion","1.0");
        cloudEvent.put("type","com.example.order.created");
        cloudEvent.put("source","payment-service");
        cloudEvent.put("id",UUID.randomUUID().toString());
        cloudEvent.put("subject","orders/ORD-OWN-001");
        cloudEvent.put("datacontenttype","application/json");
        cloudEvent.put("data",message);
        String cloudEventJson = objectMapper.writeValueAsString(cloudEvent);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("content-type","application/cloudevents+json");
        daprClient.publishEvent(PUBSUB,"orders-custom-envelope",cloudEventJson,metadata).block();
        System.out.println("APPLICATION-CREATED CLOUD EVENT PUBLISHED");
        System.out.println(cloudEventJson);
        return "Custom CloudEvent published";
    }




}