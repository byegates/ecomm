package com.wly.ecomm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter @Builder
public class ReceiptDto {

    @JsonProperty("Cart Items")
    private final List<CartItemDto> cartItems;

    @JsonProperty("Total Due")
    private Double totalDue;
}