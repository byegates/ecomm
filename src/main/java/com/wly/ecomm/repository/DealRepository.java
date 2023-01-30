package com.wly.ecomm.repository;

import com.wly.ecomm.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Integer> {
}
