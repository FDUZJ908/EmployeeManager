package EmployeeManager.admin.application;

import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;
import EmployeeManager.admin.model.Privilege;
import EmployeeManager.admin.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 11437 on 2017/10/13.
 */

@Service
public class EmployeeService {
    @Autowired
    protected EmployeeRepository employeeRepository;

    public Employee get(String userid) {
        return employeeRepository.get(userid);
    }

    //获取所有成员
    public List<Employee> list() {
        return employeeRepository.list();
    }

    //获取部门下的成员 包括ID、姓名、部门和是否为领导
    public List<Depart> list(int dID) {
        return employeeRepository.list(dID);
    }

    public int getDepartID(String dName) {
        return employeeRepository.getDepartID(dName);
    }

    //获取所有部门
    public List<Depart> getDepartmentList() {
        return employeeRepository.getDepartmentList();
    }

    public List<Depart> getDepartmentList(String userID) {
        return employeeRepository.getDepartmentList(userID);
    }

    //获取当前所有权限级别
    public List<Privilege> getPrivilegeList() {
        return employeeRepository.getPrivilegeList();
    }

    //删除用户
    public void deleteEmp(String userid) {
        employeeRepository.removeEmp(userid);
    }

    //删除部门成员
    public void deleteEmpDep(String userid, int dID) {
        employeeRepository.removeEmpDep(userid, dID);
    }

    //删除部门
    public void deleteDep(String department) {
        employeeRepository.removeDep(department);
    }

    public int insertEmp(Employee employee) {
        return employeeRepository.insertEmp(employee);
    }

    //修改成员信息
    public int updateEmp(Employee employee) {
        return employeeRepository.updateEmp(employee);
    }

    //修改部门成员是否为领导
    public void updateLeader(String userid, int dID, int isleader) {
        employeeRepository.updateLeader(userid, dID, isleader);
    }

    public void updateLeaders(int dID, String[] leaders) {
        employeeRepository.updateLeaders(dID, leaders);
    }

    //新建部门
    public int insertDep(String dName) {
        return employeeRepository.insertDep(dName);
    }

    public int insertDepEmps(String dName, String[] userids) {
        return employeeRepository.insertDepEmps(dName, userids);
    }

    public int insertEmpDeps(String userid, String[] departs) {
        return employeeRepository.insertEmpDeps(userid, departs);
    }

    public int updateDep(int dID, String dName) {
        return employeeRepository.updateDep(dID, dName);
    }

    public int updateDepEmps(int dID, String dName, String[] userids) {
        return employeeRepository.updateDepEmps(dID, dName, userids);
    }

    public int updateEmpDeps(String userid, String[] departs) {
        return employeeRepository.updateEmpDeps(userid, departs);
    }
}