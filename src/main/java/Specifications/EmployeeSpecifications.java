package Specifications;

import jakarta.persistence.criteria.Predicate;
import models.Employee;
import models.EmployeeStatus;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecifications {
    public static Specification<Employee> byFilters(String name, EmployeeStatus status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

