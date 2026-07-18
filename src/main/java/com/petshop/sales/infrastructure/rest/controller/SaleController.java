package com.petshop.sales.infrastructure.rest.controller;

import com.petshop.sales.common.dto.ApiResponse;
import com.petshop.sales.application.service.SaleService;
import com.petshop.sales.domain.model.Sale;
import com.petshop.sales.domain.model.SaleItem;
import com.petshop.sales.infrastructure.rest.dto.sale.SaleItemResponse;
import com.petshop.sales.infrastructure.rest.dto.sale.SaleRequest;
import com.petshop.sales.infrastructure.rest.dto.sale.SaleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SaleResponse>>> findAll() {
        List<SaleResponse> responses = service.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleResponse>> findById(@PathVariable Long id) {
        SaleResponse response = toResponse(service.findById(id));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaleResponse>> create(@Valid @RequestBody SaleRequest request) {
        Sale sale = toDomain(request);
        SaleResponse response = toResponse(service.create(sale));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Sale created", response));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> findByCustomerId(@PathVariable Long customerId) {
        List<SaleResponse> responses = service.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/invoice/{invoiceNumber}")
    public ResponseEntity<ApiResponse<SaleResponse>> findByInvoiceNumber(@PathVariable String invoiceNumber) {
        SaleResponse response = toResponse(service.findByInvoiceNumber(invoiceNumber));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private Sale toDomain(SaleRequest request) {
        List<SaleItem> items = request.getItems().stream()
                .map(item -> SaleItem.builder()
                        .productSku(item.getProductSku())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getUnitPrice().multiply(new java.math.BigDecimal(item.getQuantity())))
                        .build())
                .toList();
        return Sale.builder()
                .customerId(request.getCustomerId())
                .totalAmount(request.getTotalAmount())
                .taxAmount(request.getTaxAmount())
                .discountAmount(request.getDiscountAmount())
                .finalAmount(request.getFinalAmount())
                .paymentMethod(request.getPaymentMethod())
                .items(items)
                .build();
    }

    private SaleResponse toResponse(Sale sale) {
        List<SaleItemResponse> itemResponses = sale.getItems() != null
                ? sale.getItems().stream().map(this::toItemResponse).toList()
                : List.of();
        return SaleResponse.builder()
                .id(sale.getId())
                .invoiceNumber(sale.getInvoiceNumber())
                .customerId(sale.getCustomerId())
                .totalAmount(sale.getTotalAmount())
                .taxAmount(sale.getTaxAmount())
                .discountAmount(sale.getDiscountAmount())
                .finalAmount(sale.getFinalAmount())
                .paymentMethod(sale.getPaymentMethod())
                .status(sale.getStatus())
                .items(itemResponses)
                .active(sale.isActive())
                .createdAt(sale.getCreatedAt())
                .updatedAt(sale.getUpdatedAt())
                .build();
    }

    private SaleItemResponse toItemResponse(SaleItem item) {
        return SaleItemResponse.builder()
                .id(item.getId())
                .saleId(item.getSaleId())
                .productSku(item.getProductSku())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
