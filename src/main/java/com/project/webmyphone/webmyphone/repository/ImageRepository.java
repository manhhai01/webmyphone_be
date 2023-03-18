package com.project.webmyphone.webmyphone.repository;

import com.project.webmyphone.webmyphone.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
