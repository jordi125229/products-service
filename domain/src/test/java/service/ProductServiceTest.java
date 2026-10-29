package service;

import command.CreateProductCommand;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ports.ProductOptionsPort;
import ports.ProductServicePort;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    }

}
