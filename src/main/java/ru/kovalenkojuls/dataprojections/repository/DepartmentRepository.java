package ru.kovalenkojuls.dataprojections.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kovalenkojuls.dataprojections.domain.Department;

public interface DepartmentRepository  extends JpaRepository<Department, Long> {
}
