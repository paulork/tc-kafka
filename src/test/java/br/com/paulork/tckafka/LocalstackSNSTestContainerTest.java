package br.com.paulork.tckafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalstackSNSTestContainerTest extends AbstractBaseTest {

    @Value("${test.aws.region}")
    private static String region;

    @Container
    public static LocalStackContainer localStack = new LocalStackContainer(DockerImageName
            .parse("localstack/localstack:0.11.3"))
            .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS)
            .withEnv("DEFAULT_REGION", region);

    @Test
    public void testeLocalstack() {
        assertTrue(localStack.isRunning(), "Localstack is running");
    }

}
