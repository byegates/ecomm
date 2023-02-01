package com.wly.ecomm.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data  @AllArgsConstructor  @NoArgsConstructor
@Embeddable
public class CartItemId implements Serializable {

    private UUID userId;

    private Integer productId;
}
