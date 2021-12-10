package br.com.paulork.tckafka;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalstackSNSTestContainerTest extends AbstractBaseTest {

    private static AmazonSNS sns;
    private static AmazonSQS sqs;

    @Value("${test.aws.region}")
    private static String region;

    @Container
    public static LocalStackContainer localStack = new LocalStackContainer(DockerImageName
            .parse("localstack/localstack:0.11.3"))
            .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS)
            .withEnv("DEFAULT_REGION", region)
            .withExposedPorts(4566)
            .withReuse(true)
            .waitingFor(
                    Wait.forLogMessage(".*Ready.*\\n", 1)
            );

    @BeforeAll
    public static void runBefore() {
        sns = AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SNS))
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .build();
        sns.createTopic("local_sns");

        sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .build();
        sqs.createQueue("sqs-primary");
        sqs.createQueue("sqs-secondary");
        sqs.createQueue("sqs-tertiary");

        SubscribeRequest sns_sr = new SubscribeRequest();
        sns_sr.setEndpoint("http://"+localStack.getHost()+":"+localStack.getMappedPort(4566).toString());
        sns_sr.setEndpoint("http://"+localStack.getHost()+":"+localStack.getFirstMappedPort().toString());
        sns_sr.setTopicArn("arn:aws:sns:us-east-1:000000000000:local_sns");
        sns_sr.setProtocol("sqs");
        sns.subscribe(sns_sr);

        sns = AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SNS))
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .build();

        sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .build();
    }

    @Test
    public void testIfLocalstackIsRunning() {
        assertTrue(localStack.isRunning(), "Localstack is running");
    }

    @Test
    public void testIfSQSQueusExists() {
        String port = localStack.getMappedPort(4566).toString();
        String host = localStack.getHost();

        String primary = "http://" + host + ":" + port + "/000000000000/sqs-primary";
        String secondary = "http://" + host + ":" + port + "/000000000000/sqs-secondary";
        String tertiary = "http://" + host + ":" + port + "/000000000000/sqs-tertiary";

        assertEquals(3, sqs.listQueues().getQueueUrls().size(), "Number of SQS queue's");
        assertEquals(primary, sqs.getQueueUrl("sqs-primary").getQueueUrl(), "Primary SQS queue");
        assertEquals(secondary, sqs.getQueueUrl("sqs-secondary").getQueueUrl(), "Secondary SQS queue");
        assertEquals(tertiary, sqs.getQueueUrl("sqs-tertiary").getQueueUrl(), "Tertiary SQS queue");
    }

    @Test
    public void testIfSNSTopicExists() {
        assertEquals(1, sns.listTopics().getTopics().size(), "Number of SNS topics");
        assertEquals("arn:aws:sns:us-east-1:000000000000:local_sns",
                sns.listTopics().getTopics().get(0).getTopicArn(), "SNS topic name");
    }

    @Test
    public void testIfSNStoSQSSubscriptionExists() {
        assertEquals(1, sns.listSubscriptions()
                .getSubscriptions().size(), "Number of SNS subscriptions");
        assertEquals("arn:aws:sns:us-east-1:000000000000:local_sns",
                sns.listSubscriptions()
                        .getSubscriptions()
                        .get(0).getTopicArn(), "SNS subscription topic");
    }

}
