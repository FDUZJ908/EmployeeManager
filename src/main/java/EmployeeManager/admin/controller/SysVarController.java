package EmployeeManager.admin.controller;


import EmployeeManager.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static EmployeeManager.Util.getRepeatQMark;

@Controller
@RequestMapping("/sysVar")
public class SysVarController {

    @Autowired
    protected JdbcTemplate jdbcTemplate;


    @RequestMapping(method = RequestMethod.GET)
    public String sysVar(Model model) {
        model.addAttribute("maxGeneralReportCount", Variable.maxGeneralReportCount);
        model.addAttribute("caseReportEntryLimit", Variable.caseReportEntryLimit);
        model.addAttribute("caseReportCheckLimit", Variable.caseReportCheckLimit);
        return "sysVar/listSysVar";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestParam("maxGeneralReportCount") int maxGeneralReportCount,
                       @RequestParam("caseReportEntryLimit") int caseReportEntryLimit,
                       @RequestParam("caseReportCheckLimit") int caseReportCheckLimit,
                       Model model) {
        Variable.maxGeneralReportCount = maxGeneralReportCount;
        Variable.caseReportEntryLimit = caseReportEntryLimit;
        Variable.caseReportCheckLimit = caseReportCheckLimit;
        String sqli = "INSERT INTO sysVar (varName, value) VALUES " + getRepeatQMark(3, 2) + " ON DUPLICATE KEY UPDATE value = VALUES(value)";

        Object[] args = new Object[]{"maxGeneralReportCount", Variable.maxGeneralReportCount,
                "caseReportEntryLimit", Variable.caseReportEntryLimit,
                "caseReportCheckLimit", Variable.caseReportCheckLimit};
        jdbcTemplate.update(sqli, args);
        return "redirect:/sysVar";
    }

}
