package EmployeeManager.admin.controller;

import EmployeeManager.admin.application.EmployeeService;
import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import com.admin.interfaces.facade.assembler.UserAssembler;
//import com.admin.interfaces.facade.commondobject.UserCommond;

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
        model.addAttribute("department", "");
        List<Depart> departmentList = employeeService.getDepartmentList(); //获取所有部门
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("currentMenu", "menu5");
        return "employee/listAllEmp";
    }

    //增加人员
    @RequestMapping(value = "/addEmp", method = RequestMethod.POST)
    public String addEmp(@PathVariable("userid") String userid, Employee employee) {
        employee.setUserid(userid);
        employeeService.modify1(employee);//修改成员信息
        return "redirect:/employee";
    }

    //删除人员

    //编辑人员信息
    @RequestMapping(value = "/editEmp", method = RequestMethod.GET)
    public String editEmp(@RequestParam("userid") String userid, Model model) {
        model.addAttribute("employee", employeeService.get(userid));
        model.addAttribute("userid", userid);
        model.addAttribute("privilegeList", employeeService.getPrivilegeList());
        return "employee/formEditAll";
    }

    @RequestMapping(value = "/editEmp", method = RequestMethod.POST)
    public String modifyEditEmp(@RequestParam("userid") String userid,
                                Employee employee,
                                @RequestParam(value = "position_", required = false) String position_,
                                @RequestParam(value = "title_", required = false) String title_,
                                @RequestParam(value = "status_") String status_,
                                @RequestParam(value = "gender_") String gender_) {
        employee.setUserid(userid);
        switch (position_) {
            case "村级干部":
                employee.setPosition("0");
                break;
            case "一般干部":
                employee.setPosition("1");
                break;
            case "中层干部":
                employee.setPosition("2");
                break;
            case "领导干部":
                employee.setPosition("3");
                break;
            default:
                break;
        }
        switch (title_) {
            case "村级":
                employee.setTitle("0");
                break;
            case "科员":
                employee.setTitle("1");
                break;
            case "股级":
                employee.setTitle("2");
                break;
            case "科级":
                employee.setTitle("3");
                break;
            default:
                break;
        }
        switch (status_) {
            case "在职":
                employee.setStatus("0");
                break;
            case "退休":
                employee.setStatus("1");
                break;
            case "退职":
                employee.setStatus("2");
                break;
            case "开除":
                employee.setStatus("3");
                break;
            case "离任":
                employee.setStatus("4");
                break;
            default:
                break;
        }
        switch (gender_) {
            case "男":
                employee.setGender("1");
                break;
            case "女":
                employee.setGender("2");
                break;
            case "保密":
                employee.setGender("0");
                break;
            default:
                break;
        }
        employeeService.modify1(employee);
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
        String isleader = isleader_.equals("是")?"1":"0";
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
        return "employee/formEditDep";
    }

    @RequestMapping(value = "/editDepEmp", method = RequestMethod.POST)
    public String modifyEditDepEmp(@RequestParam("userid") String userid,
                                @RequestParam("department") String department,
                                @RequestParam("isleader_") String isleader_) {
        String isleader = isleader_.equals("是")?"1":"0";
        employeeService.modify2(userid, department, isleader);
        return "redirect:/employee";
    }

}
