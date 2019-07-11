package EmployeeManager;

import EmployeeManager.admin.model.Privilege;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
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
        Variable.generalCount.clear();
        Variable.leaderCount.clear();
        logger.info("Refresh succeed!"); //log
        return "Refresh succeed!";
    }

    @RequestMapping("/reminder")
    @ResponseBody
    public String reminder() {
        logger.info("Reminder starts!"); //log
        int weekday = 1 << Util.getWeekday();
        List<Privilege> privilegeList = server.getAllPriviledges();
        Integer[] privileges = new Integer[privilegeList.size()];
        int i = 0, hour = Util.getHour();
        for (Privilege privilege : privilegeList) {
            if ((privilege.getWeekday() & weekday) > 0 && Util.getHour(privilege.getPushTime()) == hour)
                privileges[i++] = privilege.getPrivilege();
        }

        String userIDs = server.getUsersByPriviledges(Arrays.copyOf(privileges, i), "|");
        try {
            server.sendMessage(userIDs, Variable.mesgForReport, false, Variable.AgentID);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("Reminder succeed!"); //log
        return "Reminder succeed";
    }
}
