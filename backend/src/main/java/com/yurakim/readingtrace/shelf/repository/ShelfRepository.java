package com.yurakim.readingtrace.shelf.repository;

import com.yurakim.readingtrace.shelf.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    List<Shelf> findByUserId(Long userId);

    Shelf findByUserIdAndSlug(Long userId, String slug);

}
