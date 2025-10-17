package com.yurakim.readingtrace.book.spec;

import com.yurakim.readingtrace.book.entity.UserBookStatus;
import org.springframework.data.jpa.domain.Specification;

public class UserBookStatusSpecs {

    public static Specification<UserBookStatus> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<UserBookStatus> hasShelfId(Long shelfId){
        return (root, query, cb) -> cb.equal(root.get("shelfId"), shelfId);
    }

    public static Specification<UserBookStatus> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<UserBookStatus> hasVisibility(String visibility) {
        return (root, query, cb) -> cb.equal(root.get("visibility"), visibility);
    }

    public static Specification<UserBookStatus> hasRating(Integer rating) {
        return (root, query, cb) -> cb.equal(root.get("rating"), rating);
    }
}