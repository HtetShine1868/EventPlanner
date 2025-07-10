package com.project.EventPlanner.features.event.domain;

import com.project.EventPlanner.features.event.domain.model.Event;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecifiaction{

    public static Specification<Event> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };
    }

    public static Specification<Event> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Event> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) return null;
            return cb.equal(cb.upper(root.get("status")), status.toUpperCase());
        };
    }
}
