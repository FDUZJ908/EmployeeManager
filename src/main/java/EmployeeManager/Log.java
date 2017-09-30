package EmployeeManager;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    Logger logger;
    FileHandler fileHandler;

    Log(String name){
        logger= Logger.getLogger(name);
        logger.setLevel(Level.ALL);
        //logger.setUseParentHandlers(false);
        try {
            fileHandler = new FileHandler("~/Log/"+name+".log");
        }catch(Exception e)
        {
            System.out.println("log failed");
        }
    }

}
