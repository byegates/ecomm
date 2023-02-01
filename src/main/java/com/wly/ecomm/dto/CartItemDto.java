package com.wly.ecomm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class CartItemDto {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("deal_applied")
    private String dealApplied;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("price_after_discount")
    private Double priceAfterDiscount;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("subtotal")
    private Double subtotal;
}
