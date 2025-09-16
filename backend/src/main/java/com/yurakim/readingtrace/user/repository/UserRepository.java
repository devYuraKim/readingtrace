package com.yurakim.readingtrace.user.repository;

import com.yurakim.readingtrace.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);

    List<User> findAll();

}
