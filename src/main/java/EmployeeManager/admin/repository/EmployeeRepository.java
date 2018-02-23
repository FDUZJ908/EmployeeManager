package EmployeeManager.admin.repository;

import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;
import EmployeeManager.admin.model.Privilege;

import java.util.List;

/**
 * Created by 11437 on 2017/10/13.
 */
public interface EmployeeRepository {

    Employee get(String userid);

    List<Employee> list();

    List<Depart> getDepartmentList();

    List<Privilege> getPrivilegeList();

    List<Employee> getEmployeeList();

    List<Depart> list(String department);

    void add(String username, String deparmtnet, String isleader);

    void removeEmp(String userid);

    void remove(String userid, String department);

    void remove(String department);

    void update2(String userid, String department, String isleader);

    int insertEmp(Employee employee);

    int updateEmp(Employee employee);

    int getDepartID(String dName);

    int insertDepEmps(String dName, String[] userids);

    int insertDep(String dName);

    int updateDep(int dID, String dName);

    int updateDepEmps(int dID, String dName, String[] userids);

}
