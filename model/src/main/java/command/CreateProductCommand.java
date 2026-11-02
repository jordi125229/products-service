package command;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductCommand {
    private String name;
    private BigDecimal netPrice;
    private BigDecimal grossPrice;
    private Double taxRate;
    private String type;
    private Integer quantity;
    private String ownerId;
}
