package command;

import lombok.Getter;
import model.Price;

import java.math.BigDecimal;

@Getter
public class CreateProductOptionCommand {
    private Long productId;
    private String name;
    private BigDecimal netPrice;
    private BigDecimal grossPrice;
    private Double taxRate;
    private String type;
    private String value;
    private Integer quantity;
}
