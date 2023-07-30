package ru.davydenko.spring.rest.dao;



import ru.davydenko.spring.rest.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    public Employee getEmployee(int id);

    public List<Employee> getAllEmployees();

   public void saveEmployee(Employee employee);

    public void deleteEmployee(int id);
}
