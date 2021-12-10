package br.com.paulork.tckafka.infrastructure.sns;

import br.com.paulork.tckafka.domain.entity.Product;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SNSProducer {

    private final AmazonSNSAsync snsAsync;
    private final ObjectMapper objectMapper;

    @Value("${cloud.aws.sns.topic}")
    private String topic;

    public SNSProducer(AmazonSNSAsync snsAsync, ObjectMapper objectMapper) {
        this.snsAsync = snsAsync;
        this.objectMapper = objectMapper;
    }

    public void send(Product product) {
        try {
            snsAsync.publishAsync(this.topic, objectMapper.writeValueAsString(product));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
