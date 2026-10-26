package model;

import java.math.BigDecimal;

public record Price(BigDecimal netPrice, BigDecimal grossPrice, Double taxRate) {

}
