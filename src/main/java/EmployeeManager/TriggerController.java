package EmployeeManager;

import EmployeeManager.admin.model.Privilege;
import EmployeeManager.cls.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by lsh on 01/03/2018.
 */
@Controller
@RequestMapping("/wechat")
public class TriggerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Server server;
/*
    @RequestMapping("/synchronizer")
    @ResponseBody
    public String Synchronizer() throws Exception {
        logger.info("Synchronization starts!"); //log
        server.syncUser(server.syncDepartment());
        logger.info("Synchronization succeed!"); //log
        return "Synchronization succeed!";
    }
*/

    @RequestMapping("/refresh")
    @ResponseBody
    public String refresh() throws Exception {
        logger.info("Refresh starts!"); //log
        Variable.reported.clear();
        Variable.maxReportCount = server.getIntSysVar("maxReportCount");
        logger.info("Refresh succeed!"); //log
        return "Refresh succeed!";
    }

    @RequestMapping("/reminder")
    @ResponseBody
    public String reminder() {
        logger.info("Reminder starts!"); //log
        int weekday = 1 << Util.getWeekday();
        List<Privilege> privilegeList = server.getAllPriviledges();
        int[] privileges = new int[privilegeList.size()];
        int i = 0;
        for (Privilege privilege : privilegeList) {
            if ((privilege.getWeekday() & weekday) > 0)
                privileges[i] = privilege.getPrivilege();
            i++;
        }
        String userIDs = server.getUsersByPriviledges(privileges);
        try {
            server.sendMessage(userIDs, Variable.mesgForReport, false, Variable.reportAgentID);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("Reminder succeed!"); //log
        return "Reminder succeed";
    }
}