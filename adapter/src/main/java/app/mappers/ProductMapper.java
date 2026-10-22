package app.mappers;

import app.dataPersistance.ProductEntity;
import app.dataPersistance.ProductOptionEntity;
import dto.ProductDto;
import dto.ProductOptionDto;
import model.Product;
import model.ProductOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductOption toPojo(ProductOptionEntity entity);

    @Mapping(target = "product", ignore = true)
    ProductOptionEntity toEntity(ProductOption option);

    ProductOptionDto toDto(ProductOption productOption);

    Set<ProductOptionDto> toDto(Set<ProductOption> options);

    ProductEntity toEntity(Product product);

    Product toPojo(ProductEntity entity);

    ProductDto toDto (Product product);
}
