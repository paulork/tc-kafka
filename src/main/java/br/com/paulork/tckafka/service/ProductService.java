package br.com.paulork.tckafka.service;

import br.com.paulork.tckafka.domain.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public void saveProduct() {

    }

}
