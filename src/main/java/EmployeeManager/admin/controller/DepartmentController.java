package EmployeeManager.admin.controller;

import EmployeeManager.admin.application.EmployeeService;
import EmployeeManager.admin.model.Depart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by lsh on 21/02/2018.
 */
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    protected EmployeeService employeeService;

    //查询所有部门
    @RequestMapping(value = "/dep", method = RequestMethod.GET)
    public String listDep(Model model) {
        List<Depart> departmentList = employeeService.getDepartmentList();
        model.addAttribute("list", departmentList);//获取部门列表
        return "employee/listAllDep";
    }

    //增加部门
    @RequestMapping(value = "/addDep", method = RequestMethod.GET)
    public String addDep(Model model) {
        model.addAttribute("dID", 123);
        model.addAttribute("users",employeeService.list());
        return "employee/formAddDep";
    }

    @RequestMapping(value = "/addDep", method = RequestMethod.POST)
    public String modifyDep(@RequestParam("dID") String dID,
                            @RequestParam("dName") String dName,
                            @RequestParam("selected") String selected) {
        System.out.println("dID: "+dID);
        System.out.println("dName: "+dName);
        System.out.println("selected: "+selected);
        //employeeService.createDep(username, department, isleader);
        return "redirect:/employee/dep";
    }

    //删除部门
    @RequestMapping(value = "/deleteDep", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDep(@RequestParam("department") String department) {
        employeeService.delete(department);//删除部门
    }

}
