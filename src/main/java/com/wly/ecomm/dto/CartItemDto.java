package com.wly.ecomm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class CartItemDto {
    @JsonProperty("Product Name")
    private String productName;

    @JsonProperty("Deal Applied")
    private String dealApplied;

    @JsonProperty("Price")
    private Double price;

    @JsonProperty("Price After Discount")
    private Double priceAfterDiscount;

    @JsonProperty("Quantity")
    private Integer quantity;

    @JsonProperty("Subtotal")
    private Double subtotal;
}
