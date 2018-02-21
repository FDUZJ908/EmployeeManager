package EmployeeManager.admin.controller;

import EmployeeManager.admin.application.PrivilegeService;
import EmployeeManager.admin.model.Privilege;
import EmployeeManager.admin.model.WeekDay;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */

@Controller
@RequestMapping("/privilege")
public class PrivilegeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PrivilegeService privilegeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("list", privilegeService.list());
        return "privilege/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("WeekDayList",createWeekDayList());
        return "privilege/formAdd";
    }

    @RequestMapping(value = "/modifyAdd", method = RequestMethod.POST)
    public String modifyAdd(Privilege privilege,@RequestParam(value="weekdays",required = false)String[] weekdays ) {
        int weekday=weekdayStringToInt(weekdays);
        privilege.setWeekday(String.valueOf(weekday));
        privilegeService.create(privilege);
        return "redirect:/privilege";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam("pid") String pid, Model model) {
        model.addAttribute("pid", pid);
        model.addAttribute("privilege", privilegeService.get(pid));
        model.addAttribute("WeekDayList",createWeekDayList());
        return "privilege/formEdit";

    }

    @RequestMapping(value = "/modifyEdit", method = RequestMethod.POST)
    public String modifyEdit(@RequestParam("pid") String pid,
                             @RequestParam(value="weekdays",required = false)String[] weekdays,
                             Privilege privilege) {
        privilege.setPid(pid);
        int weekday=weekdayStringToInt(weekdays);
        privilege.setWeekday(String.valueOf(weekday));
        privilegeService.modify(privilege);
        return "redirect:/privilege";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@RequestParam("pid") String pid) {
        privilegeService.delete(pid);
    }

    protected List<WeekDay> createWeekDayList() {
        List<WeekDay> re = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            WeekDay temp = new WeekDay(String.valueOf(i));
            if(!re.add(temp))
                return null;
        }
        return re;
    }

    protected int weekdayStringToInt(String[] weekdays){
        int re=0;
        for (int i=0;i<weekdays.length;i++){
            re|=(1<<( Integer.parseInt(weekdays[i])-1));
        }
        return re;
    }

}
