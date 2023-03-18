package com.project.webmyphone.webmyphone.repository;

import com.project.webmyphone.webmyphone.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
