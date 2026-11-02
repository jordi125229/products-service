package app.dataPersistance;

import jakarta.persistence.*;
import lombok.*;
import model.Price;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "sourced_product_id"}))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sourcedProductId;
    private String name;
    @Embedded
    private Price price;
    private String type;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
