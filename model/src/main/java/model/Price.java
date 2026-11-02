package model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Price(BigDecimal netPrice, BigDecimal grossPrice, Double taxRate) {

}
