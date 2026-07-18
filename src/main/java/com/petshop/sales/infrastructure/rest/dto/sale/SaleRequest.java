package com.petshop.sales.infrastructure.rest.dto.sale;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;

    @NotNull(message = "Tax amount is required")
    private BigDecimal taxAmount;

    @NotNull(message = "Discount amount is required")
    private BigDecimal discountAmount;

    @NotNull(message = "Final amount is required")
    @Positive(message = "Final amount must be positive")
    private BigDecimal finalAmount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Sale items are required")
    @Valid
    private List<SaleItemRequest> items;
}
