package br.com.paulork.tckafka;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = TcKafkaApplication.class)
@DirtiesContext
abstract class AbstractBaseTest {

}
