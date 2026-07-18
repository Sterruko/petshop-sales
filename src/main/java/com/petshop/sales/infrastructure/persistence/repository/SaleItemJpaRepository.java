package com.petshop.sales.infrastructure.persistence.repository;

import com.petshop.sales.infrastructure.persistence.entity.SaleItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemJpaRepository extends JpaRepository<SaleItemEntity, Long> {
    List<SaleItemEntity> findBySaleId(Long saleId);
    List<SaleItemEntity> findBySaleIdAndCompanyId(Long saleId, Long companyId);
}
