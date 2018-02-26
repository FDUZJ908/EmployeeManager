package EmployeeManager.admin.controller;

import EmployeeManager.admin.application.EmployeeService;
import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller
public class DepartmentController {
    @Autowired
    protected EmployeeService employeeService;

    //查询所有部门
    @RequestMapping(method = RequestMethod.GET)
    public String listDepartment(Model model) {
        List<Depart> departmentList = employeeService.getDepartmentList();
        model.addAttribute("list", departmentList);//获取部门列表
        return "employee/listAllDep";
    }

    //增加部门
    @RequestMapping(value = "/addDep", method = RequestMethod.GET)
    public String addDep(Model model) {
        model.addAttribute("users", employeeService.list());
        return "employee/formAddDep";
    }

    @RequestMapping(value = "/addDep", method = RequestMethod.POST)
    public String modifyAddDep(@RequestParam("dName") String dName,
                               @RequestParam("selected") String selected,
                               Model model) {
        int res = employeeService.insertDep(dName);

        if (res == 0 && selected.length() > 0) {
            String[] users = selected.split(",");
            res = employeeService.insertDepEmps(dName, users);
        }
        if (res != 0) {
            model.addAttribute("errorNum", "05");
            return "templates/failure";
        }
        return "redirect:/department";
    }

    //删除部门
    @RequestMapping(value = "/deleteDep", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDep(@RequestParam("department") String department) {
        employeeService.delete(department);//删除部门
    }

    //编辑部门
    @RequestMapping(value = "/editDep", method = RequestMethod.GET)
    public String editDep(@RequestParam("dID") String dID,
                          @RequestParam("dName") String dName,
                          Model model) {
        model.addAttribute("dID", dID);
        model.addAttribute("dName", dName);
        List<Employee> users = employeeService.list();
        List<Depart> depEmps = employeeService.list(dName);
        for (Employee user : users) {
            for (Depart emp : depEmps)
                if (emp.getUserid().equals(user.getUserID()))
                    user.setSelected(1);
        }
        model.addAttribute("users", users);
        return "employee/formEditDep";
    }

    @RequestMapping(value = "/editDep", method = RequestMethod.POST)
    public String modifyEditDep(@RequestParam("dID") int dID,
                                @RequestParam("dName") String dName,
                                @RequestParam("selected") String selected,
                                Model model) {
        int res = employeeService.updateDep(dID, dName);
        if (res != 0) {
            model.addAttribute("errorNum", "05");
            return "templates/failure";
        }

        String[] users = (selected.length() > 0) ? selected.split(",") : new String[0];
        res = employeeService.updateDepEmps(dID, dName, users);
        if (res != 0) {
            model.addAttribute("errorNum", "05");
            return "templates/failure";
        }
        return "redirect:/department";
    }

}
