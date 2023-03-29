package com.example.demo.repository;

import com.example.demo.models.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
