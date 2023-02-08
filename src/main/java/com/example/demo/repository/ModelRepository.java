package com.example.demo.repository;

import com.example.demo.models.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ModelRepository extends JpaRepository<Model,Long>{
    String findByModel(String model);
}
