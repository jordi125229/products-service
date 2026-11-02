package command;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActualizeProductCommand {
    private String name;
    private BigDecimal netPrice;
    private BigDecimal grossPrice;
    private Double taxRate;
    private String type;
    private Integer quantity;
    private String ownerId;
}
