package org.example.rest.dto;

import org.example.core.domain.Employee;

public record EmployeeDTO(
        String id,
        String firstName,
        String lastName,
        String department) {

    public static EmployeeDTO fromDomain(Employee employee) {
        return new EmployeeDTO(
                employee.id,
                employee.firstName,
                employee.lastName,
                employee.department
        );
    }

}
