package EmployeeManager.admin.controller;

import EmployeeManager.admin.service.EmployeeService;
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

import static EmployeeManager.Util.getTimestamp;


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
    public String listEmployee(@RequestParam(value = "department", required = false) String department,
                               @RequestParam(value = "userName", required = false) String name,
                               Model model) {
        int dID = employeeService.getDepartID(department);
        if (name == null) name = "";
        else name = name.trim();
        List<Employee> list = (dID == 0) ? employeeService.listEmp(name) : employeeService.listEmp(dID, name);
        model.addAttribute("list", list); //获取所有成员
        model.addAttribute("departmentList", employeeService.getDepartmentList());
        model.addAttribute("searchName", name);
        model.addAttribute("searchDepart", department);
        return "employee/listAllEmp";
    }

    //增加人员
    @RequestMapping(value = "/addEmp", method = RequestMethod.GET)
    @ResponseBody
    public String addEmp(Model model) {
        /*
        model.addAttribute("departmentList", employeeService.getDepartmentList()); //获取所有部门
        model.addAttribute("privilegeList", employeeService.getPrivilegeList());
        return "employee/formAddEmp";
        */
        return "已开启通讯录同步，请前往企业微信处理：https://work.weixin.qq.com/";
    }

    /*
        @RequestMapping(value = "/addEmp", method = RequestMethod.POST)
        public String modifyAddEmp(Employee employee,
                                   @RequestParam(value = "position_", required = false) String position_,
                                   @RequestParam(value = "title_", required = false) String title_,
                                   @RequestParam(value = "status_") String status_,
                                   @RequestParam("selectedDeps") String selected,
                                   Model model) {
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
    */

    //删除人员
    @RequestMapping(value = "/deleteEmp", method = RequestMethod.POST)
    @ResponseBody
    public String deleteDep(@RequestParam("userid") String userid) {
        employeeService.deleteEmp(userid);//删除部门
        return "Succeed!";
    }

    //编辑人员信息
    @RequestMapping(value = "/editEmp", method = RequestMethod.GET)
    public String editEmp(@RequestParam("userid") String userid,
                          @RequestParam("redirect") String redirect,
                          Model model) {
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
        model.addAttribute("redirect", redirect);
        return "employee/formEditEmp";
    }

    @RequestMapping(value = "/editEmp", method = RequestMethod.POST)
    public String modifyEditEmp(Employee employee,
                                @RequestParam(value = "position_", required = false) String position_,
                                @RequestParam(value = "title_", required = false) String title_,
                                @RequestParam(value = "status_") String status_,
                                @RequestParam("selectedDeps") String selected,
                                @RequestParam("redirect") String redirect,
                                RedirectAttributes redirectAttributes,
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

        if (!redirect.equals("0")) {
            redirectAttributes.addAttribute("department", redirect);
            return "redirect:/employee/depEmp";
        }
        return "redirect:/employee";
    }


    //****************************************************************************************************************//


    //查询选定部门成员
    @RequestMapping(value = "/depEmp", method = RequestMethod.GET)
    public String listDepEmployee(@RequestParam(value = "dID", required = false) Integer dID,
                                  @RequestParam("department") String department,
                                  @RequestParam(value = "userName", required = false) String name,
                                  Model model) {
        if (dID == null) dID = employeeService.getDepartID(department);
        if (name == null) name = "";
        else name = name.trim();
        List<Depart> list = (dID == 0) ? employeeService.list(name) : employeeService.list(dID, name);
        model.addAttribute("dID", dID);
        model.addAttribute("department", department);
        model.addAttribute("list", list);//获取部门department下的成员 包括ID、姓名、部门和是否为领导
        model.addAttribute("departmentList", employeeService.getDepartmentList());
        model.addAttribute("searchName", name);
        model.addAttribute("searchDepart", department);
        return "employee/listDepEmp";
    }

    //批量编辑领导
    @RequestMapping(value = "/editDepLeader", method = RequestMethod.GET)
    public String editDepLeader(@RequestParam("dID") int dID,
                                @RequestParam("dName") String dName,
                                Model model) {
        model.addAttribute("dID", dID);
        model.addAttribute("dName", dName);
        model.addAttribute("list", employeeService.list(dID));
        return "employee/formEditDepLeader";
    }

    @RequestMapping(value = "/editDepLeader", method = RequestMethod.POST)
    public String modifyEditDepLeader(@RequestParam("dID") int dID,
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
    public String deleteDepEmp(@RequestParam("userid") String userid,
                               @RequestParam("dID") int dID) {
        employeeService.deleteEmpDep(userid, dID);//删除部门成员
        return "Succeed!";
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
