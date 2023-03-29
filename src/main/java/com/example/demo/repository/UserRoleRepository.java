package com.example.demo.repository;

import com.example.demo.models.entity.UserRole;
import com.example.demo.models.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    Optional<UserRole> findUserRoleByUserRole(UserRoleEnum roleEnum);
}
