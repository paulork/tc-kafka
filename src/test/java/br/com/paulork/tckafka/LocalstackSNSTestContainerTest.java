package br.com.paulork.tckafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(initializers = PostgresTestContainersTest.Initializer.class)
public class LocalstackSNSTestContainerTest extends AbstractBaseTest {

    @Value("${test.aws.region}")
    private static String region;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
            .withDatabaseName("products")
            .withUsername("postgres")
            .withPassword("password");

    @Container
    public static LocalStackContainer localStack = new LocalStackContainer(DockerImageName
            .parse("localstack/localstack:0.11.3"))
            .withServices(LocalStackContainer.Service.SNS)
            .withEnv("DEFAULT_REGION", region);

    @Test
    public void testeLocalstack() {
        Assertions.assertTrue(localStack.isRunning());
    }

}
