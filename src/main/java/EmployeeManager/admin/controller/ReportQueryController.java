package EmployeeManager.admin.controller;

import EmployeeManager.admin.adminServer;
import EmployeeManager.admin.model.report;
import EmployeeManager.cls.CaseReport;
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
@RequestMapping("/reportQuery")
public class ReportQueryController {
    @Autowired
    adminServer adminServer;
    String sqluc = "select * " +
            "from undealedCaseReport , reportType , user " +
            "where undealedCaseReport.category = reportType.typeName " +
            "and undealedCaseReport.userID = user.userID " +
            "and (undealedCaseReport.userID like concat('%',?,'%') or members like concat('%',?,'%')) " +
            "and submitTime between ? and ? " +
            "and leaderName like concat('%',?,'%') " +
            "and undealedCaseReport.userID like concat('%',?,'%') " +
            "order by submitTime desc";
    String sqlc = "select * " +
            "from caseReport , reportType , user " +
            "where caseReport.category = reportType.typeName " +
            "and caseReport.userID = user.userID " +
            "and (caseReport.userID like concat('%',?,'%') or members like concat('%',?,'%')) " +
            "and submitTime between ? and ? " +
            "and leaderName like concat('%',?,'%') " +
            "and caseReport.userID like concat('%',?,'%') " +
            "order by submitTime desc";
    String sqlug = "select * " +
            "from undealedGeneralReport , reportType , user " +
            "where undealedGeneralReport.category = reportType.typeName " +
            "and undealedGeneralReport.userID = user.userID " +
            "and undealedGeneralReport.userID like concat('%',?,'%') " +
            "and submitTime between ? and ? " +
            "and leaderName like concat('%',?,'%') " +
            "and undealedGeneralReport.userID like concat('%',?,'%') " +
            "order by submitTime desc";
    String sqlg = "select * " +
            "from generalReport , reportType , user " +
            "where generalReport.category = reportType.typeName " +
            "and generalReport.userID = user.userID " +
            "and generalReport.userID like concat('%',?,'%') " +
            "and submitTime between ? and ? " +
            "and leaderName like concat('%',?,'%') " +
            "and generalReport.userID like concat('%',?,'%') " +
            "order by submitTime desc";

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

        if (start.equals("")) start = "0001-01-01";
        if (end.equals("")) end = "9999-12-31";

        System.out.println(adminServer.name2id(score));

        Map<String, Object> defValue = new HashMap<String, Object>();
        int selectType = 0;
        List<HistoryReport> reports = new ArrayList<HistoryReport>();
        if (category.equals("个案报告")) {
            Object args[] = new Object[]{adminServer.name2id(score), score, start, end, leader, adminServer.name2id(submitter)};
            defValue.put("type", HistoryReport.CASE);
            reports = adminServer.jdbcTemplate.query(sqluc, args, new Mapper<HistoryReport>(HistoryReport.class, defValue));
            defValue.put("type", HistoryReport.CASE);
            reports.addAll(adminServer.jdbcTemplate.query(sqlc, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
        }
        if (category.equals("一般报告")) {
            Object args[] = new Object[]{adminServer.name2id(score), start, end, leader, adminServer.name2id(submitter)};
            defValue.put("type", HistoryReport.GENERAL);
            reports = adminServer.jdbcTemplate.query(sqlug, args, new Mapper<HistoryReport>(HistoryReport.class, defValue));
            defValue.put("type", HistoryReport.GENERAL | HistoryReport.APPROVED);
            reports.addAll(adminServer.jdbcTemplate.query(sqlg, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
        }
        if (category.equals("领导报告")) {
            Object args[] = new Object[]{score, start, end, adminServer.name2id(submitter)};
            String sqll = "select * " +
                    "from leaderReport , reportType , user " +
                    "where leaderReport.category = reportType.typeName " +
                    "and leaderReport.userID = user.userID " +
                    "and members like concat('%',?,'%') " +
                    "and submitTime between ? and ? " +
                    "and leaderReport.userID like concat('%',?,'%') " +
                    "order by submitTime desc";
            defValue.put("type", HistoryReport.LEADER);
            reports = adminServer.jdbcTemplate.query(sqll, args, new Mapper<HistoryReport>(HistoryReport.class, defValue));
        }
        ;


        model.addAttribute("list", adminServer.getAllUser());
        model.addAttribute("category", category);
        model.addAttribute("reports", reports);
        return "reportQuery/reportList";
    }
}
