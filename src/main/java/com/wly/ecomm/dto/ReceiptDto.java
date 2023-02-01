package com.wly.ecomm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter @Builder
public class ReceiptDto {
    private final List<CartItemDto> cartItems;

    @JsonProperty("total_due")
    private Double totalDue;
}