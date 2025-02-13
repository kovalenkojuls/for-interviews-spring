package ru.kovalenkojuls.dataprojections.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kovalenkojuls.dataprojections.domain.Department;
import ru.kovalenkojuls.dataprojections.domain.Employee;
import ru.kovalenkojuls.dataprojections.projection.EmployeeProjection;
import ru.kovalenkojuls.dataprojections.repository.DepartmentRepository;
import ru.kovalenkojuls.dataprojections.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Optional<EmployeeProjection> getEmployeeProjectionById(Long employeeId) {
        return Optional.ofNullable(employeeRepository.findEmployeeProjectionById(employeeId));
    }

    public List<EmployeeProjection> getAllEmployeeProjections() {
        return employeeRepository.findAllEmployeeProjections();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee createEmployee(Employee employee, Long departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        if(department.isPresent()){
            employee.setDepartment(department.get());
            return employeeRepository.save(employee);
        } else {
            throw new RuntimeException("Department not found");
        }
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee, Long departmentId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        Optional<Department> department = departmentRepository.findById(departmentId);

        if (employeeOptional.isPresent() && department.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setFirstName(updatedEmployee.getFirstName());
            employee.setLastName(updatedEmployee.getLastName());
            employee.setPosition(updatedEmployee.getPosition());
            employee.setSalary(updatedEmployee.getSalary());
            employee.setDepartment(department.get());
            return employeeRepository.save(employee);
        } else {
            throw new RuntimeException("Employee or Department not found");
        }
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}

