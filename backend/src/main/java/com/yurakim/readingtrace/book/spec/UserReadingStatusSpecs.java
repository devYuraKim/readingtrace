package com.yurakim.readingtrace.book.spec;

import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import org.springframework.data.jpa.domain.Specification;

public class UserReadingStatusSpecs {

    public static Specification<UserReadingStatus> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<UserReadingStatus> hasShelfId(Long shelfId){
        return (root, query, cb) -> cb.equal(root.get("shelfId"), shelfId);
    }

    public static Specification<UserReadingStatus> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<UserReadingStatus> hasVisibility(String visibility) {
        return (root, query, cb) -> cb.equal(root.get("visibility"), visibility);
    }

    public static Specification<UserReadingStatus> hasRating(Integer rating) {
        return (root, query, cb) -> cb.equal(root.get("rating"), rating);
    }
}