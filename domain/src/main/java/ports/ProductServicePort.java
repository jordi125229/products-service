package ports;

import model.PagePojo;
import model.PageablePojo;
import model.Product;

public interface ProductServicePort {

    Product save(Product product);

    Product findById(Long id);

    void delete(Long id);

    PagePojo<Product> findAll(PageablePojo pageable);
}
