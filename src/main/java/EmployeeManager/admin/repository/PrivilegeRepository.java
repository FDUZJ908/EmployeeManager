package EmployeeManager.admin.repository;

import EmployeeManager.admin.model.Privilege;

import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */
public interface PrivilegeRepository {
    void update(Privilege privilege);
    void remove(String pid);
    void add(Privilege privilege);
    Privilege get(String pid);
    List<Privilege> list();
}
