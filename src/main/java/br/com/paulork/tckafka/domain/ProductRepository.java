package br.com.paulork.tckafka.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAll();

    <S extends Product> S save(S entity);

    Optional<Product> findById(Long aLong);

    void deleteById(Long aLong);

    void delete(Product entity);

}
