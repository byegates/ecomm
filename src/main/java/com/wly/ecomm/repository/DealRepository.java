package com.wly.ecomm.repository;

import com.wly.ecomm.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Integer> {
    @Query("select d from Deal d where d.name like %:code%")
    List<Deal> findPartiallyByNameJPQL(@Param("code") String dealCode);

    @Query(value = "select * from deal d where d.name like %?1%", nativeQuery = true)
    List<Deal> findPartiallyByNameNative(String dealCode);
}
