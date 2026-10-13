package service;

import command.ActualizeProductCommand;
import command.CreateProductCommand;
import command.CreateProductOptionCommand;
import exceptions.ProductServiceException;
import lombok.RequiredArgsConstructor;
import model.*;
import model.ProductOption;
import ports.ProductOptionsPort;
import ports.ProductServicePort;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductService {

    private final ProductServicePort productServicePort;
    private final ProductOptionsPort productOptionsPort;

    public Product createProduct(CreateProductCommand createProductCommand) {
        Price price = new Price(createProductCommand.getNetPrice(), createProductCommand.getGrossPrice(), createProductCommand.getTaxRate());
        if (createProductCommand.getQuantity() == null) {
            createProductCommand.setQuantity(0);
        }
        Product product = new Product(createProductCommand.getName(), price, createProductCommand.getType(), createProductCommand.getQuantity());
        return productServicePort.save(product);
    }

    public Product actualizeProduct(Long productId, ActualizeProductCommand command) {
        Product product = productServicePort.findById(productId);
        if (command.getName() != null) {
            product.setName(command.getName());
        }
        if (command.getQuantity() != null) {
            product.setQuantity(command.getQuantity());
        }
        if (command.getType() != null) {
            product.setType(command.getType());
        }
        if (command.getNetPrice() != null && command.getGrossPrice() != null && command.getTaxRate() != null) {
            Price price = new Price(command.getNetPrice(), command.getGrossPrice(), command.getTaxRate());
            product.setPrice(price);
        }
        return productServicePort.save(product);
    }

    public ProductOption addOption(Long productId, CreateProductOptionCommand command) {
        Price price = new Price(command.getNetPrice(), command.getGrossPrice(), command.getTaxRate());
        ProductOption option = new ProductOption(command.getName(), price, command.getType(), command.getQuantity());
        Product product = productServicePort.findById(productId);
        if (option.getSourcedProductId() != null) {
            productServicePort.save(product);
        } else {
            product.setQuantity(product.getQuantity() + command.getQuantity());
            productServicePort.save(product);
        }
        return productOptionsPort.save(productId, option);
    }

    public ProductOption addProductAsConfiguration(Long productId, Long productWhichBeConfigure) {
        Product product = productServicePort.findById(productId);
        if (productOptionsPort.existsByProductIdAndSourcedProductId(productWhichBeConfigure, productId)) {
            throw new IllegalStateException("Ten produkt jest już konfiguracją tego produktu");
        }
        ProductOption option = new ProductOption(product.getName(), product.getPrice(), product.getType(), product.getQuantity());
        option.setSourcedProductId(productId);
        return productOptionsPort.save(productWhichBeConfigure, option);
    }

    public void delete(Long id) {
        productServicePort.delete(id);
    }

    public PagePojo<Product> getProducts(int pageNumber, int pageSize) {
        PageablePojo pageable = new PageablePojo(pageSize, pageNumber);
        return productServicePort.findAll(pageable);
    }

    public PagePojo<ProductOption> getOptions(int pageNumber, int pageSize, Long id) {
        PageablePojo pageable = new PageablePojo(pageSize, pageNumber);
        return productOptionsPort.findAllByProductId(id, pageable);
    }

    public ProductConfiguration configureProduct(Long productId, Set<Long> optionIds, int quantity) {
        Product product = productServicePort.findById(productId);
        if (product.getQuantity() < quantity) {
            throw new ProductServiceException("Insufficient quantity of the product");
        }
//        product.setQuantity(product.getQuantity() - quantity);
//        productServicePort.save(product);
        Product productToCart = new Product(product.getName(), product.getPrice(), product.getType(), quantity);
        Set<ProductOption> options = optionIds.stream()
                .map(productOptionsPort::findById)
                .collect(Collectors.toSet());
        return new ProductConfiguration(productToCart, options);
    }
}
