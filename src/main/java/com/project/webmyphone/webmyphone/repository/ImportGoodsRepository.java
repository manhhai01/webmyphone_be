package com.project.webmyphone.webmyphone.repository;

import com.project.webmyphone.webmyphone.entity.ImportGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportGoodsRepository extends JpaRepository<ImportGoods, Long> {

}
