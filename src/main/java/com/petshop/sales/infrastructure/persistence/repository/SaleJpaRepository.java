package com.petshop.sales.infrastructure.persistence.repository;

import com.petshop.sales.infrastructure.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleJpaRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findAllByActiveTrueOrderByCreatedAtDesc();
    Optional<SaleEntity> findByIdAndActiveTrue(Long id);
    List<SaleEntity> findByCustomerIdAndActiveTrueOrderByCreatedAtDesc(Long customerId);
    Optional<SaleEntity> findByInvoiceNumberAndActiveTrue(String invoiceNumber);
    boolean existsByInvoiceNumber(String invoiceNumber);

    List<SaleEntity> findAllByCompanyIdAndActiveTrueOrderByCreatedAtDesc(Long companyId);
    Optional<SaleEntity> findByIdAndCompanyIdAndActiveTrue(Long id, Long companyId);
    List<SaleEntity> findByCompanyIdAndCustomerIdAndActiveTrueOrderByCreatedAtDesc(Long companyId, Long customerId);
    Optional<SaleEntity> findByCompanyIdAndInvoiceNumberAndActiveTrue(Long companyId, String invoiceNumber);
    boolean existsByCompanyIdAndInvoiceNumber(Long companyId, String invoiceNumber);
}
