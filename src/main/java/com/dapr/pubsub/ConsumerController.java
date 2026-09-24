package com.dapr.pubsub;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {



    // 1. RAW MESSAGE
    @PostMapping("/raw")
    @ResponseStatus(HttpStatus.OK)
    @Topic(pubsubName = "pubsub",name = "orders-raw",metadata = """
                      {"rawPayload": "true", "content-type": "application/json"}""")
    public void consumeRaw( @RequestBody Message message) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("RAW MESSAGE RECEIVED");
        System.out.println("==========================================");
        System.out.println("Message = " + message);
    }

    // 2. DAPR-GENERATED CLOUD EVENT
    @PostMapping("/dapr-cloud-event")
    @ResponseStatus(HttpStatus.OK)
    @Topic(pubsubName = "pubsub",name = "orders-dapr-cloud-event")
    public void consumeDaprCloudEvent(@RequestBody CloudEvent<Message> cloudEvent) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("DAPR-GENERATED CLOUD EVENT RECEIVED");
        System.out.println("==========================================");
        printCloudEvent(cloudEvent);
    }

    // 3. DAPR CLOUD EVENT WITH CUSTOM METADATA
    @PostMapping("/custom-metadata")
    @ResponseStatus(HttpStatus.OK)
    @Topic( pubsubName = "pubsub", name = "orders-custom-cloud-event")
    public void consumeCustomMetadata(@RequestBody CloudEvent<Message> cloudEvent) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("CUSTOM METADATA CLOUD EVENT RECEIVED");
        System.out.println("==========================================");
        printCloudEvent(cloudEvent);
    }


    // 4. APPLICATION-CREATED CLOUD EVENT
    @PostMapping("/custom-cloud-event")
    @ResponseStatus(HttpStatus.OK)
    @Topic(pubsubName = "pubsub",name = "orders-custom-envelope")
    public void consumeCustomCloudEvent(@RequestBody CloudEvent<Message> cloudEvent) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("APPLICATION-CREATED CLOUD EVENT RECEIVED");
        System.out.println("==========================================");
        printCloudEvent(cloudEvent);
    }




    // COMMON CLOUD EVENT PRINT METHOD

    private void printCloudEvent(CloudEvent<Message> cloudEvent) {
        System.out.println("ID       : " + cloudEvent.getId());
        System.out.println("Source   : " + cloudEvent.getSource());
        System.out.println("Type     : " + cloudEvent.getType());
        System.out.println("Spec     : " + cloudEvent.getSpecversion());
        System.out.println("DataType : " + cloudEvent.getDatacontenttype());
        System.out.println("Data     : " + cloudEvent.getData());
    }
}