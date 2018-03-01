package EmployeeManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lsh on 01/03/2018.
 */
public class VariableController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Server server;

    @RequestMapping("/Synchronizer")
    @ResponseBody
    public String Synchronizer() throws Exception {
        logger.info("Synchronization starts!"); //log
        server.syncUser(server.syncDepartment());
        logger.info("Synchronization succeed!"); //log
        return "Synchronization succeed!";
    }

    @RequestMapping("/Refresh")
    @ResponseBody
    public String Refresh() throws Exception {
        logger.info("Refresh starts!"); //log
        Variable.reported.clear();
        Variable.maxReportCount=server.getIntSysVar("maxReportCount");
        logger.info("Refresh succeed!"); //log
        return "Refresh succeed!";
    }

}
