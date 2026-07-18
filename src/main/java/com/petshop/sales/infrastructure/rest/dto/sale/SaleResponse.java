package com.petshop.sales.infrastructure.rest.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    private Long id;
    private Long companyId;
    private String invoiceNumber;
    private Long customerId;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String paymentMethod;
    private String status;
    private List<SaleItemResponse> items;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
