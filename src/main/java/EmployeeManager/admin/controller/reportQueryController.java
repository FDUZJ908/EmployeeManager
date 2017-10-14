package EmployeeManager.admin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reportQuery")
public class reportQueryController {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @RequestMapping(method = RequestMethod.GET)
    public String reportQuery(Model model) {

        return "reportQuery/reportList";
    }
}
