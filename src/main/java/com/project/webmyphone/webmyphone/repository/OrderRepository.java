package com.project.webmyphone.webmyphone.repository;

import com.project.webmyphone.webmyphone.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
