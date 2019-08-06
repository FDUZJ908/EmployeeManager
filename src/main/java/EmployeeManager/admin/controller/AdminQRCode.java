package EmployeeManager.admin.controller;

import EmployeeManager.Server;
import EmployeeManager.Variable;
import EmployeeManager.admin.model.QRChecker;
import EmployeeManager.cls.Mapper;
import EmployeeManager.cls.QRCode;
import EmployeeManager.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/adminQRCode")
public class AdminQRCode {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Server server;


    String sqli = "INSERT INTO QRCode (s_time, e_time, token, managers, value, content) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    String sqls = "SELECT * FROM QRCode ORDER BY QRID";
    String sqlq = "SELECT * FROM QRCode WHERE QRID = ?";
    String sqlu = "UPDATE QRCode SET e_time = ? WHERE QRID = ? AND e_time > ?";
    String sqlc = "SELECT userName, QRCheckin.* FROM QRCheckin,user WHERE QRCheckin.userID = user.userID AND QRCheckin.QRID = ?";
    String sqld = "DELETE FROM QRCode WHERE QRID=?";

    @RequestMapping(method = RequestMethod.GET)
    public String adminQRCode(Model model) {
        List<QRCode> QRCodes = server.jdbcTemplate.query(sqls, new Mapper<>(QRCode.class));
        for (QRCode qrcode : QRCodes)
            qrcode.managers = server.id2name(qrcode.managers);
        List<String> AllUsers = server.getAllUsers();
        model.addAttribute("QRCodes", QRCodes);
        model.addAttribute("AllUsers", AllUsers);
        if (QRCodes.size() > 0) model.addAttribute("managers", QRCodes.get(QRCodes.size() - 1).managers);
        model.addAttribute("minReportWordCount", Variable.minReportWordCount);
        return "adminQRCode/adminQRCode";
    }

    @RequestMapping(value = "/setTime", method = RequestMethod.POST)
    public String setTime(@RequestParam("startdate") String startdate,
                          @RequestParam("starttime") String starttime,
                          @RequestParam("enddate") String enddate,
                          @RequestParam("endtime") String endtime,
                          @RequestParam("members") String managersname,
                          @RequestParam("value") int value,
                          @RequestParam("content") String content,
                          Model model) {
        String managers = server.name2id(managersname, ",");
        if (!managers.equals("")) {
            Integer token = Math.abs(new Random().nextInt());
            Object args[] = new Object[]{startdate + " " + starttime + ":00",
                    enddate + " " + endtime + ":00", token, managers, value, content};
            server.jdbcTemplate.update(sqli, args);
        }
        return "redirect:/adminQRCode";
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public String stop(@RequestParam(value = "id", required = false) String id, Model model) {
        String time = Util.currentTime();
        Object args[] = new Object[]{time, id, time};
        try {
            server.jdbcTemplate.update(sqlu, args);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return "redirect:/adminQRCode";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(@RequestParam("id") String id, Model model) {
        QRCode qrcode = server.jdbcTemplate.queryForObject(sqlq, new Object[]{id}, new Mapper<>(QRCode.class));
        if (qrcode == null) return "redirect:/adminQRCode";
        qrcode.managers = server.id2name(qrcode.managers);
        List<QRChecker> checkers = server.jdbcTemplate.query(sqlc, new Object[]{id}, new Mapper<>(QRChecker.class));
        for (QRChecker checker : checkers)
            checker.manager = server.id2name(checker.manager);
        model.addAttribute("qrcode", qrcode);
        model.addAttribute("checkers", checkers);
        return "adminQRCode/detail";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("id") String id, Model model) {
        server.jdbcTemplate.update(sqld, id);
        return "Succeed!";
    }
}
