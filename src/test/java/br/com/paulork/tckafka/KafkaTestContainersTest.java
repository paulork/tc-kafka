package br.com.paulork.tckafka;

import br.com.paulork.tckafka.infrastructure.kafka.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KafkaTestContainersTest extends AbstractBaseTest {

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaTemplate<String, String> producer;

    @Value("${kafka.topic}")
    private String topic;

    @Test
    public void givenKafkaDockerContainer() {
        producer.send(topic, "Sending with own controller");

        given()
            .await()
            .atMost(3000, TimeUnit.MILLISECONDS)
            .untilAsserted(() ->
                assertEquals("Sending with own controller", consumer.getPayload(), "Payload content")
            );
    }

}
