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
            "from undealedCaseReport, user " +
            "where undealedCaseReport.userID = user.userID " +
            "and (undealedCaseReport.userID like concat(?,'%') or members like concat('%',?,'%')) " +
            "and submitTime between ? and ? " +
            "and leaderName like concat(?,'%') " +
            "and undealedCaseReport.userID like concat(?,'%') " +
            "order by submitTime desc";
    String sqlc = "select * " +
            "from caseReport , user " +
            "where caseReport.userID = user.userID " +
            "and (caseReport.userID like concat(?,'%') or members like concat('%',?,'%')) " +
            "and submitTime between ? and ? " +
            "and leaderName like concat(?,'%') " +
            "and caseReport.userID like concat(?,'%') " +
            "order by submitTime desc";

    String sqlug = "select * " +
            "from undealedGeneralReport, user " +
            "where undealedGeneralReport.userID = user.userID " +
            "and submitTime between ? and ? " +
            "and leaderName like concat(?,'%') " +
            "and undealedGeneralReport.userID like concat(?,'%') " +
            "order by submitTime desc";
    String sqlg = "select * " +
            "from generalReport, user " +
            "where generalReport.userID = user.userID " +
            "and submitTime between ? and ? " +
            "and leaderName like concat(?,'%') " +
            "and generalReport.userID like concat(?,'%') " +
            "order by submitTime desc";

    String sqll = "select * " +
            "from leaderReport , user " +
            "where leaderReport.userID = user.userID " +
            "and members like concat('%',?,'%') " +
            "and submitTime between ? and ? " +
            "and leaderReport.userID like concat(?,'%') " +
            "order by submitTime desc";

    @RequestMapping(method = RequestMethod.GET)
    public String reportQuery(Model model) {
        model.addAttribute("list", server.getAllUsers());
        model.addAttribute("types", 7);
        return "reportQuery/reportList";
    }


    @RequestMapping(value = "/searchReport")
    public String searchReport(@RequestParam("submitter") String submitter,
                               @RequestParam("leader") String leader,
                               @RequestParam("start") String start,
                               @RequestParam("end") String end,
                               @RequestParam(value = "types", required = false) int[] types,
                               @RequestParam("score") String score,
                               Model model) {

        if (start.equals("")) start = "0001-01-01";
        if (end.equals("")) end = "9999-12-31";

        String scoreID = server.name2id(score);
        String submitterID = server.name2id(submitter);

        Map<String, Object> defValue = new HashMap<String, Object>();
        List<HistoryReport> reports = new ArrayList<HistoryReport>();

        int type = 0;
        if (types != null) {
            for (int x : types) type |= x;
        }
        
        if ((type & 1) != 0) {
            Object args[] = new Object[]{start, end, leader, submitterID};
            defValue.put("type", HistoryReport.GENERAL);
            reports.addAll(server.jdbcTemplate.query(sqlug, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            defValue.put("type", HistoryReport.GENERAL | HistoryReport.APPROVED);
            reports.addAll(server.jdbcTemplate.query(sqlg, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
        }

        if ((type & 2) != 0) {
            Object args[] = new Object[]{scoreID, score, start, end, leader, submitterID};
            defValue.put("type", HistoryReport.CASE);
            reports.addAll(server.jdbcTemplate.query(sqluc, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
            defValue.put("type", HistoryReport.CASE | HistoryReport.APPROVED);
            reports.addAll(server.jdbcTemplate.query(sqlc, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));

        }

        if ((type & 4) != 0) {
            Object args[] = new Object[]{score, start, end, submitterID};
            defValue.put("type", HistoryReport.LEADER);
            reports.addAll(server.jdbcTemplate.query(sqll, args, new Mapper<HistoryReport>(HistoryReport.class, defValue)));
        }

        int i = 0;
        for (HistoryReport report : reports)
            report.setReportID(String.valueOf(++i));

        model.addAttribute("list", server.getAllUsers());
        model.addAttribute("submitter", submitter);
        model.addAttribute("leader", leader);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("score", score);
        model.addAttribute("types", type);
        model.addAttribute("reports", reports);
        return "reportQuery/reportList";
    }
}
