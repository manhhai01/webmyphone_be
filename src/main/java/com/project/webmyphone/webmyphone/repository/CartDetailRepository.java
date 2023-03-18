package com.project.webmyphone.webmyphone.repository;

import com.project.webmyphone.webmyphone.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

}
