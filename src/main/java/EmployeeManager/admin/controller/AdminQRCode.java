package EmployeeManager.admin.controller;

import EmployeeManager.Server;
import EmployeeManager.Variable;
import EmployeeManager.cls.Mapper;
import EmployeeManager.cls.QRCode;
import EmployeeManager.cls.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/adminQRCode")
public class AdminQRCode {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    Server server;


    String sqli = "INSERT INTO QRCode (s_time, e_time, token, managers, value) " +
            "VALUES (?, ?, ?, ?, ?)";
    String sqls = "SELECT * FROM QRCode ORDER BY QRID";
    String sqlu = "UPDATE QRCode SET e_time = ? WHERE QRID = ? AND e_time > ?";

    @RequestMapping(method = RequestMethod.GET)
    public String adminQRCode(Model model) {
        List<QRCode> QRCodes = jdbcTemplate.query(sqls, new Mapper<QRCode>((QRCode.class)));
        for (QRCode qrcode : QRCodes)
            qrcode.managers = server.id2name(qrcode.managers);
        List<String> AllUsers = server.getAllUsers();
        model.addAttribute("QRCodes", QRCodes);
        model.addAttribute("AllUsers", AllUsers);
        model.addAttribute("managers", Variable.QRManagers);
        return "adminQRCode/adminQRCode";
    }

    @RequestMapping(value = "/setTime", method = RequestMethod.POST)
    public String setTime(@RequestParam("startdate") String startdate,
                          @RequestParam("starttime") String starttime,
                          @RequestParam("enddate") String enddate,
                          @RequestParam("endtime") String endtime,
                          @RequestParam("members") String managersname,
                          @RequestParam("value") int value,
                          Model model) {
        /*
        * 插入数据库
        * */
        Variable.QRManagers = managersname;
        String managers = server.name2id(managersname, ",");
        if (!managers.equals("")) {
            Integer token = Math.abs(new Random().nextInt());
            Object args[] = new Object[]{startdate + " " + starttime + ":00",
                    enddate + " " + endtime + ":00", token, managers, value};
            jdbcTemplate.update(sqli, args);
        }
        return "redirect:/adminQRCode";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop")
    public String stop(@RequestParam(value = "id", required = false) String id,
                       Model model) {
        /*
        * 更新数据库
        * */
        String time = Util.currentTime();
        Object args[] = new Object[]{time, id, time};
        try {
            server.jdbcTemplate.update(sqlu, args);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        return "redirect:/adminQRCode";
    }
}
