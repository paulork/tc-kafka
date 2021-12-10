package br.com.paulork.tckafka;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisTestContainersTest extends AbstractBaseTest {

    @Container
    public static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.2.6"))
            .withExposedPorts(6379)
            .withReuse(true);

    private final Jedis jedis = new Jedis(redis.getHost(), redis.getMappedPort(6379));

    @Test
    @Order(1)
    public void testRedisIsRunning() {
        assertTrue(redis.isRunning());
    }

    @Test
    @Order(2)
    public void testInsertDataAndGetData() {
        var data = "Dados inseridos";
        jedis.set("xxxyyyzzz", data);

        assertEquals(data, jedis.get("xxxyyyzzz"));
    }

    @Test
    @Order(3)
    public void testGetAndUpdateData() {
        var data = jedis.get("xxxyyyzzz");
        assertEquals(data, "Dados inseridos");

        jedis.set("xxxyyyzzz", "Dados atualizados");
        assertNotEquals(data, jedis.get("xxxyyyzzz"));
    }

    @Test
    @Order(4)
    public void testGetAndDeleteData() {
        var data = jedis.get("xxxyyyzzz");
        assertEquals(data, "Dados atualizados");

        jedis.del("xxxyyyzzz");
        assertNull(jedis.get("xxxyyyzzz"));
    }

}
