package model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private Price price;
    private String type;
    private Integer quantity;
    private Set<ProductOption> options;

    public Product(String name, Price price, String type, Integer quantity) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
        this.options = new HashSet<>();
    }
}