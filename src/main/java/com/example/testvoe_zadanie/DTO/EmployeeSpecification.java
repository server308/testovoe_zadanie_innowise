package com.example.testvoe_zadanie.DTO;

import com.example.testvoe_zadanie.models.Department;
import com.example.testvoe_zadanie.models.Employee;
import com.example.testvoe_zadanie.models.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> getEmployeeSpecification(EmployeeSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getFirstName() != null && !criteria.getFirstName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
            }
            if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
                Join<Employee, Status> statusJoin = root.join("status", JoinType.INNER);
                predicates.add(builder.equal(builder.lower(statusJoin.get("name")), criteria.getStatus().toLowerCase()));
            }
            if (criteria.getDepartmentNames() != null && !criteria.getDepartmentNames().isEmpty()) {
                Join<Employee, Department> departmentsJoin = root.join("departments", JoinType.INNER);
                CriteriaBuilder.In<String> inClause = builder.in(departmentsJoin.get("name"));
                criteria.getDepartmentNames().forEach(inClause::value);
                predicates.add(inClause);

            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}