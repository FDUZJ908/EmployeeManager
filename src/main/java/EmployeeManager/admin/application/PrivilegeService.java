package EmployeeManager.admin.application;

import EmployeeManager.admin.model.Privilege;
import EmployeeManager.admin.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */

@Service
public class PrivilegeService {
    @Autowired
    protected PrivilegeRepository privilegeRepository;

    public void modify(Privilege privilege) {
        privilegeRepository.update(privilege);
    }

    public void delete(String privilege) {
        privilegeRepository.remove(privilege);
    }

    public Privilege get(String privilege) {
        return privilegeRepository.get(privilege);
    }

    public void add(Privilege privilege) {
        privilegeRepository.add(privilege);
    }

    public List<Privilege> list() {
        return privilegeRepository.list();
    }

    public void create(Privilege privilege) {
        privilegeRepository.add(privilege);
    }
}
