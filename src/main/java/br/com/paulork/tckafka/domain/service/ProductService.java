package br.com.paulork.tckafka.domain.service;

import br.com.paulork.tckafka.application.dto.ProductDTO;
import br.com.paulork.tckafka.domain.converter.ProductConverter;
import br.com.paulork.tckafka.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        return ProductConverter.toDTO(
                repository.save(
                        ProductConverter.toDomain(productDTO)
                )
        );
    }

}
