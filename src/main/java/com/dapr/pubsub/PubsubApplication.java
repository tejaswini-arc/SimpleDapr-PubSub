package com.dapr.pubsub;

import io.dapr.client.DaprClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;

import java.util.Map;

@SpringBootApplication
public class PubsubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PubsubApplication.class, args);


}
}
