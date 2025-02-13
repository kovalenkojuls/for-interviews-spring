package ru.kovalenkojuls.dataprojections.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kovalenkojuls.dataprojections.domain.Employee;
import ru.kovalenkojuls.dataprojections.projection.EmployeeProjection;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e.firstName || ' ' || e.lastName AS fullName, e.position AS position, d.name AS departmentName " +
            "FROM Employee e " +
            "LEFT JOIN e.department d " +
            "WHERE e.id = :employeeId")
    EmployeeProjection findEmployeeProjectionById(@Param("employeeId") Long employeeId);

    @Query("SELECT e.firstName || ' ' || e.lastName AS fullName, e.position AS position, d.name AS departmentName " +
            "FROM Employee e " +
            "LEFT JOIN e.department d")
    List<EmployeeProjection> findAllEmployeeProjections();
}
