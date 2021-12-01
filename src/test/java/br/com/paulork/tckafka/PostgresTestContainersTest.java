package br.com.paulork.tckafka;

import br.com.paulork.tckafka.domain.Product;
import br.com.paulork.tckafka.domain.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


@ContextConfiguration(initializers = PostgresTestContainersTest.Initializer.class)
public class PostgresTestContainersTest extends AbstractBaseTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"));

    @Autowired
    private ProductRepository repository;

    @Autowired
    private DataSource ds;

    @Test
    public void testIfTheDatabaseIsUp() throws SQLException {
        ResultSet resultSet = performQuery(postgres, "SELECT 1");
        int resultSetInt = resultSet.getInt(1);
        Assertions.assertEquals(1, resultSetInt, "A basic SELECT query succeeds");

    }

    @Test
    public void testInsertProductsInDatabase() throws InterruptedException {
        Product product1 = new Product("Mochila", 120.0);
        Product product2 = new Product("Notebook", 2500.0);
        repository.save(product1);
        repository.save(product2);

        List<Product> all = repository.findAll();
        Assertions.assertEquals(2, all.size(), "Number of records inserted");

        Product find1 = repository.findById(1L).orElseThrow();
        Product find2 = repository.findById(2L).orElseThrow();
        Assertions.assertEquals("Mochila", find1.getName(), "Product 1 name");
        Assertions.assertEquals("Notebook", find2.getName(), "Product 2 name");
    }

    // HELPER FUNCTION
    private ResultSet performQuery(JdbcDatabaseContainer<?> container, String sql) throws SQLException {
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        return resultSet;
    }

    // CONTEXT CONFIG CLASS
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            postgres.start();
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.password=" + postgres.getPassword(),
                    "spring.datasource.username=" + postgres.getUsername()
            );
            values.applyTo(configurableApplicationContext);
        }
    }

}
