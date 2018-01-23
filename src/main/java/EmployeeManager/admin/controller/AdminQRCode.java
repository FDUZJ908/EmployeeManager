package EmployeeManager.admin.controller;

import EmployeeManager.Server;
import EmployeeManager.cls.*;
import EmployeeManager.admin.adminServer;
import EmployeeManager.admin.model.report;
import EmployeeManager.admin.model.userScore;
import EmployeeManager.cls.CaseReport;
import EmployeeManager.cls.HistoryReport;
import EmployeeManager.cls.Mapper;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/adminQRCode")
public class AdminQRCode {
    @Autowired
    adminServer adminServer;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    Server server;

    String sqlu = "INSERT INTO QRCode VALUES (?, ?, ?, ?, ?)";
    String sqls = "SELECT * FROM QRCode";

    @RequestMapping(method = RequestMethod.GET)
    public String adminQRCode(Model model) {
        List<QRCode> QRCodes = jdbcTemplate.query(sqls, new Mapper<QRCode>((QRCode.class)));
        model.addAttribute("QRCodes", QRCodes);
        return "adminQRCode/adminQRCode";
    }

    @RequestMapping(value = "/setTime")
    public String setTime(@RequestParam("startdate") String startdate,
                          @RequestParam("starttime") String starttime,
                          @RequestParam("enddate") String enddate,
                          @RequestParam("endtime") String endtime,
                          @RequestParam("managers") String managers,
                          @RequestParam("value") String value,
                          Model model) {
        Integer token = new Random().nextInt();
        Object args[] = new Object[]{startdate + " " + starttime + ":00",
                enddate + " " + endtime + ":00", token, managers, value};
        jdbcTemplate.update(sqlu, args);
        List<QRCode> QRCodes = jdbcTemplate.query(sqls, new Mapper<QRCode>((QRCode.class)));

        model.addAttribute("QRCodes", QRCodes);
        return "adminQRCode/adminQRCode";
    }
}
