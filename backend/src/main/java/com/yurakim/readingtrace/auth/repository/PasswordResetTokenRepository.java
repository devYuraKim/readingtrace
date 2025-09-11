package com.yurakim.readingtrace.auth.repository;

import com.yurakim.readingtrace.auth.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

}
