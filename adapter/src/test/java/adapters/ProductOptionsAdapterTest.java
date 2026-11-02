package adapters;

import app.adapters.ProductOptionsAdapter;
import app.dataPersistance.ProductEntity;
import app.dataPersistance.ProductOptionEntity;
import app.dataPersistance.ProductOptionsRepository;
import app.dataPersistance.ProductRepository;
import app.mappers.ProductMapper;
import model.PagePojo;
import model.PageablePojo;
import model.ProductOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductOptionsAdapterTest {

    private ProductOptionsRepository repository;
    private ProductRepository productRepository;
    private ProductMapper mapper;
    private ProductOptionsAdapter adapter;

    @BeforeEach
    void setUp() {
        this.repository = Mockito.mock(ProductOptionsRepository.class);
        this.productRepository = Mockito.mock(ProductRepository.class);
        this.mapper = Mockito.mock(ProductMapper.class);
        this.adapter = new ProductOptionsAdapter(repository, productRepository, mapper);
    }

    @Test
    void findAllByProductId_DataCorrect_ProductOptionsForOneProductReturned() {
        // given
        ProductOptionEntity productOptionEntity = ProductOptionEntity.builder()
                .id(1L)
                .name("Phone")
                .quantity(10)
                .build();
        ProductOption productOption = ProductOption.builder()
                .id(1L)
                .name("Phone")
                .quantity(10)
                .build();
        long productId = 1L;
        Pageable pageable = PageRequest.of(1, 1);
        PageablePojo pageablePojo = new PageablePojo(1, 1);
        Page<ProductOptionEntity> page = new PageImpl<>(List.of(productOptionEntity), pageable, 1);
        when(repository.findAllByProductId(anyLong(), any(Pageable.class))).thenReturn(page);
        when(mapper.toPojo(any(ProductOptionEntity.class))).thenReturn(productOption);

        // when
        PagePojo<ProductOption> allByProductId = adapter.findAllByProductId(productId, pageablePojo);

        // then
        assertAll(
                () -> assertEquals(1, allByProductId.getContent().size()),
                () -> assertEquals(productOption, allByProductId.getContent().getFirst())
        );

        verify(repository).findAllByProductId(1L, pageable);
        verify(mapper).toPojo(productOptionEntity);
    }

    @Test
    void findById_DataCorrect_ProductOptionFound() {
        // given
        ProductOptionEntity productOptionEntity = ProductOptionEntity.builder()
                .id(1L)
                .name("Phone")
                .quantity(10)
                .build();
        ProductOption productOption = ProductOption.builder()
                .id(1L)
                .name("Phone")
                .quantity(10)
                .build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(productOptionEntity));
        when(mapper.toPojo(any(ProductOptionEntity.class))).thenReturn(productOption);

        // when
        Optional<ProductOption> productOptionById = adapter.findById(1L);

        // then
        assertAll(
                () -> assertNotNull(productOptionById),
                () -> assertEquals(productOption,productOptionById.get())
        );
        verify(mapper).toPojo(productOptionEntity);
    }

    @Test
    void save_DataCorrect_ProductOptionSavedWithProduct() {
        // given
        Long productId = 1L;

        ProductOption productOption = ProductOption.builder()
                .name("Phone")
                .quantity(10)
                .build();

        ProductOptionEntity productOptionEntity = ProductOptionEntity.builder()
                .name("Phone")
                .quantity(10)
                .build();

        ProductEntity productEntity = ProductEntity.builder()
                .id(productId)
                .build();

        ProductOptionEntity savedEntity = ProductOptionEntity.builder()
                .id(1L)
                .name("Phone")
                .quantity(10)
                .product(productEntity)
                .build();

        ProductOption expectedProductOption = ProductOption.builder()
                .id(1L)
                .name("Phone")
                .quantity(10)
                .build();

        when(mapper.toEntity(productOption)).thenReturn(productOptionEntity);
        when(productRepository.getReferenceById(productId)).thenReturn(productEntity);
        when(repository.save(productOptionEntity)).thenReturn(savedEntity);
        when(mapper.toPojo(savedEntity)).thenReturn(expectedProductOption);

        // when
        ProductOption result = adapter.save(productId, productOption);

        // then
        assertEquals(expectedProductOption, result);
        assertEquals(productEntity, productOptionEntity.getProduct());

        verify(mapper).toEntity(productOption);
        verify(productRepository).getReferenceById(productId);
        verify(repository).save(productOptionEntity);
        verify(mapper).toPojo(savedEntity);
    }

    @Test
    void save_DataCorrect_ProductOptionSaved() {
        // given
        ProductOption productOption = ProductOption.builder()
                .id(1L)
                .name("Phone")
                .quantity(10)
                .build();
        when(repository.save(productOption)).thenReturn(productOption);

        // when
        ProductOption result = adapter.save(productOption);

        // then
        assertEquals(productOption, result);
        verify(repository).save(productOption);
    }

    @Test
    void existsByProductIdAndSourcedProductId_DataCorrect_ReturnsTrue() {
        // given
        Long productId = 1L;
        Long linkedProductId = 2L;

        when(repository.existsByProductIdAndSourcedProductId(productId, linkedProductId))
                .thenReturn(true);

        // when
        boolean result = adapter.existsByProductIdAndSourcedProductId(productId, linkedProductId);

        // then
        assertTrue(result);
        verify(repository).existsByProductIdAndSourcedProductId(productId, linkedProductId);
    }
}
