package app.adapters;

import app.dataPersistance.ProductEntity;
import app.dataPersistance.ProductOptionEntity;
import app.dataPersistance.ProductOptionsRepository;
import app.dataPersistance.ProductRepository;
import app.mappers.ProductMapper;
import app.mappers.PageMapper;
import exceptions.ProductNotFoundException;
import exceptions.ProductQuantityException;
import exceptions.ProductsOptionQuantity;
import lombok.RequiredArgsConstructor;
import model.PagePojo;
import model.PageablePojo;
import model.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ports.ProductOptionsPort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductOptionsAdapter implements ProductOptionsPort {

    private final ProductOptionsRepository repository;
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public PagePojo<ProductOption> findAllByProductId(Long id, PageablePojo pageable) {
        Pageable springPageable = PageRequest.of(pageable.getPage(), pageable.getSize());
        Page<ProductOptionEntity> productsOptionsByProductId = repository.findAllByProductId(id, springPageable);
        return PageMapper.toPojo(productsOptionsByProductId, mapper::toPojo);
    }

    @Override
    public Optional<ProductOption> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toPojo);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ProductOption save(Long productId, ProductOption option) {
        ProductOptionEntity productOptionEntity = mapper.toEntity(option);
        ProductEntity productProxy = productRepository.getReferenceById(productId);
        productOptionEntity.setProduct(productProxy);
        ProductOptionEntity saved = repository.save(productOptionEntity);
        return mapper.toPojo(saved);
    }

    @Override
    public ProductOption save(ProductOption productOption) {
        return repository.save(productOption);
    }

    @Override
    public boolean existsByProductIdAndSourcedProductId(Long productId, Long linkedProductId) {
        return repository.existsByProductIdAndSourcedProductId(productId, linkedProductId);
    }
}
