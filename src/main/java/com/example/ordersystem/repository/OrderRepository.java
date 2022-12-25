package com.example.ordersystem.repository;

import com.example.ordersystem.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE orders SET status = :updateStatus, updated_time = CURRENT_TIMESTAMP WHERE id = :id AND status = :currentStatus", nativeQuery = true)
    int setOrderStatusById(@Param("id") String id, @Param("currentStatus") String currentStatus, @Param("updateStatus") String updateStatus);

    @Query(value = "SELECT * FROM orders ORDER BY created_time DESC LIMIT :offset,:limit", nativeQuery = true)
    List<Order> findAll(@Param("offset") int offset, @Param("limit") int limit);
}
