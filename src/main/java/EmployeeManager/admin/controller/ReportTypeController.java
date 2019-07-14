package EmployeeManager.admin.controller;

import EmployeeManager.Server;
import EmployeeManager.admin.model.reportType;
import EmployeeManager.cls.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReportTypeController {
    @Autowired
    Server server;

    @RequestMapping(method = RequestMethod.GET)
    public String reportType(Model model) {
        Map<String, Object> defValue = new HashMap<>();
        List<Map<String, Object>> list;
        String sql = "select * from reportType";
        Object args[] = new Object[]{};
        List<reportType> rptTypeList = server.jdbcTemplate.query(sql, args, new Mapper<>(reportType.class, defValue));
        model.addAttribute("list", rptTypeList);
        return "reportType/listReportType";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/add")
    public String add() {
        return "reportType/formAdd";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addReport")
    public String modifyAdd(@RequestParam("name") String name,
                            @RequestParam("value") String value,
                            @RequestParam("remark") String remark) {
        Object args[] = new Object[]{name, Integer.parseInt(value), remark};
        try {
            server.jdbcTemplate.update("insert into reportType (typeName, typeValue, typeRemark)" +
                    " values (?, ?, ?)", args);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "redirect:/reportType";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/modify")
    public String edit(@RequestParam(value = "id", required = false) String id,
                       Model model) {
        Map<String, Object> defValue = new HashMap<>();
        String sql = "select * from reportType where typeName = ?";
        List<Map<String, Object>> list;
        Object args[] = new Object[]{id};
        reportType rpt = server.jdbcTemplate.queryForObject(sql, args, new Mapper<>(reportType.class, defValue));
        model.addAttribute("rpt", rpt);

        return "reportType/formEdit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modifyReport")
    public String modifyEdit(@RequestParam(value = "id", required = false) String id,
                             @RequestParam("name") String name,
                             @RequestParam("value") String value,
                             @RequestParam("remark") String remark) {

        Object args[] = new Object[]{name, Integer.parseInt(value), remark, id};
        try {
            server.jdbcTemplate.update("update reportType set typeName = ?, typeValue = ?, typeRemark = ?" +
                    " where typeName = ?", args);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "redirect:/reportType";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete")
    public String delete(@RequestParam(value = "id", required = false) String id,
                             Model model) {


        Object args[] = new Object[]{id};
        try {
            server.jdbcTemplate.update("delete from reportType where typeName = ?", args);
        } catch (Exception e) {
            return e.getMessage();
        }


        return "redirect:/reportType";
    }
}
