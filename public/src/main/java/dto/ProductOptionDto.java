package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Price;

@Getter
@Setter
@NoArgsConstructor
public class ProductOptionDto {

    private Long id;
    private String name;
    private Price price;
    private String type;
}



