package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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