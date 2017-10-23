package EmployeeManager.admin.controller;

import EmployeeManager.admin.application.EmployeeService;
import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;
//import com.admin.interfaces.facade.assembler.UserAssembler;
//import com.admin.interfaces.facade.commondobject.UserCommond;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

/**
 * Created by 11437 on 2017/10/13.
 */

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    protected EmployeeService employeeService;

    //查询所有部门成员
    @RequestMapping(method = RequestMethod.GET)
    public String list1(Model model){
        model.addAttribute("list", employeeService.list()); //获取所有成员
        model.addAttribute("department","");
        List<Depart> departmentList=employeeService.getDepartmentList(); //获取所有部门
        model.addAttribute("departmentList",departmentList);
        return "employee/list";
    }

    //查询选定部门成员
    @RequestMapping(method = RequestMethod.POST)
    public String list2(@RequestParam("department") String department ,Model model){
        model.addAttribute("list", employeeService.list(department));//获取部门department下的成员 包括ID、姓名、部门和是否为领导
        model.addAttribute("department",department);
        List<Depart> departmentList=employeeService.getDepartmentList();
        model.addAttribute("departmentList",departmentList);
        return "employee/list2";
    }


    //增加部门成员
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public String add(@RequestParam("department") String department,
                      @RequestParam("username") String username,
                      @RequestParam("isleader") String isleader,
                      Model model){
        employeeService.add(username,department,isleader); //部门中添加成员
        model.addAttribute("department",department);
        model.addAttribute("list", employeeService.list(department));
        List<Depart> departmentList=employeeService.getDepartmentList();
        model.addAttribute("departmentList",departmentList);
        //return "employee/list2"; ???
        return "redirect:/employee";
    }



    //删除部门成员
    @RequestMapping(value = "/{userid}/delete/{department}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("userid") String userid,
                       @PathVariable("department") String department) {
        System.out.println("***************");
        System.out.println(department);
        employeeService.delete(userid,department);//删除部门成员
    }

    //编辑成员
    @RequestMapping(value = "/{userid}/modify1",method = RequestMethod.POST)
    public String modify1(@PathVariable("userid")String userid, Employee employee){
        employee.setUserid(userid);
        employeeService.modify1(employee);//修改成员信息
        return "redirect:/employee";
    }

    //编辑部门下的成员
    @RequestMapping(value = "/{userid}/modify2/{department}",method = RequestMethod.POST)
    public String modify2(@PathVariable("userid")String userid,
                          @RequestParam("department") String department,
                          @RequestParam("isleader") String isleader){
        employeeService.modify2(userid,department,isleader); //修改部门成员是否为领导
        return "redirect:/employee";
        //???
    }

    //修改表单

    /*增加信息*/
    @RequestMapping(value = "/form1",method = RequestMethod.GET)
    public String toform1(@RequestParam(value = "department",required = false)String department,
                         Model model){
        String api="/employee/add";
        model.addAttribute("api",api);
        model.addAttribute("department",department);
        return "employee/formAdd";
    }

    /*修改信息*/
    @RequestMapping(value = "/form",method = RequestMethod.GET)
    public String toform2(@RequestParam(value = "userid")String userid,
                         @RequestParam(value = "department",required = false)String department,
                         @RequestParam(value = "username", required = false)String username,
                          @RequestParam(value = "isleader", required = false)String isleader,
                         Model model){
        String api="";
        if(department.equals("")) {
            api = "/employee/" + userid + "/modify1";
            model.addAttribute("api",api);
            model.addAttribute("employee",employeeService.get(userid));
            return "employee/formAll";
        }
        else {
            api = "/employee/" + userid + "/modify2/" + department;
            model.addAttribute("api", api);
            model.addAttribute("department",department);
            model.addAttribute("username",username);
            model.addAttribute("isleader",isleader);
            return "employee/formDep";
        }
    }
}
