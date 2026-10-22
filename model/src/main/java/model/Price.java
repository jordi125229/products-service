package model;

import java.math.BigDecimal;

public record Price(BigDecimal netPrice, BigDecimal grossPrice, Double taxRate) {
    public Price multiply(int quantity) {
        BigDecimal multiplier = BigDecimal.valueOf(quantity);
        return new Price(netPrice.multiply(multiplier), grossPrice.multiply(multiplier), taxRate);
    }
}
