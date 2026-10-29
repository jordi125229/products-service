package service;

import command.ActualizeProductCommand;
import command.CreateProductCommand;
import command.CreateProductOptionCommand;
import exceptions.ProductNotFoundException;
import exceptions.ProductQuantityException;
import exceptions.ProductsOptionQuantity;
import exceptions.UserIsNotTheOwnerException;
import lombok.RequiredArgsConstructor;
import model.*;
import model.ProductOption;
import ports.ProductOptionsPort;
import ports.ProductServicePort;

import java.util.Collections;
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
        Product product = new Product(createProductCommand.getName(), price, createProductCommand.getType(), createProductCommand.getQuantity(), createProductCommand.getOwnerId());
        return productServicePort.save(product);
    }

    public Product actualizeProduct(Long productId, ActualizeProductCommand command, String userId) {
        Product product = productServicePort.findById(productId).orElseThrow(() -> new ProductNotFoundException("Can't find product"));
        if (!product.getOwnerId().equals(userId)) {
            throw new UserIsNotTheOwnerException("User is not the owner of this product");
        }
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
        Product product = productServicePort.findById(productId).orElseThrow(() -> new NullPointerException("Can't find product"));
        if (option.getSourcedProductId() != null) {
            productServicePort.save(product);
        } else {
            product.setQuantity(product.getQuantity() + command.getQuantity());
            productServicePort.save(product);
        }
        return productOptionsPort.save(productId, option);
    }

    public ProductOption addProductAsConfiguration(Long productId, Long productWhichBeConfigure) {
        Product product = productServicePort.findById(productId).orElseThrow(() -> new ProductNotFoundException("Can't find product"));
        if (productOptionsPort.existsByProductIdAndSourcedProductId(productWhichBeConfigure, productId)) {
            throw new ProductsOptionQuantity("This product is already configuration of the product.");
        }
        ProductOption option = new ProductOption(product.getName(), product.getPrice(), product.getType(), product.getQuantity());
        option.setSourcedProductId(productId);
        return productOptionsPort.save(productWhichBeConfigure, option);
    }

    public void delete(Long id, String userId) {
        Product product = productServicePort.findById(id).orElseThrow(() -> new ProductNotFoundException("Can't find product"));
        if (!product.getOwnerId().equals(userId)) {
            throw new UserIsNotTheOwnerException("User is not the owner of this product");
        }
        productServicePort.delete(id);
    }

    public void deleteOption(Long optionId, String userId) {
        ProductOption byId = productOptionsPort.findById(optionId);
        Product product = productServicePort.findById(byId.getSourcedProductId()).orElseThrow(() -> new ProductNotFoundException("Can't find product"));
        if (!product.getOwnerId().equals(userId)) {
            throw new UserIsNotTheOwnerException("User is not the owner of this product");
        }
        productOptionsPort.deleteById(optionId);
    }

    public PagePojo<Product> getProducts(String productType, int pageNumber, int pageSize) {
        PageablePojo pageable = new PageablePojo(pageSize, pageNumber);
        if (productType == null || productType.isBlank()) {
            return productServicePort.findAll(pageable);
        }
        return productServicePort.findByProductsType(pageable, productType);
    }

    public PagePojo<ProductOption> getOptions(int pageNumber, int pageSize, Long id) {
        PageablePojo pageable = new PageablePojo(pageSize, pageNumber);
        return productOptionsPort.findAllByProductId(id, pageable);
    }

    public ProductConfiguration configureProduct(Long productId, Set<Long> optionIds, int quantity) {
        if (quantity <= 0) {
            throw new ProductQuantityException("Quantity must be greater than zero");
        }
        Product product = productServicePort.findById(productId).orElseThrow(() -> new ProductNotFoundException("Can't find product"));
        if (product.getQuantity() < quantity) {
            throw new ProductQuantityException("Insufficient quantity of the product");
        }
        Product productToCart = new Product(product.getName(), product.getPrice(), product.getType(), quantity);
        Set<Long> optionIdsNotNull = (optionIds != null) ? optionIds : Collections.emptySet();
        Set<ProductOption> options = optionIdsNotNull.stream()
                .map(productOptionsPort::findById)
                .collect(Collectors.toSet());
        return new ProductConfiguration(productToCart, options);
    }

//    public void decreaseStock(Long productId, int quantity) {
//        if (quantity <= 0) {
//            throw new ProductServiceException("Quantity must be greater than zero");
//        }
//        Product productById = productServicePort.findById(productId).orElseThrow(() -> new NullPointerException("Can't find product"));
//        if (productById.getQuantity() < quantity) {
//            throw new ProductServiceException("Insufficient quantity of the product");
//        }
//        productById.setQuantity(productById.getQuantity() - quantity);
//        productServicePort.save(productById);
//    } to jest metoda potrzebna do zmniajszania stanu magazynowego w momencie w ktorym zakup jest potwierdzony
}

