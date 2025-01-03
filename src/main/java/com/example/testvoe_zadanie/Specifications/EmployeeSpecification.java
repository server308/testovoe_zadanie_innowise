package com.example.testvoe_zadanie.Specifications;

import com.example.testvoe_zadanie.DTO.EmployeeSearchCriteria;
import com.example.testvoe_zadanie.models.Employee;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import com.example.testvoe_zadanie.models.Status;
import com.example.testvoe_zadanie.models.Department;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> getEmployeeSpecification(EmployeeSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("deleted"), false));

            if (criteria.getFirstName() != null && !criteria.getFirstName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
            }

            if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("lastName")), "%" + criteria.getLastName().toLowerCase() + "%"));
            }

            if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
                Join<Employee, Status> statusJoin = root.join("status", JoinType.INNER);
                predicates.add(builder.like(builder.lower(statusJoin.get("name")), "%" + criteria.getStatus().toLowerCase() + "%"));
            }

            if (criteria.getManagerName() != null && !criteria.getManagerName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("managerName")), "%" + criteria.getManagerName().toLowerCase() + "%"));
            }

            if (criteria.getDepartmentNames() != null && !criteria.getDepartmentNames().isEmpty()) {
                Join<Employee, Department> departmentJoin = root.join("departments", JoinType.INNER);
                CriteriaBuilder.In<String> inClause = builder.in(builder.lower(departmentJoin.get("name")));
                criteria.getDepartmentNames().forEach(name -> inClause.value(name.toLowerCase()));
                predicates.add(inClause);
            }
            query.distinct(true);
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}