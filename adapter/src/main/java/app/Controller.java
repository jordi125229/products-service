package app;

import app.mappers.ProductMapper;
import app.mappers.PageMapper;
import app.mappers.ProductConfigurationMapper;
import command.ActualizeProductCommand;
import command.CreateProductOptionCommand;
import dto.PageableDto;
import dto.ProductConfigurationDto;
import dto.ProductDto;
import dto.ProductOptionDto;
import lombok.RequiredArgsConstructor;
import command.CreateProductCommand;
import model.PagePojo;
import model.Product;
import model.ProductConfiguration;
import model.ProductOption;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import service.ProductService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class Controller {

    private final ProductService service;
    private final ProductMapper productMapper;
    private final ProductConfigurationMapper productConfigurationMapper;

    @GetMapping
    public PageableDto<ProductDto> getProducts(@RequestParam(required = false) String productsType, @RequestParam int page, @RequestParam int size) {
        PagePojo<Product> products = service.getProducts(productsType, page, size);
        return PageMapper.toDto(products, productMapper::toDto);
    }

    @GetMapping("/options/{id}")
    PageableDto<ProductOptionDto> getOptions(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
        PagePojo<ProductOption> options = service.getOptions(page, size, id);
        return PageMapper.toDto(options, productMapper::toDto);
    }

    @PatchMapping("/update/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, @RequestBody ActualizeProductCommand command, @AuthenticationPrincipal Jwt jwt){
        Product product = service.actualizeProduct(productId, command, jwt.getSubject());
        return productMapper.toDto(product);
    }

    @PostMapping("/option-assign/{productId}/{productQuantity}")
    public ProductConfigurationDto chooseOption(@PathVariable Long productId, @PathVariable int productQuantity, @RequestParam(required = false) Set<Long> optionIds) {
        ProductConfiguration productOption = service.configureProduct(productId, optionIds, productQuantity);
        return productConfigurationMapper.toDto(productOption);
    }

    @PostMapping("/new")
    public ProductDto createProduct(@RequestBody CreateProductCommand command, @AuthenticationPrincipal Jwt jwt) {
        command.setOwnerId(jwt.getSubject());
        Product product = service.createProduct(command);
        return productMapper.toDto(product);
    }

//    @PostMapping("/stock/{productId}/{quantity}")
//    public void decreaseStock(@PathVariable Long productId, @PathVariable int quantity) {
//        service.decreaseStock(productId, quantity);  // do wywolania w serwisie order ale dopiero w momencie gdy zamowienie jest oplacone
//    }

    @PostMapping("/{productId}/options")
    public ProductOptionDto addOption(@PathVariable Long productId, @RequestBody CreateProductOptionCommand command) {
        ProductOption option = service.addOption(productId, command);
        return productMapper.toDto(option);
    }

    @PostMapping("/{configurationProductId}/configurations/{productWhichIsBeingConfiguredId}")
    public ProductOptionDto addProductAsConfiguration(@PathVariable Long configurationProductId, @PathVariable Long productWhichIsBeingConfiguredId) {
        ProductOption productOption = service.addProductAsConfiguration(configurationProductId, productWhichIsBeingConfiguredId);
        return productMapper.toDto(productOption);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        service.delete(id, jwt.getSubject());
    }

    @DeleteMapping("/option/{id}")
    public void deleteProductsOption(@PathVariable Long optionId, @AuthenticationPrincipal Jwt jwt){
        service.deleteOption(optionId, jwt.getSubject());
    }
}
