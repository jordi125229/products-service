package app.mappers;

import dto.ProductConfigurationDto;
import lombok.RequiredArgsConstructor;
import model.ProductConfiguration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConfigurationMapper {

    private final ProductMapper productMapper;

    public ProductConfigurationDto toDto(ProductConfiguration configuration) {
        ProductConfigurationDto dto = new ProductConfigurationDto();
        dto.setProduct(productMapper.toDto(configuration.getProduct()));
        dto.setSelectedOptions(productMapper.toDto(configuration.getSelectedOptions()));
        return dto;
    }
}
