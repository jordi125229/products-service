package app.adapters;

import app.dataPersistance.ProductEntity;
import app.dataPersistance.ProductRepository;
import app.mappers.ProductMapper;
import app.mappers.PageMapper;
import lombok.RequiredArgsConstructor;
import model.PagePojo;
import model.PageablePojo;
import model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ports.ProductServicePort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductServiceAdapter implements ProductServicePort {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = repository.save(entity);
        return mapper.toPojo(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        ProductEntity productEntity = repository.findById(id).orElseThrow(() -> new NullPointerException());
        return Optional.of(mapper.toPojo(productEntity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public PagePojo<Product> findAll(PageablePojo pageable) {
        Pageable springPageable = PageRequest.of(pageable.getPage(), pageable.getSize());
        Page<ProductEntity> productPage = repository.findAll(springPageable);
        return PageMapper.toPojo(productPage, mapper::toPojo);
    }

    @Override
    public PagePojo<Product> findByProductsType(PageablePojo pageable, String productType) {
        Pageable springPageable = PageRequest.of(pageable.getPage(), pageable.getSize());
        List<Product> byProductType = repository.findByType(productType).stream()
                .map(mapper::toPojo)
                .toList();
        Page<ProductEntity> productPage = repository.findAll(springPageable);
        PagePojo<Product> pojoProductPage = PageMapper.toPojo(productPage, mapper::toPojo);
        pojoProductPage.setContent(byProductType);
        return pojoProductPage;
    }
}
