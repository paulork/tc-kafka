package br.com.paulork.tckafka;

import br.com.paulork.tckafka.domain.entity.Product;
import br.com.paulork.tckafka.domain.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.JdbcDatabaseContainer;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgresTestContainersTest extends AbstractBaseTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private DataSource ds;

    @Test
    public void testIfTheDatabaseIsUp() throws SQLException {
        ResultSet resultSet = performQuery(postgres, "SELECT 1");
        int resultSetInt = resultSet.getInt(1);
        assertEquals(1, resultSetInt, "A basic SELECT query succeeds");
    }

    @Test
    public void testInsertProductsInDatabase() {
        Product product1 = new Product("Mochila", 120.0);
        Product product2 = new Product("Notebook", 2500.0);
        repository.save(product1);
        repository.save(product2);

        List<Product> all = repository.findAll();
        assertEquals(2, all.size(), "Number of records inserted");

        Product find1 = repository.findById(1L).orElseThrow();
        Product find2 = repository.findById(2L).orElseThrow();
        assertEquals("Mochila", find1.getName(), "Product 1 name");
        assertEquals("Notebook", find2.getName(), "Product 2 name");
    }

    // HELPER FUNCTION
    private ResultSet performQuery(JdbcDatabaseContainer<?> container, String sql) throws SQLException {
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        return resultSet;
    }

}
