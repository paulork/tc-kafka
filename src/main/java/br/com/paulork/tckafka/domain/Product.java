package br.com.paulork.tckafka.domain;

import javax.persistence.*;

@Entity
@Table(name = "product", schema = "prk")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private Double price;

}
