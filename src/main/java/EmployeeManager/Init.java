package EmployeeManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Init implements CommandLineRunner {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<Map<String, Object>> sysVar = jdbcTemplate.queryForList("SELECT * FROM sysVar");
        for (Map<String, Object> map : sysVar) {
            switch (map.get("varName").toString()) {
                case "maxGeneralReportCount":
                    Variable.maxGeneralReportCount = (int) map.get("value");
                    break;
                case "caseReportEntryLimit":
                    Variable.caseReportEntryLimit = (int) map.get("value");
                    break;
                case "caseReportCheckLimit":
                    Variable.caseReportCheckLimit = (int) map.get("value");
                    break;
            }
        }
    }

}