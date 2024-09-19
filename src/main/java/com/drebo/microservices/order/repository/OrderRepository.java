package com.drebo.microservices.order.repository;

import com.drebo.microservices.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
