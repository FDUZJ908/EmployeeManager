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

    List<Depart> list(int dID);

    List<Depart> getDepartmentList();

    List<Depart> getDepartmentList(String userID);

    List<Privilege> getPrivilegeList();

    void removeEmp(String userid);

    void removeEmpDep(String userid, int dID);

    void removeDep(String department);

    void updateLeader(String userid, int dID, int isleader);

    void updateLeaders(int dID, String[] leaders);

    int insertEmp(Employee employee);

    int updateEmp(Employee employee);

    int getDepartID(String dName);

    int insertDepEmps(String dName, String[] userids);

    int insertEmpDeps(String userid, String[] departs);

    int insertDep(String dName);

    int updateDep(int dID, String dName);

    int updateDepEmps(int dID, String dName, String[] userids);

    int updateEmpDeps(String userid, String[] departs);

}
