package com.petshop.sales.application.service;

import com.petshop.sales.common.context.TenantContext;
import com.petshop.sales.common.exception.ResourceNotFoundException;
import com.petshop.sales.domain.model.Sale;
import com.petshop.sales.domain.model.SaleItem;
import com.petshop.sales.infrastructure.persistence.entity.SaleEntity;
import com.petshop.sales.infrastructure.persistence.entity.SaleItemEntity;
import com.petshop.sales.infrastructure.persistence.repository.SaleItemJpaRepository;
import com.petshop.sales.infrastructure.persistence.repository.SaleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleJpaRepository saleRepository;
    private final SaleItemJpaRepository saleItemRepository;

    @Transactional(readOnly = true)
    public List<Sale> findAll() {
        Long companyId = TenantContext.getCompanyId();
        return saleRepository.findAllByCompanyIdAndActiveTrueOrderByCreatedAtDesc(companyId).stream()
                .map(this::toDomainWithItems)
                .toList();
    }

    @Transactional(readOnly = true)
    public Sale findById(Long id) {
        Long companyId = TenantContext.getCompanyId();
        SaleEntity entity = saleRepository.findByIdAndCompanyIdAndActiveTrue(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", id));
        return toDomainWithItems(entity);
    }

    @Transactional
    public Sale create(Sale sale) {
        Long companyId = TenantContext.getCompanyId();
        SaleEntity entity = toEntity(sale);
        entity.setCompanyId(companyId);
        entity.setInvoiceNumber(generateInvoiceNumber(companyId));
        entity.setStatus("COMPLETED");
        SaleEntity saved = saleRepository.save(entity);

        List<SaleItemEntity> itemEntities = sale.getItems().stream()
                .map(item -> {
                    SaleItemEntity itemEntity = toItemEntity(item);
                    itemEntity.setSaleId(saved.getId());
                    itemEntity.setCompanyId(companyId);
                    return itemEntity;
                })
                .toList();
        saleItemRepository.saveAll(itemEntities);

        return toDomainWithItems(saved);
    }

    @Transactional(readOnly = true)
    public List<Sale> findByCustomerId(Long customerId) {
        Long companyId = TenantContext.getCompanyId();
        return saleRepository.findByCompanyIdAndCustomerIdAndActiveTrueOrderByCreatedAtDesc(companyId, customerId).stream()
                .map(this::toDomainWithItems)
                .toList();
    }

    @Transactional(readOnly = true)
    public Sale findByInvoiceNumber(String invoiceNumber) {
        Long companyId = TenantContext.getCompanyId();
        SaleEntity entity = saleRepository.findByCompanyIdAndInvoiceNumberAndActiveTrue(companyId, invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "invoiceNumber", invoiceNumber));
        return toDomainWithItems(entity);
    }

    @Transactional
    public void delete(Long id) {
        Long companyId = TenantContext.getCompanyId();
        SaleEntity existing = saleRepository.findByIdAndCompanyIdAndActiveTrue(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", id));
        existing.setActive(false);
        saleRepository.save(existing);
    }

    private String generateInvoiceNumber(Long companyId) {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = ThreadLocalRandom.current().nextInt(10000, 99999);
        return "INV-" + companyId + "-" + datePart + "-" + random;
    }

    private Sale toDomainWithItems(SaleEntity entity) {
        List<SaleItem> items = saleItemRepository.findBySaleIdAndCompanyId(entity.getId(), entity.getCompanyId()).stream()
                .map(this::toItemDomain)
                .toList();
        return toDomain(entity, items);
    }

    private Sale toDomain(SaleEntity entity, List<SaleItem> items) {
        return Sale.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .invoiceNumber(entity.getInvoiceNumber())
                .customerId(entity.getCustomerId())
                .totalAmount(entity.getTotalAmount())
                .taxAmount(entity.getTaxAmount())
                .discountAmount(entity.getDiscountAmount())
                .finalAmount(entity.getFinalAmount())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .items(items)
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private SaleEntity toEntity(Sale sale) {
        return SaleEntity.builder()
                .id(sale.getId())
                .companyId(sale.getCompanyId())
                .customerId(sale.getCustomerId())
                .totalAmount(sale.getTotalAmount())
                .taxAmount(sale.getTaxAmount())
                .discountAmount(sale.getDiscountAmount())
                .finalAmount(sale.getFinalAmount())
                .paymentMethod(sale.getPaymentMethod())
                .status(sale.getStatus())
                .build();
    }

    private SaleItem toItemDomain(SaleItemEntity entity) {
        return SaleItem.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .saleId(entity.getSaleId())
                .productSku(entity.getProductSku())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .subtotal(entity.getSubtotal())
                .build();
    }

    private SaleItemEntity toItemEntity(SaleItem item) {
        return SaleItemEntity.builder()
                .productSku(item.getProductSku())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
