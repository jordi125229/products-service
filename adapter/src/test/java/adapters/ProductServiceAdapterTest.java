package adapters;

import app.adapters.ProductServiceAdapter;
import app.dataPersistance.ProductEntity;
import app.dataPersistance.ProductRepository;
import app.mappers.ProductMapper;
import exceptions.ProductNotFoundException;
import model.PagePojo;
import model.PageablePojo;
import model.Product;
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
import static org.mockito.Mockito.*;

public class ProductServiceAdapterTest {

    private ProductRepository repository;
    private ProductMapper mapper;
    private ProductServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        this.repository = Mockito.mock(ProductRepository.class);
        this.mapper = Mockito.mock(ProductMapper.class);
        this.adapter = new ProductServiceAdapter(repository, mapper);
    }

    @Test
    void save_DataCorrect_ProductSaved() {
        //given
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();
        when(mapper.toEntity(any(Product.class))).thenReturn(productEntity);
        when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(mapper.toPojo(any(ProductEntity.class))).thenReturn(product);

        // when
        Product savedProduct = adapter.save(product);

        // then
        assertEquals(product, savedProduct);
        verify(mapper).toEntity(product);
        verify(repository).save(productEntity);
        verify(mapper).toPojo(productEntity);
    }

    @Test
    void findById_IdCorrect_ProductFound() {
        // given
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(mapper.toPojo(productEntity)).thenReturn(product);

        // when
        Optional<Product> foundProduct = adapter.findById(1L);

        // then
        assertTrue(foundProduct.isPresent());
        assertEquals(product, foundProduct.get());
        verify(repository).findById(1L);
        verify(mapper).toPojo(productEntity);
    }

    @Test
    void findById_IdIncorrect_ProductNotFoundExceptionThrown() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ProductNotFoundException.class, () -> adapter.findById(1L));
        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void delete_IdCorrect_ProductDeleted() {
        // when
        adapter.delete(1L);

        // then
        verify(repository).deleteById(1L);
    }

    @Test
    void findAll_PageableCorrect_ProductsFound() {
        // given
        PageablePojo pageablePojo = new PageablePojo(1, 1);
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();

        Pageable pageable = PageRequest.of(1, 1);

        Page<ProductEntity> productPage = new PageImpl<>(List.of(productEntity), pageable, 0);

        when(repository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(mapper.toPojo(productEntity)).thenReturn(product);

        // when
        PagePojo<Product> result = adapter.findAll(pageablePojo);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(product, result.getContent().get(0));

        verify(repository).findAll(pageable);
        verify(mapper).toPojo(productEntity);
    }

    @Test
    void findAll_NoProducts_NoProductPageReturned() {
        // given
        PageablePojo pageablePojo = new PageablePojo(1, 1);
        Pageable pageable = PageRequest.of(1, 1);
        Page<ProductEntity> productPage = new PageImpl<>(List.of(), pageable, 0);
        when(repository.findAll(any(Pageable.class))).thenReturn(productPage);

        // when
        PagePojo<Product> result = adapter.findAll(pageablePojo);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());

        verify(repository).findAll(pageable);
        verifyNoInteractions(mapper);
    }

    @Test
    void findByProductsType_ProductTypeCorrect_ProductsFound() {
        // given
        String productType = "ELECTRONICS";
        PageablePojo pageablePojo = new PageablePojo(1, 1);
        Pageable pageable = PageRequest.of(1, 1);
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("name")
                .quantity(10)
                .build();

        Page<ProductEntity> productPage = new PageImpl<>(List.of(productEntity), pageable, 1);

        when(repository.findByType(productType, pageable)).thenReturn(productPage);
        when(mapper.toPojo(productEntity)).thenReturn(product);

        // when
        PagePojo<Product> result = adapter.findByProductsType(pageablePojo, productType);

        // then
        assertNotNull(result);
        assertAll(
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(product, result.getContent().get(0)));

        verify(repository).findByType(productType, pageable);
        verify(mapper).toPojo(productEntity);
    }

    @Test
    void findByProductsType_NoProducts_NoProductPageReturned() {
        // given
        String productType = "ELECTRONICS";
        PageablePojo pageablePojo = new PageablePojo(1, 1);
        Pageable pageable = PageRequest.of(1, 1);

        Page<ProductEntity> productPage = new PageImpl<>(List.of(), pageable, 1);
        when(repository.findByType(productType, pageable)).thenReturn(productPage);

        // when
        PagePojo<Product> result = adapter.findByProductsType(pageablePojo, productType);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());

        verify(repository).findByType(productType, pageable);
        verifyNoInteractions(mapper);
    }
}
