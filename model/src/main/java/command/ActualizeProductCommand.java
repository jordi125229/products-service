package command;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ActualizeProductCommand {
    private String name;
    private BigDecimal netPrice;
    private BigDecimal grossPrice;
    private Double taxRate;
    private String type;
    private Integer quantity;
    private String ownerId;
}
