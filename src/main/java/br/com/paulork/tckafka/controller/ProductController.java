package br.com.paulork.tckafka.controller;

import br.com.paulork.tckafka.service.ProductService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    public void saveProduct() {

    }

}
