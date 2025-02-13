package ru.kovalenkojuls.dataprojections.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kovalenkojuls.dataprojections.domain.Employee;
import ru.kovalenkojuls.dataprojections.projection.EmployeeProjection;
import ru.kovalenkojuls.dataprojections.service.EmployeeService;
import ru.kovalenkojuls.dataprojections.config.TestConfig;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(TestConfig.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllEmployees() throws Exception {
        EmployeeProjection projection = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Julia Kovalenko";
            }

            @Override
            public String getPosition() {
                return "Developer";
            }

            @Override
            public String getDepartmentName() {
                return "IT";
            }
        };

        when(employeeService.getAllEmployeeProjections()).thenReturn(Collections.singletonList(projection));
        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Julia Kovalenko"))
                .andExpect(jsonPath("$[0].position").value("Developer"))
                .andExpect(jsonPath("$[0].departmentName").value("IT"));
    }

    @Test
    void getEmployeeById() throws Exception {
        EmployeeProjection projection = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Julia Kovalenko";
            }

            @Override
            public String getPosition() {
                return "Developer";
            }

            @Override
            public String getDepartmentName() {
                return "IT";
            }
        };

        when(employeeService.getEmployeeProjectionById(1L)).thenReturn(Optional.of(projection));
        mockMvc.perform(get("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Julia Kovalenko"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.departmentName").value("IT"));

        when(employeeService.getEmployeeProjectionById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/employees/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Julia");
        employee.setLastName("Kovalenko");
        employee.setPosition("Developer");
        employee.setSalary(300000L);

        when(employeeService.createEmployee(any(Employee.class), anyLong())).thenReturn(employee);
        mockMvc.perform(post("/api/employees")
                        .param("departmentId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Julia"))
                .andExpect(jsonPath("$.lastName").value("Kovalenko"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.salary").value(300000L));

        when(employeeService.createEmployee(any(Employee.class), anyLong())).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/employees")
                        .param("departmentId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Julia");
        updatedEmployee.setLastName("Kovalenko");
        updatedEmployee.setPosition("Developer");
        updatedEmployee.setSalary(300000L);

        when(employeeService.updateEmployee(anyLong(), any(Employee.class), anyLong())).thenReturn(updatedEmployee);
        mockMvc.perform(put("/api/employees/1")
                        .param("departmentId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Julia"))
                .andExpect(jsonPath("$.lastName").value("Kovalenko"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.salary").value(300000L));

        when(employeeService.updateEmployee(anyLong(), any(Employee.class), anyLong())).thenThrow(new RuntimeException());
        mockMvc.perform(put("/api/employees/1")
                        .param("departmentId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);
        mockMvc.perform(delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
