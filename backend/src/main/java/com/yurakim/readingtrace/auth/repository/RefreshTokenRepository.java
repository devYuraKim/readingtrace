package com.yurakim.readingtrace.auth.repository;

import com.yurakim.readingtrace.auth.entity.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    List<RefreshToken> findAllByUserId(Long userId);

}
