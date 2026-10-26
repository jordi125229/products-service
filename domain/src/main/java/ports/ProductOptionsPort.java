package ports;

import model.PagePojo;
import model.PageablePojo;
import model.ProductOption;

import java.util.Optional;

public interface ProductOptionsPort {

    PagePojo<ProductOption> findAllByProductId(Long productId, PageablePojo pageable);

    ProductOption findById(Long id);

    void deleteById(Long id);

    ProductOption save(Long productId, ProductOption option);

    ProductOption save(ProductOption productOption);

    boolean existsByProductIdAndSourcedProductId(Long productId, Long sourceProductId);
}
