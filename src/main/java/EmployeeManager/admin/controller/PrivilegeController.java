package EmployeeManager.admin.controller;

import EmployeeManager.admin.application.PrivilegeService;
import EmployeeManager.admin.model.Privilege;
import EmployeeManager.admin.model.WeekDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        model.addAttribute("WeekDayList", createWeekDayList());
        return "privilege/formAdd";
    }

    @RequestMapping(value = "/modifyAdd", method = RequestMethod.POST)
    public String modifyAdd(Privilege privilege, @RequestParam(value = "weekdays", required = false) int[] weekdays) {
        int weekday = weekdaysToInt(weekdays);
        privilege.setWeekday(weekday);
        privilegeService.create(privilege);
        return "redirect:/privilege";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam("privilege") String privilege, Model model) {
        model.addAttribute("privilege", privilegeService.get(privilege));
        model.addAttribute("WeekDayList", createWeekDayList());
        return "privilege/formEdit";

    }

    @RequestMapping(value = "/modifyEdit", method = RequestMethod.POST)
    public String modifyEdit(@RequestParam(value = "weekdays", required = false) int[] weekdays,
                             Privilege privilege) {
        int weekday = weekdaysToInt(weekdays);
        privilege.setWeekday(weekday);
        privilegeService.modify(privilege);
        return "redirect:/privilege";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@RequestParam("privilege") String privilege) {
        privilegeService.delete(privilege);
    }

    protected List<WeekDay> createWeekDayList() {
        List<WeekDay> re = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (!re.add(new WeekDay(i)))
                return null;
        }
        return re;
    }

    protected int weekdaysToInt(int[] weekdays) {
        if (weekdays == null) return 0;
        int re = 0;
        for (int i = 0; i < weekdays.length; i++) {
            re |= (1 << weekdays[i]);
        }
        return re;
    }

}
