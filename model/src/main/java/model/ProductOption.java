package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductOption {

    private Long id;
    private Long productId;
    private Long sourcedProductId;
    private String name;
    private Price price;
    private String type;
    private Integer quantity;

    public ProductOption(String name, Price price, String type, Integer quantity) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
    }
}
