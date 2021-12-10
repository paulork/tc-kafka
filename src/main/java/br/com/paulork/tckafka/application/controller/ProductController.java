package br.com.paulork.tckafka.application.controller;

import br.com.paulork.tckafka.application.dto.ProductDTO;
import br.com.paulork.tckafka.domain.service.ProductService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        return service.saveProduct(productDTO);
    }

}
