package EmployeeManager.admin.controller;

import EmployeeManager.admin.modle.reportType;
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
@RequestMapping("/reportType")
public class reportTypeController {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public String reportType(Model model) {
        List<Map<String, Object>> list;
        try {
            list = jdbcTemplate.queryForList("select * from reportType");
        } catch (Exception e) {
            return e.getMessage();
        }
        List<reportType> rptTypeList = new ArrayList<reportType>();
        for (Map<String, Object> map : list) {
            reportType type = new reportType(map.get("typeName"), map.get("typeValue"), map.get("typeRemark"));
            rptTypeList.add(type);
        }

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
            jdbcTemplate.update("insert into reportType (typeName, typeValue, typeRemark)" +
                    " values (?, ?, ?)", args);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "redirect:/reportType";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/modify")
    public String typeModify(@RequestParam(value = "id", required = false) String id,
                             Model model) {

        List<Map<String, Object>> list;
        Object args[] = new Object[]{id};
        try {
            list = jdbcTemplate.queryForList("select * from reporttype where typeName = ?", args);
        } catch (Exception e) {
            return e.getMessage();
        }
        for (Map<String, Object> map : list) {
            model.addAttribute("typeName", map.get("typeName"));
            model.addAttribute("typeValue", map.get("typeValue"));
            model.addAttribute("typeRemark", map.get("typeRemark"));
        }

        return "reportType/modify";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modifyReport")
    public String typeModify(@RequestParam(value = "id", required = false) String id,
                             @RequestParam("name") String name,
                             @RequestParam("value") String value,
                             @RequestParam("remark") String remark) {

        Object args[] = new Object[]{name, Integer.parseInt(value), remark, id};
        try {
            jdbcTemplate.update("update reporttype set typeName = ?, typeValue = ?, typeRemark = ?" +
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
            jdbcTemplate.update("delete from reporttype where typeName = ?", args);
        } catch (Exception e) {
            return e.getMessage();
        }


        return "redirect:/reportType";
    }
}
