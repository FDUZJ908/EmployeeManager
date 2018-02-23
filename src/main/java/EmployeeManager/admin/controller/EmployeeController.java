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

import static EmployeeManager.cls.Util.getTimestamp;


/**
 * Created by 11437 on 2017/10/13.
 */

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    protected EmployeeService employeeService;

    //查询所有人员
    @RequestMapping(method = RequestMethod.GET)
    public String listEmployee(Model model) {
        model.addAttribute("list", employeeService.list()); //获取所有成员
        List<Depart> departmentList = employeeService.getDepartmentList(); //获取所有部门
        model.addAttribute("departmentList", departmentList);
        return "employee/listAllEmp";
    }

    //增加人员
    @RequestMapping(value = "/addEmp", method = RequestMethod.GET)
    public String addEmp(Model model) {
        model.addAttribute("departmentList", employeeService.getDepartmentList()); //获取所有部门
        model.addAttribute("privilegeList", employeeService.getPrivilegeList());
        return "employee/formAddEmp";
    }

    @RequestMapping(value = "/addEmp", method = RequestMethod.POST)
    public String modifyAddEmp(Employee employee,
                               @RequestParam(value = "position_", required = false) String position_,
                               @RequestParam(value = "title_", required = false) String title_,
                               @RequestParam(value = "status_") String status_,
                               Model model) {
        employee.setUserid("random" + String.valueOf(getTimestamp()));
        employee.setPosition_(position_);
        employee.setTitle_(title_);
        employee.setStatus_(status_);
        int res = employeeService.insertEmp(employee);
        if (res != 0) {
            model.addAttribute("errorNum", "06");
            return "templates/failure";
        }
        return "redirect:/employee";
    }

    //删除人员
    @RequestMapping(value = "/deleteEmp", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDep(@RequestParam("userid") String userid) {
        employeeService.deleteEmp(userid);//删除部门
    }

    //编辑人员信息
    @RequestMapping(value = "/editEmp", method = RequestMethod.GET)
    public String editEmp(@RequestParam("userid") String userid, Model model) {
        model.addAttribute("employee", employeeService.get(userid));
        model.addAttribute("departmentList", employeeService.getDepartmentList()); //获取所有部门
        model.addAttribute("privilegeList", employeeService.getPrivilegeList());
        return "employee/formEditEmp";
    }

    @RequestMapping(value = "/editEmp", method = RequestMethod.POST)
    public String modifyEditEmp(Employee employee,
                                @RequestParam(value = "position_", required = false) String position_,
                                @RequestParam(value = "title_", required = false) String title_,
                                @RequestParam(value = "status_") String status_,
                                Model model) {
        employee.setPosition_(position_);
        employee.setTitle_(title_);
        employee.setStatus_(status_);
        int res = employeeService.updateEmp(employee);
        if (res != 0) {
            model.addAttribute("errorNum", "06");
            return "templates/failure";
        }
        return "redirect:/employee";
    }


    //****************************************************************************************************************//


    //查询选定部门成员
    @RequestMapping(value = "/depEmp", method = RequestMethod.GET)
    public String listDepEmployee(@RequestParam("department") String department, Model model) {
        model.addAttribute("list", employeeService.list(department));//获取部门department下的成员 包括ID、姓名、部门和是否为领导
        model.addAttribute("department", department);//获取部门
        List<Depart> departmentList = employeeService.getDepartmentList();
        model.addAttribute("departmentList", departmentList);//获取部门列表
        return "employee/listDepEmp";
    }

    //增加部门成员
    @RequestMapping(value = "/addDepEmp", method = RequestMethod.GET)
    public String addDepEmp(@RequestParam("department") String department, Model model) {
        model.addAttribute("department", department);
        model.addAttribute("usernameList", employeeService.getEmployeeList());
        return "employee/formAddDepEmp";
    }

    @RequestMapping(value = "/addDepEmp", method = RequestMethod.POST)
    public String modifyAddDepEmp(@RequestParam("department") String department,
                                  @RequestParam("username") String username,
                                  @RequestParam("isleader_") String isleader_) {
        String isleader = isleader_.equals("是") ? "1" : "0";
        employeeService.add(username, department, isleader);
        return "redirect:/employee";
    }

    //删除部门成员
    @RequestMapping(value = "/deleteDepEmp", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDepEmp(@RequestParam("userid") String userid,
                             @RequestParam("department") String department) {
        employeeService.delete(userid, department);//删除部门成员
    }

    //编辑部门成员信息
    @RequestMapping(value = "/editDepEmp", method = RequestMethod.GET)
    public String editDepEmp(@RequestParam("userid") String userid,
                             @RequestParam("username") String username,
                             @RequestParam("department") String department,
                             @RequestParam("isleader") String isleader,
                             Model model) {
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);
        model.addAttribute("department", department);
        model.addAttribute("isleader", isleader);
        return "employee/formEditDepEmp";
    }

    @RequestMapping(value = "/editDepEmp", method = RequestMethod.POST)
    public String modifyEditDepEmp(@RequestParam("userid") String userid,
                                   @RequestParam("department") String department,
                                   @RequestParam("isleader_") String isleader_) {
        String isleader = isleader_.equals("是") ? "1" : "0";
        employeeService.modify2(userid, department, isleader);
        return "redirect:/employee";
    }

}
