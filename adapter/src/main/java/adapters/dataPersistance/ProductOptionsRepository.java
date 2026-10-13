package adapters.dataPersistance;

import adapters.dataPersistance.entities.ProductOptionEntity;
import model.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionsRepository extends JpaRepository<ProductOptionEntity, Long> {

    Page<ProductOptionEntity> findAllByProductId(Long productId, Pageable pageable);

    boolean existsByProductIdAndSourcedProductId(Long productId, Long linkedProductId);

    ProductOption save (ProductOption productOption);
}
