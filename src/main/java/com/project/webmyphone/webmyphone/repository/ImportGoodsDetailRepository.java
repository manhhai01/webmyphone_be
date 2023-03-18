package com.project.webmyphone.webmyphone.repository;

import com.project.webmyphone.webmyphone.entity.ImportGoodsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportGoodsDetailRepository extends JpaRepository<ImportGoodsDetail, Long> {

}
