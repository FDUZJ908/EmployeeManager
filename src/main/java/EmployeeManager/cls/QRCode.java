package EmployeeManager.cls;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lsh on 14/10/2017.
 */
public class QRCode {
    public int QRID;
    public String s_time;
    public String e_time;
    public int value;
    public String QREntry;
    public String flag;

    public Set<String> checkins;

    public QRCode(Map<String, Object> argv){
        QRID=(Integer)argv.get("QRID");
        s_time=argv.get("s_time").toString();
        e_time=argv.get("t_time").toString();
        value=(Integer)argv.get("value");
        QREntry=argv.get("QREntry").toString();
        flag=argv.get("flag").toString();

        checkins=new HashSet<String>();
    }

}
