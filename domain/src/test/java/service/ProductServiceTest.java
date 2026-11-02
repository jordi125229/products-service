package service;

import command.ActualizeProductCommand;
import command.CreateProductCommand;
import command.CreateProductOptionCommand;
import model.Price;
import model.Product;
import model.ProductOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ports.ProductOptionsPort;
import ports.ProductServicePort;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private ProductServicePort productServicePort;
    private ProductOptionsPort productOptionsPort;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        this.productServicePort = Mockito.mock(ProductServicePort.class);
        this.productOptionsPort = Mockito.mock(ProductOptionsPort.class);
        this.productService = new ProductService(productServicePort, productOptionsPort);
    }

    @Test
    void createProduct_DataCorrect_ProductCreated() {
        // given
        Product product = Product.builder()
                .name("productName")
                .type("productType")
                .quantity(10)
                .build();
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .name("productName")
                .type("productType")
                .quantity(10)
                .build();
        when(productServicePort.save(any(Product.class))).thenReturn(product);

        // when
        Product createdProduct = productService.createProduct(createProductCommand);

        // then
        assertAll(
                () -> assertEquals("productName", createdProduct.getName()),
                () -> assertEquals("productType", createdProduct.getType()),
                () -> assertEquals(10, createdProduct.getQuantity())
        );
    }

    @Test
    void actualizeProduct_DataCorrect_ProductActualized() {
        // given
        Product product = Product.builder()
                .id(1L)
                .ownerId("user")
                .name("name")
                .quantity(10)
                .build();
        ActualizeProductCommand command = ActualizeProductCommand.builder()
                .name("newName")
                .quantity(20)
                .build();
        when(productServicePort.findById(anyLong())).thenReturn(Optional.of(product));
        when(productServicePort.save(product)).thenReturn(product);

        // when
        Product productAfterChange = productService.actualizeProduct(1L, command, "user");

        // then
        assertAll(
                () -> assertEquals(20, productAfterChange.getQuantity()),
                () -> assertEquals("newName", productAfterChange.getName())
        );
    }

    @Test
    void addOption_DataCorrect_ProductAdded() {
        // given
        Product product = Product.builder()
                .id(1L)
                .ownerId("user")
                .name("name")
                .quantity(10)
                .build();
        CreateProductOptionCommand command = CreateProductOptionCommand.builder()
                .name("optionName")
                .netPrice(BigDecimal.valueOf(100))
                .taxRate(0.23)
                .grossPrice(BigDecimal.valueOf(123))
                .type("Processor")
                .value("processor")
                .quantity(10)
                .build();
        Price price = Price.builder()
                .netPrice(BigDecimal.valueOf(100))
                .taxRate(0.23)
                .grossPrice(BigDecimal.valueOf(123))
                .build();
        ProductOption productOption = ProductOption.builder()
                .name("optionName")
                .type("Processor")
                .price(price)
                .quantity(10)
                .build();
        when(productServicePort.findById(anyLong())).thenReturn(Optional.of(product));
        when(productServicePort.save(any())).thenReturn(product);
        when(productOptionsPort.save(any(), any())).thenReturn(productOption);

        // when
        ProductOption productOptionCreated = productService.addOption(1L, command);

        // then
        assertAll(
                () -> assertEquals("optionName", productOptionCreated.getName()),
                () -> assertEquals("Processor", productOptionCreated.getType()),
                () -> assertEquals(price, productOptionCreated.getPrice()),
                () -> assertEquals(10, productOptionCreated.getQuantity())
        );
    }

    @Test
    void addProductAsConfiguration_DataCorrect_ProductAddedAsConfiguration() {
        // given
        Product product = Product.builder()
                .id(1L)
                .ownerId("user")
                .name("name")
                .quantity(10)
                .build();
        Price price = Price.builder()
                .netPrice(BigDecimal.valueOf(100))
                .taxRate(0.23)
                .grossPrice(BigDecimal.valueOf(123))
                .build();
        ProductOption productOption = ProductOption.builder()
                .name("optionName")
                .type("Processor")
                .price(price)
                .quantity(10)
                .build();
        Long productAsConfiguration = 2L;
        when(productServicePort.findById(anyLong())).thenReturn(Optional.of(product));
        when(productOptionsPort.existsByProductIdAndSourcedProductId(anyLong(), anyLong())).thenReturn(false);
        when(productOptionsPort.save(any(), any())).thenReturn(productOption);

        // when
        ProductOption productAsOption = productService.addProductAsConfiguration(productAsConfiguration, 1L);

        // then
        assertAll(
                () -> assertEquals("optionName", productAsOption.getName()),
                () -> assertEquals("Processor", productAsOption.getType()),
                () -> assertEquals(price, productAsOption.getPrice())
        );
    }

    @Test
    void delete_DataCorrect_ProductDeleted(){
        //given
        Product product = Product.builder()
                .id(1L)
                .ownerId("user")
                .name("name")
                .quantity(10)
                .build();
        when(productServicePort.findById(anyLong())).thenReturn(Optional.of(product));

        // when
        productService.delete(1L,"user");

        // then
        verify(productServicePort).delete(anyLong());
    }
}
