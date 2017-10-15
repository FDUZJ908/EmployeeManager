package EmployeeManager.admin.controller;

import EmployeeManager.admin.adminServer;
import EmployeeManager.admin.model.report;
import EmployeeManager.cls.CaseReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reportQuery")
public class reportQueryController {
    @Autowired
    adminServer adminServer;

    @RequestMapping(method = RequestMethod.GET)
    public String reportQuery(Model model) {


        model.addAttribute("list", adminServer.getAllUser());
        return "reportQuery/reportList";
    }

    @RequestMapping(value = "/searchReport")
    public String searchReport(@RequestParam("score") String score,
                               @RequestParam("type") String category,
                               @RequestParam("start") String start,
                               @RequestParam("end") String end,
                               @RequestParam("leader") String leader,
                               @RequestParam("submitter") String submitter,
                               Model model) {

        Object args[] = new Object[]{score, score, start, end, leader, adminServer.name2id(submitter)};
        List<report> reports = new ArrayList<report>();
        if (category.equals("个案报告")) {

        }
        ;
        if (category.equals("一般报告")) ;
        if (category.equals("领导报告")) ;


        model.addAttribute("list", adminServer.getAllUser());
        model.addAttribute("reports", reports);
        return "reportQuery/reportList";
    }
}
