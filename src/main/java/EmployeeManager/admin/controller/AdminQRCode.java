package EmployeeManager.admin.controller;

import EmployeeManager.Server;
import EmployeeManager.Variable;
import EmployeeManager.cls.*;
import EmployeeManager.admin.adminServer;
import EmployeeManager.admin.model.report;
import EmployeeManager.admin.model.userScore;
import EmployeeManager.cls.CaseReport;
import EmployeeManager.cls.HistoryReport;
import EmployeeManager.cls.Mapper;
import groovy.util.MapEntry;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
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

    String sqli = "INSERT INTO QRCode (s_time, e_time, token, managers, value) " +
            "VALUES (?, ?, ?, ?, ?)";
    String sqls = "SELECT * FROM QRCode";
    String sqlu = "UPDATE QRCode SET e_time = ? " +
            "WHERE QRID = ?";
    String sqllast = "SELECT LAST_INSERT_ID()";
    String sqlm = "SELECT * FROM QRCode WHERE QRID = ?";

    @RequestMapping(method = RequestMethod.GET)
    public String adminQRCode(Model model) {
        List<QRCode> QRCodes = jdbcTemplate.query(sqls, new Mapper<QRCode>((QRCode.class)));
        adminServer.updateQRCodes(QRCodes);
        List<String> AllUsers = server.getAllUsers();
        model.addAttribute("QRCodes", QRCodes);
        model.addAttribute("AllUsers", AllUsers);
        return "adminQRCode/adminQRCode";
    }

    @RequestMapping(value = "/setTime")
    public String setTime(@RequestParam("startdate") String startdate,
                          @RequestParam("starttime") String starttime,
                          @RequestParam("enddate") String enddate,
                          @RequestParam("endtime") String endtime,
                          @RequestParam("members") String managersname,
                          @RequestParam("value") String value,
                          Model model) {

        /*
        * 插入数据库
        * */
        // System.out.println(managersname);
        String managers = adminServer.name2id(managersname);
        if (!managers.equals("")) {
            Integer token = new Random().nextInt();
            Object args[] = new Object[]{startdate + " " + starttime + ":00",
                    enddate + " " + endtime + ":00", token, managers, value};
            jdbcTemplate.update(sqli, args);

        /*
        * 更新缓存
        * */
            Integer tmp = jdbcTemplate.queryForObject(sqllast, Integer.class);
            QRCode tmpQR = new QRCode(tmp, startdate + " " + starttime + ":00",
                    enddate + " " + endtime + ":00", token, managers, Integer.parseInt(value));
            Variable.QRCodes.put(tmp, tmpQR);

        }

        /*
        * 返回数据给前端
        * */
        List<QRCode> QRCodes = jdbcTemplate.query(sqls, new Mapper<QRCode>((QRCode.class)));
        adminServer.updateQRCodes(QRCodes);
        List<String> AllUsers = server.getAllUsers();
        model.addAttribute("QRCodes", QRCodes);
        model.addAttribute("AllUsers", AllUsers);
        return "adminQRCode/adminQRCode";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop")
    public String delete(@RequestParam(value = "id", required = false) String id,
                         Model model) {
        /*
        * 更新数据库
        * */
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(day);
        Object args[] = new Object[]{time, id};
        try {
            adminServer.jdbcTemplate.update(sqlu, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        /*
        * 更新缓存
        * */
        try {
            Variable.QRCodes.get(Integer.parseInt(id)).e_time = time;
        } catch (Exception e) {
            Object argstmp[] = new Object[]{id};
            List<QRCode> QRCodes = jdbcTemplate.query(sqlm, argstmp, new Mapper<QRCode>((QRCode.class)));
            if (QRCodes.size() == 1) {
                Variable.QRCodes.put(Integer.parseInt(id), QRCodes.get(0));
            }
        }

        /*
        * 返回数据给前端
        * */
        List<QRCode> QRCodes = jdbcTemplate.query(sqls, new Mapper<QRCode>((QRCode.class)));
        adminServer.updateQRCodes(QRCodes);
        List<String> AllUsers = server.getAllUsers();
        model.addAttribute("QRCodes", QRCodes);
        model.addAttribute("AllUsers", AllUsers);
        return "adminQRCode/adminQRCode";
    }
}
