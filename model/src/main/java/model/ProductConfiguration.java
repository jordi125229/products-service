package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class ProductConfiguration {

    private Product product;
    private Set<ProductOption> selectedOptions;
}
