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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                               @RequestParam("selectedDeps") String selected,
                               Model model) {
        employee.setUserid("random" + String.valueOf(getTimestamp())); //need to be updated later
        employee.setPosition_(position_);
        employee.setTitle_(title_);
        employee.setStatus_(status_);
        int res = employeeService.insertEmp(employee);

        if (res == 0 && selected.length() > 0) {
            String[] departs = selected.split(",");
            res = employeeService.insertEmpDeps(employee.getUserID(), departs);
        }
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
        List<Depart> departs = employeeService.getDepartmentList();
        List<Depart> empDeps = employeeService.getDepartmentList(userid);
        for (Depart d1 : departs) {
            for (Depart d2 : empDeps)
                if (d1.getDid().equals(d2.getDid()))
                    d1.setSelected(1);
        }

        model.addAttribute("employee", employeeService.get(userid));
        model.addAttribute("departmentList", departs); //获取所有部门
        model.addAttribute("privilegeList", employeeService.getPrivilegeList());
        return "employee/formEditEmp";
    }

    @RequestMapping(value = "/editEmp", method = RequestMethod.POST)
    public String modifyEditEmp(Employee employee,
                                @RequestParam(value = "position_", required = false) String position_,
                                @RequestParam(value = "title_", required = false) String title_,
                                @RequestParam(value = "status_") String status_,
                                @RequestParam("selectedDeps") String selected,
                                Model model) {
        employee.setPosition_(position_);
        employee.setTitle_(title_);
        employee.setStatus_(status_);

        int res = employeeService.updateEmp(employee);
        if (res != 0) {
            model.addAttribute("errorNum", "06");
            return "templates/failure";
        }

        String[] departs = (selected.length() > 0) ? selected.split(",") : new String[0];
        res = employeeService.updateEmpDeps(employee.getUserID(), departs);
        if (res != 0) {
            model.addAttribute("errorNum", "06");
            return "templates/failure";
        }

        return "redirect:/employee";
    }


    //****************************************************************************************************************//


    //查询选定部门成员
    @RequestMapping(value = "/depEmp", method = RequestMethod.GET)
    public String listDepEmployee(@RequestParam(value = "dID", required = false) Integer dID,
                                  @RequestParam("department") String department,
                                  Model model) {
        if (dID == null) dID = employeeService.getDepartID(department);
        model.addAttribute("dID", dID);
        model.addAttribute("department", department);
        model.addAttribute("list", employeeService.list(dID));//获取部门department下的成员 包括ID、姓名、部门和是否为领导
        model.addAttribute("departmentList", employeeService.getDepartmentList());//获取部门列表
        return "employee/listDepEmp";
    }

    //批量编辑领导
    @RequestMapping(value = "/editDepLeader", method = RequestMethod.GET)
    public String adminDepEmp(@RequestParam("dID") int dID,
                              @RequestParam("dName") String dName,
                              Model model) {
        model.addAttribute("dID", dID);
        model.addAttribute("dName", dName);
        model.addAttribute("list", employeeService.list(dID));
        return "employee/formEditDepLeader";
    }

    @RequestMapping(value = "/editDepLeader", method = RequestMethod.POST)
    public String modifyAdminDepEmp(@RequestParam("dID") int dID,
                                    @RequestParam("department") String department,
                                    @RequestParam("selected") String selected,
                                    RedirectAttributes redirectAttributes) {
        String[] users = (selected.length() > 0) ? selected.split(",") : new String[0];
        employeeService.updateLeaders(dID, users);
        redirectAttributes.addAttribute("dID", dID);
        redirectAttributes.addAttribute("department", department);
        return "redirect:/employee/depEmp";
    }

    //删除部门成员
    @RequestMapping(value = "/deleteDepEmp", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDepEmp(@RequestParam("userid") String userid,
                             @RequestParam("dID") int dID) {
        employeeService.deleteEmpDep(userid, dID);//删除部门成员
    }

    //编辑部门成员信息
    @RequestMapping(value = "/editDepEmp", method = RequestMethod.GET)
    public String editDepEmp(@RequestParam("userid") String userid,
                             @RequestParam("username") String username,
                             @RequestParam("dID") int dID,
                             @RequestParam("dName") String dName,
                             @RequestParam("isleader") int isleader,
                             Model model) {
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);
        model.addAttribute("dID", dID);
        model.addAttribute("dName", dName);
        model.addAttribute("isleader", isleader);
        return "employee/formEditDepEmp";
    }

    @RequestMapping(value = "/editDepEmp", method = RequestMethod.POST)
    public String modifyEditDepEmp(@RequestParam("userid") String userid,
                                   @RequestParam("dID") int dID,
                                   @RequestParam("department") String department,
                                   @RequestParam("isleader") int isleader,
                                   RedirectAttributes redirectAttributes) {
        employeeService.updateLeader(userid, dID, isleader);
        redirectAttributes.addAttribute("dID", dID);
        redirectAttributes.addAttribute("department", department);
        return "redirect:/employee/depEmp";
    }

}
