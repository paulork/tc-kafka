package br.com.paulork.tckafka;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@RunWith(SpringRunner.class)
@Import(br.com.paulork.tckafka.KafkaTestConteinersLiveTest.KafkaTestContainersConfiguration.class)
@SpringBootTest(classes = TcKafkaApplication.class)
@DirtiesContext
public class PostgresTestConteinersTest {

    @ClassRule
    public static PostgreSQLContainer<?> post = new PostgreSQLContainer<>(DockerImageName.parse("postgres:9.6.12"));

    private void teste() {
        // insert
        // insert
        // insert
        // insert
        // insert
    }

}
