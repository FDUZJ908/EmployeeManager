package EmployeeManager.admin.repository;

import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;

import java.util.List;

/**
 * Created by 11437 on 2017/10/13.
 */
public interface EmployeeRepository {

    Employee get(String userid);

    List<Employee> list();

    List<Depart> getDepartmentList();

    List<Depart> list(String department);

    void add(String username,String deparmtnet,String isleader);

    void remove(String userid,String department);

    void update1(Employee employee);

    void update2(String userid,String department,String isleader);

    void createDep(String username,String department,String isleader);
}
