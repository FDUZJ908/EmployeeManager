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

    //获取所有成员
    public List<Employee> list() {
        return employeeRepository.list();
    }

    //获取所有部门
    public List<Depart> getDepartmentList() {
        return employeeRepository.getDepartmentList();
    }

    //获取所有成员姓名
    public List<Employee> getEmployeeList() {
        return employeeRepository.getEmployeeList();
    }

    //获取当前所有权限级别
    public List<Privilege> getPrivilegeList() {
        return employeeRepository.getPrivilegeList();
    }

    //获取部门department下的成员 包括ID、姓名、部门和是否为领导
    public List<Depart> list(String department) {
        return employeeRepository.list(department);
    }

    //部门中添加成员
    public void add(String username, String department, String isleader) {
        employeeRepository.add(username, department, isleader);
    }

    //删除部门成员
    public void delete(String userid, String department) {
        employeeRepository.remove(userid, department);
    }

    //删除部门
    public void delete(String department) {
        employeeRepository.remove(department);
    }

    //修改成员信息
    public void modify1(Employee employee) {
        employeeRepository.update1(employee);
    }

    //修改部门成员是否为领导
    public void modify2(String userid, String department, String isleader) {
        employeeRepository.update2(userid, department, isleader);
    }

    public Employee get(String userid) {
        return employeeRepository.get(userid);
    }

    //新建部门
    public void createDep(String username, String department, String isleader) {
        employeeRepository.createDep(username, department, isleader);
    }
}
