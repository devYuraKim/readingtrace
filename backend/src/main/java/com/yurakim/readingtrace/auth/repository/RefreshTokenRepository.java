package com.yurakim.readingtrace.auth.repository;

import com.yurakim.readingtrace.auth.entity.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    List<RefreshToken> findAllByUserId(Long userId);

    Optional<RefreshToken> findByTokenHash(String refreshToken);
}
