package com.wly.ecomm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class UserDTO {
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Last Name")
    private String lastName;
    @JsonProperty("First Name")
    private String firstName;

    @JsonProperty("Cart Summary")
    private ReceiptDto receiptDto;
}
