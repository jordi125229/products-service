package dto;

import lombok.Getter;
import lombok.Setter;
import model.Price;

import java.util.Set;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private Price price;
    private String type;
    private Integer quantity;
    private Set<ProductOptionDto> options;
}