package ports;

import model.PagePojo;
import model.PageablePojo;
import model.Product;

import java.util.Optional;

public interface ProductServicePort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    void delete(Long id);

    PagePojo<Product> findAll(PageablePojo pageable);

    PagePojo<Product> findByProductsType(PageablePojo pageable, String productType);
}
