package com.example.project_3.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.function.BiFunction;

public interface BaseSpecification {
    static <T> Specification<T> buildSpecification(Map<String, String> searchParams, BiFunction<String, String, Specification<T>> specFunction, boolean and) {
        Specification<T> specification = null;
        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isBlank()) {
                Specification<T> spec = specFunction.apply(field, value);
                if (spec == null) {
                    continue;
                }
                if (specification == null) {
                    specification = spec;
                } else if (and) {
                    specification = spec.and(specification);
                } else {
                    specification = spec.or(specification);
                }
            }
        }
        return specification;
    }

    static <T> Specification<T> buildLikeSpecification(Map<String, String> searchParams, boolean and) {
        return buildSpecification(searchParams, BaseSpecification::attributeLike, and);
    }

    static <T> Specification<T> buildEqualsSpecification(Map<String, String> searchParams, boolean and) {
        return buildSpecification(searchParams, BaseSpecification::attributeEquals, and);
    }

    static <T> Specification<T> attributeLike(String field, String value) {
        if (field.equals("page")) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(field).as(String.class), "%" + value + "%");
    }

    static <T> Specification<T> attributeEquals(String field, String value) {
        if (field.equals("page")) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value);
    }
}
