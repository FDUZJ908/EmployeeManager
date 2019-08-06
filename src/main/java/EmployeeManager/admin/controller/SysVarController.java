package EmployeeManager.admin.controller;


import EmployeeManager.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static EmployeeManager.Util.getRepeatQMark;

@Controller
@RequestMapping("/sysVar")
public class SysVarController {

    @Autowired
    protected JdbcTemplate jdbcTemplate;


    @GetMapping
    public String sysVar(Model model) {
        model.addAttribute("maxGeneralReportCount", Variable.maxGeneralReportCount);
        model.addAttribute("caseReportEntryLimit", Variable.caseReportEntryLimit);
        model.addAttribute("caseReportCheckLimit", Variable.caseReportCheckLimit);
        model.addAttribute("leaderReportEntryLimit", Variable.leaderReportEntryLimit);
        model.addAttribute("minReportWordCount", Variable.minReportWordCount);
        return "sysVar/listSysVar";
    }

    @PostMapping(value = "/edit")
    public String edit(@RequestParam("maxGeneralReportCount") int maxGeneralReportCount,
                       @RequestParam("caseReportEntryLimit") int caseReportEntryLimit,
                       @RequestParam("caseReportCheckLimit") int caseReportCheckLimit,
                       @RequestParam("leaderReportEntryLimit") int leaderReportEntryLimit,
                       @RequestParam("minReportWordCount") int minReportWordCount,
                       Model model) {
        Variable.maxGeneralReportCount = maxGeneralReportCount;
        Variable.caseReportEntryLimit = caseReportEntryLimit;
        Variable.caseReportCheckLimit = caseReportCheckLimit;
        Variable.leaderReportEntryLimit = leaderReportEntryLimit;
        Variable.minReportWordCount = minReportWordCount;

        Object[] args = new Object[]{
                "maxGeneralReportCount", Variable.maxGeneralReportCount,
                "caseReportEntryLimit", Variable.caseReportEntryLimit,
                "caseReportCheckLimit", Variable.caseReportCheckLimit,
                "leaderReportEntryLimit", Variable.leaderReportEntryLimit,
                "minReportWordCount", Variable.minReportWordCount
        };

        String sqli = "INSERT INTO sysVar (varName, value) VALUES " + getRepeatQMark(args.length / 2, 2) + " ON DUPLICATE KEY UPDATE value = VALUES(value)";

        jdbcTemplate.update(sqli, args);
        return "redirect:/sysVar";
    }

}
