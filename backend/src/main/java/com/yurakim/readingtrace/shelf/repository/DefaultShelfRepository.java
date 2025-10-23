package com.yurakim.readingtrace.shelf.repository;

import com.yurakim.readingtrace.shelf.entity.DefaultShelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultShelfRepository extends JpaRepository<DefaultShelf, Long> {
}
