package com.yurakim.readingtrace.book.spec;

import com.yurakim.readingtrace.book.entity.UserBook;
import org.springframework.data.jpa.domain.Specification;

public class UserBookSpecs {
    public static Specification<UserBook> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<UserBook> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<UserBook> hasVisibility(String visibility) {
        return (root, query, cb) -> cb.equal(root.get("visibility"), visibility);
    }

    public static Specification<UserBook> hasRating(Integer rating) {
        return (root, query, cb) -> cb.equal(root.get("rating"), rating);
    }
}