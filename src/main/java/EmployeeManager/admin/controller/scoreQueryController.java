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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/scoreQuery")
public class scoreQueryController {

    String sqlc = "select userID, members, scoreType, singleScore " +
            "from caseReport " +
            "where submitTime between ? and ? " +
            "and isPass = 1 " +
            "order by submitTime desc";

    String sqlg = "select userID, typeValue " +
            "from generalReport, reportType " +
            "where generalReport.category = reportType.typeName " +
            "and submitTime between ? and ? " +
            "and isPass = 1 " +
            "order by submitTime desc";

    String sqll = "select members, scoreType, singleScore " +
            "from leaderReport " +
            "where submitTime between ? and ? " +
            "and isPass = 1 " +
            "order by submitTime desc";


    @Autowired
    adminServer adminServer;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    Server server;

    @RequestMapping(method = RequestMethod.GET)
    public String reportQuery() {
        return "scoreQuery/scoreQuery";
    }

    @RequestMapping(value = "/searchScore")
    public String searchScore(@RequestParam("start") String start,
                              @RequestParam("end") String end,
                              Model model) {

        if (start.equals("") || end.equals(""))
            return "scoreQuery/scoreQuery";


        List<userScore> userScores = new ArrayList<userScore>();

        /*
        * UserId: [{userID: "...", userName: "..."}, ...]
        * */
        List<Map<String, Object>> UserId = new ArrayList<Map<String, Object>>();
        String sql = "SELECT userID, userName FROM user";
        UserId = jdbcTemplate.queryForList(sql);

        /*
        * IdScore = {Id: Score}
        * */
        Map<String, Integer> IdScore = new HashMap<String, Integer>();
        for (Map<String, Object> map : UserId) {
            IdScore.put(map.get("userID").toString(), 0);
        }


        Object args[] = new Object[]{start, end};
        List<Map<String, Object>> caseReport = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> generalReport = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> leaderReport = new ArrayList<Map<String, Object>>();
        caseReport = jdbcTemplate.queryForList(sqlc, args);
        generalReport = jdbcTemplate.queryForList(sqlg, args);
        leaderReport = jdbcTemplate.queryForList(sqll, args);
        for (Map<String, Object> map : caseReport) {
            String ids = server.name2id(map.get("members").toString(), ",") + "," + map.get("userID").toString();
            String pos = map.get("scoreType").toString();
            Integer score = Integer.parseInt(map.get("singleScore").toString());
            if (!pos.equals("true")) score = -score;
            adminServer.updateIdScoreMap(ids, score, IdScore);
        }
        for (Map<String, Object> map : generalReport) {
            String ids = map.get("userID").toString();
            Integer score = Integer.parseInt(map.get("typeValue").toString());
            adminServer.updateIdScoreMap(ids, score, IdScore);
        }
        for (Map<String, Object> map : leaderReport) {
            String ids = server.name2id(map.get("members").toString(), ",");
            String pos = map.get("scoreType").toString();
            Integer score = Integer.parseInt(map.get("singleScore").toString());
            if (!pos.equals("true")) score = -score;
            adminServer.updateIdScoreMap(ids, score, IdScore);
        }
        Integer total = 0;
        for (Map.Entry<String, Integer> entry : IdScore.entrySet()) {
            userScore tmp = new userScore(entry.getKey(), adminServer.getUserName(entry.getKey()), entry.getValue());
            total += entry.getValue();
            userScores.add(tmp);
        }
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("total", total);
        model.addAttribute("scores", userScores);
        return "scoreQuery/scoreQuery";
    }
}
