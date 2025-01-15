package com.example.testvoe_zadanie.Specifications;

import com.example.testvoe_zadanie.DTO.EmployeeSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import com.example.testvoe_zadanie.models.Employee;
import com.example.testvoe_zadanie.models.Status;
import com.example.testvoe_zadanie.models.Manager;


import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification implements Specification<Employee> {

    private EmployeeSearchCriteria criteria;

    // Конструктор, принимающий критерии поиска
    public EmployeeSpecification(EmployeeSearchCriteria criteria) {
        this.criteria = criteria;
    }

    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        // Фильтрация по имени сотрудника
        if (criteria.getFirstName() != null) {
            predicates.add(builder.like(builder.lower(root.get("firstName")), "%" + criteria.getFirstName().toLowerCase() + "%"));
        }

        // Фильтрация по фамилии сотрудника
        if (criteria.getLastName() != null) {
            predicates.add(builder.like(builder.lower(root.get("lastName")), "%" + criteria.getLastName().toLowerCase() + "%"));
        }

        // Фильтрация по email сотрудника
        if (criteria.getEmail() != null) {
            predicates.add(builder.like(builder.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
        }

        // Фильтрация по номеру телефона сотрудника
        if (criteria.getPhoneNumber() != null) {
            predicates.add(builder.like(builder.lower(root.get("phoneNumber")), "%" + criteria.getPhoneNumber().toLowerCase() + "%"));
        }

        // Фильтрация по ID менеджера
        if (criteria.getManagerId() != null) {
            predicates.add(builder.equal(root.get("manager").get("id"), criteria.getManagerId()));
        }

        // Фильтрация по статусу сотрудника
        if (criteria.getStatusId() != null) {
            predicates.add(builder.equal(root.get("status").get("id"), criteria.getStatusId()));
        }

        // Фильтрация по удаленности (soft delete)
        if (criteria.getDeleted() != null) {
            predicates.add(builder.equal(root.get("deleted"), criteria.getDeleted()));
        }

        // Фильтрация по имени менеджера
        if (criteria.getManagerName() != null) {
            predicates.add(builder.like(builder.lower(root.get("manager").get("name")), "%" + criteria.getManagerName().toLowerCase() + "%"));
        }

// Фильтрация по названию отдела
        if (criteria.getDepartmentName() != null) {
            predicates.add(builder.like(builder.lower(root.get("department").get("name")), "%" + criteria.getDepartmentName().toLowerCase() + "%"));
        }


        // Возвращаем все условия в виде одного предиката
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
