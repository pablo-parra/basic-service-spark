package org.example.client.dto;

import org.example.core.domain.Employee;

public record EmployeeDTO(
        String id,
        String firstName,
        String middleName,
        String lastName,
        int age,
        String department) {

    public Employee toDomain() {
        return new Employee(
                id,
                firstName,
                lastName,
                department
        );
    }
}
