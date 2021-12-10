package br.com.paulork.tckafka.domain.converter;

import br.com.paulork.tckafka.application.dto.ProductDTO;
import br.com.paulork.tckafka.domain.entity.Product;

public class ProductConverter {

    private ProductConverter(){}

    public static Product toDomain(ProductDTO productDTO) {
        return new Product(
                productDTO.getId(),
                productDTO.getName(),
                productDTO.getPrice()
        );
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

}
