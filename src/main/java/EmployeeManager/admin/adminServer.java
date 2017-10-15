package EmployeeManager.admin;

import EmployeeManager.admin.model.report;
import EmployeeManager.admin.model.reportType;
import EmployeeManager.cls.CaseReport;
import EmployeeManager.cls.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class adminServer {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    public List<String> getAllUser() {
        List<Map<String, Object>> list;
        try {
            list = jdbcTemplate.queryForList("select userName from user");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        List<String> nameList = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            nameList.add(map.get("userName").toString());
        }
        return nameList;
    }

    public String name2id(String names) {
        String IDs = "";
        String[] namelist = names.split(",");

        List<Map<String, Object>> memberCursor = new ArrayList<Map<String, Object>>();
        String memberSql = "";

        for (int i = 0; i < namelist.length; i++) {
            memberSql = "select userID from user where userName=?";
            Object args[] = new Object[]{namelist[i]};
            try {
                memberCursor = jdbcTemplate.queryForList(memberSql, args);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (!memberCursor.isEmpty()) {
                for (Map<String, Object> map : memberCursor) {
                    IDs = IDs + map.get("userID").toString() + "|";
                }
            } else {
                System.out.println("成员不存在");
            }
        }
        if (IDs.length() > 0)
            IDs = IDs.substring(0, IDs.length() - 1); //****
        return IDs;
    }



}