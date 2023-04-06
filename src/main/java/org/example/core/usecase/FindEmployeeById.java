package org.example.core.usecase;

import org.example.core.domain.Employee;
import org.example.core.interactors.EmployeeService;

public class FindEmployeeById {
    public final EmployeeService service;

    public FindEmployeeById(EmployeeService service) {
        this.service = service;
    }

    public Employee execute(String id) {
        return this.service.getEmployee(id);
    }
}
