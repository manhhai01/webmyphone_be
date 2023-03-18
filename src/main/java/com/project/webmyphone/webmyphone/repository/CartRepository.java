package com.project.webmyphone.webmyphone.repository;

import com.project.webmyphone.webmyphone.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
