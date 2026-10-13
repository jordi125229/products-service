package dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class ProductConfigurationDto {

    private ProductDto product;
    private Set<ProductOptionDto> selectedOptions;
}
