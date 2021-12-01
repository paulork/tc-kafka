package br.com.paulork.tckafka;

import br.com.paulork.tckafka.kafka.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KafkaTestContainersTest extends AbstractBaseTest {

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaTemplate<String, String> producer;
//    private KafkaProducer producer;

    @Value("${test.topic}")
    private String topic;

    @Test
    public void givenKafkaDockerContainer_whenSendingtoSimpleProducer_thenMessageReceived() throws Exception {
        producer.send(topic, "Sending with own controller");
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

        assertEquals(0L, consumer.getLatch().getCount());
        assertEquals("Sending with own controller", consumer.getPayload(), "Payload content");
    }

}
