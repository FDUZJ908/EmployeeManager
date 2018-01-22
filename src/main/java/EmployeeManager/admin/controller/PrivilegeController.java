package EmployeeManager.admin.controller;

import EmployeeManager.admin.application.PrivilegeService;
import EmployeeManager.admin.model.Privilege;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 11437 on 2017/10/14.
 */

@Controller
@RequestMapping("/privilege")
public class PrivilegeController {
    @Autowired
    protected PrivilegeService privilegeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("list",privilegeService.list());
        return "privilege/list";
    }

    @RequestMapping(value = "/form",method = RequestMethod.GET)
    public String toform(@RequestParam(value = "pid",required = false)String pid, Model model){
        String api="/privilege/add";
        if(StringUtils.isNotBlank(pid)){
            model.addAttribute("privilege",privilegeService.get(pid));
            api="/privilege/"+pid+"/modify";
        }
        model.addAttribute("api",api);
        return "privilege/form";
    }

    @RequestMapping(value = "/{pid}/modify",method = RequestMethod.POST)
    public String modify(@PathVariable("pid")String pid, Privilege privilege){
        privilege.setPid(pid);
            privilegeService.modify(privilege);
        return "redirect:/privilege";
    }

    @RequestMapping(value = "/{pid}/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("pid") String pid) {
        privilegeService.delete(pid);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/add")
    public String create(Privilege privilege){
        privilegeService.create(privilege);
        return "redirect:/privilege";
    }

}
