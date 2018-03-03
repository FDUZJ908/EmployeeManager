package EmployeeManager.admin.controller;

import EmployeeManager.Server;
import EmployeeManager.cls.HistoryReport;
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
@RequestMapping("/reportQuery")
public class ReportQueryController {
    @Autowired
    Server server;

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

    String sqll = "select * " +
            "from leaderReport , reportType , user " +
            "where leaderReport.category = reportType.typeName " +
            "and leaderReport.userID = user.userID " +
            "and members like concat('%',?,'%') " +
            "and submitTime between ? and ? " +
            "and leaderReport.userID like concat('%',?,'%') " +
            "order by submitTime desc";

    @RequestMapping(method = RequestMethod.GET)
    public String reportQuery(Model model) {
        model.addAttribute("list", server.getAllUsers());
        return "reportQuery/reportList";
    }


    @RequestMapping(value = "/searchReport")
    public String searchReport(@RequestParam("submitter") String submitter,
                               @RequestParam("leader") String leader,
                               @RequestParam("start") String start,
                               @RequestParam("end") String end,
                               @RequestParam("type") String category,
                               @RequestParam("score") String score,
                               Model model) {

        if (start.equals("")) start = "0001-01-01";
        if (end.equals("")) end = "9999-12-31";

        System.out.println("score: " + score);
        System.out.println("submitter: " + submitter);
        String scoreID = server.name2id(score);
        String submitterID = server.name2id(submitter);

        Map<String, Object> defValue = new HashMap<String, Object>();
        List<HistoryReport> reports = new ArrayList<HistoryReport>();

        if (category.equals("") || category.equals("个案报告")) {
            Object args[] = new Object[]{scoreID, score, start, end, leader, submitterID};
            defValue.put("type", HistoryReport.CASE);
            reports.addAll(server.jdbcTemplate.query(sqluc, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            defValue.put("type", HistoryReport.CASE | HistoryReport.APPROVED);
            reports.addAll(server.jdbcTemplate.query(sqlc, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            System.out.println(reports.size());

        }

        if (category.equals("") || category.equals("一般报告")) {
            Object args[] = new Object[]{scoreID, start, end, leader, submitterID};
            defValue.put("type", HistoryReport.GENERAL);
            reports.addAll(server.jdbcTemplate.query(sqlug, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            defValue.put("type", HistoryReport.GENERAL | HistoryReport.APPROVED);
            reports.addAll(server.jdbcTemplate.query(sqlg, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
        }

        if (category.equals("") || category.equals("领导报告")) {
            Object args[] = new Object[]{score, start, end, submitterID};
            defValue.put("type", HistoryReport.LEADER);
            reports.addAll(server.jdbcTemplate.query(sqll, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            System.out.println(reports.size());

        }

        model.addAttribute("list", server.getAllUsers());
        System.out.println(reports.size());
        model.addAttribute("reports", reports);
        return "reportQuery/reportList";
    }
}
