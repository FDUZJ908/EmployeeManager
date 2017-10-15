package EmployeeManager.admin.controller;

import EmployeeManager.admin.adminServer;
import EmployeeManager.admin.model.reportType;
import EmployeeManager.cls.HistoryReport;
import EmployeeManager.cls.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reportType")
public class reportTypeController {
    @Autowired
    adminServer adminServer;

    @RequestMapping(method = RequestMethod.GET)
    public String reportType(Model model) {
        Map<String, Object> defValue = new HashMap<String, Object>();
        List<Map<String, Object>> list;
        String sql = "select * from reportType";
        List<reportType> rptTypeList = new ArrayList<reportType>();
        Object args[] = new Object[]{};
        rptTypeList = adminServer.jdbcTemplate.query(sql, args, new Mapper<reportType>(reportType.class, defValue));

        model.addAttribute("list", rptTypeList);

        return "reportType/reportType";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/add")
    public String typeAdd() {
        return "reportType/typeAdd";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addReport")
    public String typeAdd(@RequestParam("name") String name,
                          @RequestParam("value") String value,
                          @RequestParam("remark") String remark) {
        Object args[] = new Object[]{name, Integer.parseInt(value), remark};
        try {
            adminServer.jdbcTemplate.update("insert into reportType (typeName, typeValue, typeRemark)" +
                    " values (?, ?, ?)", args);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "redirect:/reportType";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/modify")
    public String typeModify(@RequestParam(value = "id", required = false) String id,
                             Model model) {
        Map<String, Object> defValue = new HashMap<String, Object>();
        String sql = "select * from reportType where typeName = ?";
        List<Map<String, Object>> list;
        Object args[] = new Object[]{id};
        reportType rpt = adminServer.jdbcTemplate.queryForObject(sql, args, new Mapper<reportType>(reportType.class, defValue));
        model.addAttribute("rpt", rpt);

        return "reportType/modify";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modifyReport")
    public String typeModify(@RequestParam(value = "id", required = false) String id,
                             @RequestParam("name") String name,
                             @RequestParam("value") String value,
                             @RequestParam("remark") String remark) {

        Object args[] = new Object[]{name, Integer.parseInt(value), remark, id};
        try {
            adminServer.jdbcTemplate.update("update reportType set typeName = ?, typeValue = ?, typeRemark = ?" +
                    " where typeName = ?", args);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "redirect:/reportType";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete")
    public String typeDelete(@RequestParam(value = "id", required = false) String id,
                             Model model) {


        Object args[] = new Object[]{id};
        try {
            adminServer.jdbcTemplate.update("delete from reportType where typeName = ?", args);
        } catch (Exception e) {
            return e.getMessage();
        }


        return "redirect:/reportType";
    }
}
