package EmployeeManager.admin.persistence.jdbc;

import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;
import EmployeeManager.admin.model.Privilege;
import EmployeeManager.admin.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static EmployeeManager.cls.Util.getRepeatQMark;

//import EmployeeManager.admin.model.User;

/**
 * Created by 11437 on 2017/10/13.
 */
@Repository
public class EmployeeRepositoryJdbc implements EmployeeRepository {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Override
    public Employee get(String userid) {
        return jdbcTemplate.queryForObject("select * from user where userid=?", BeanPropertyRowMapper.newInstance(Employee.class), userid);
    }

    @Override
    public List<Employee> list() {
        return jdbcTemplate.query("select * from user", BeanPropertyRowMapper.newInstance(Employee.class));
    }

    @Override
    public List<Depart> getDepartmentList() {
        return jdbcTemplate.query("select dID,dName from ministry order by dID", BeanPropertyRowMapper.newInstance(Depart.class));
    }

    @Override
    public List<Depart> getDepartmentList(String userID) {
        return jdbcTemplate.query("select distinct dID,dName from department where userID=? order by dID", BeanPropertyRowMapper.newInstance(Depart.class), userID);
    }

    @Override
    public List<Privilege> getPrivilegeList() {
        return jdbcTemplate.query("select distinct privilege from privilege order by privilege", BeanPropertyRowMapper.newInstance(Privilege.class));
    }

    @Override
    public List<Employee> getEmployeeList() {
        return jdbcTemplate.query("select distinct username from user", BeanPropertyRowMapper.newInstance(Employee.class));
    }

    @Override
    public List<Depart> list(String department) {
        return jdbcTemplate.query("select distinct department.userid,user.username,department.dname,department.isleader from department,user where user.userid=department.userid and dName=?",
                BeanPropertyRowMapper.newInstance(Depart.class), department);
    }

    @Override
    public void add(String username, String department, String isleader) {
        List<Depart> temp1 = jdbcTemplate.query("select distinct did from department where dname=?", BeanPropertyRowMapper.newInstance(Depart.class), department);
        String did = temp1.get(0).getDid();
        List<Employee> temp2 = jdbcTemplate.query("select distinct userid from user where username=?", BeanPropertyRowMapper.newInstance(Employee.class), username);
        String userid = temp2.get(0).getUserID();
        jdbcTemplate.update("insert department (`did`,`userid`,`dname`,`isleader`) values (?,?,?,?)", did, userid, department, isleader);
    }

    @Override
    public void removeEmp(String userid) {
        try {
            jdbcTemplate.update("DELETE FROM user WHERE userID=?", userid);
        } catch (Exception e) {
            jdbcTemplate.update("UPDATE user SET status=4 WHERE userID=?", userid);
        }
    }

    @Override
    public void remove(String userid, String department) {
        jdbcTemplate.update("delete from department where userid=? and dname=?", userid, department);
    }

    @Override
    public void remove(String department) {
        jdbcTemplate.update("delete from department where dname=?", department);
        jdbcTemplate.update("delete from ministry where dname=?", department);
    }

    @Override
    public int insertEmp(Employee employee) {
        try {
            String sql = "INSERT INTO user(userID,userName,duty,position,title,privilege,status,gender,tel,email) VALUES " + getRepeatQMark(1, 10);
            jdbcTemplate.update(sql,
                    employee.getUserID(), employee.getUsername(), employee.getDuty(), employee.getPosition(), employee.getTitle(), employee.getPrivilege(), employee.getStatus(), employee.getGender(), employee.getTel(), employee.getEmail());
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Override
    public int updateEmp(Employee employee) {
        try {
            jdbcTemplate.update("UPDATE user SET userName=?,duty=?,position=?,title=?,privilege=?,status=?,gender=?,tel=?,email=? WHERE userID=?",
                    employee.getUsername(), employee.getDuty(), employee.getPosition(), employee.getTitle(), employee.getPrivilege(), employee.getStatus(), employee.getGender(), employee.getTel(), employee.getEmail(), employee.getUserID());
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Override
    public void update2(String userid, String department, String isleader) {
        jdbcTemplate.update("update department set isleader=? where userid=? and dname=?", isleader, userid, department);
    }

    @Override
    public int insertDep(String dName) {
        try {
            jdbcTemplate.update("INSERT INTO ministry(dName) VALUES(?)", dName);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Override
    public int updateDep(int dID, String dName) {
        try {
            jdbcTemplate.update("UPDATE ministry SET dName=? WHERE dID=?", dName, dID);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Override
    public int insertEmpDeps(String userid, String[] departs) {
        try {
            int n = departs.length;
            if (n == 0) return 0;

            Object[] args = new Object[3 * n];
            String sql = "INSERT DEPARTMENT(dID,userID,dName) VALUES " + getRepeatQMark(n, 3);
            for (int i = 0, j = 0; i < n; i++) {
                int p = departs[i].indexOf(':');
                args[j++] = departs[i].substring(0, p);
                args[j++] = userid;
                args[j++] = departs[i].substring(p + 1);
            }
            jdbcTemplate.update(sql, args);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Override
    public int updateEmpDeps(String userid, String[] departs) {
        jdbcTemplate.update("DELETE FROM department WHERE userID=?", userid);
        return insertEmpDeps(userid, departs);
    }

    @Override
    public int getDepartID(String dName) {
        List<Map<String, Object>> res = jdbcTemplate.queryForList("select dID from ministry where dName=?", dName);
        if (res.size() == 0) return 0;
        return (int) res.get(0).get("dID");
    }

    @Override
    public int insertDepEmps(String dName, String[] userids) {
        int dID = getDepartID(dName);
        if (dID == 0) return 1;
        try {
            int n = userids.length;
            if (n == 0) return 0;

            Object[] args = new Object[3 * n];
            String sql = "INSERT DEPARTMENT(dID,userID,dName) VALUES " + getRepeatQMark(n, 3);
            for (int i = 0, j = 0; i < n; i++) {
                args[j++] = dID;
                args[j++] = userids[i];
                args[j++] = dName;
            }
            jdbcTemplate.update(sql, args);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Override
    public int updateDepEmps(int dID, String dName, String[] userids) {
        if (dID == 0) return 1;
        try {
            int n = userids.length;
            if (n == 0) {
                jdbcTemplate.update("DELETE FROM department WHERE dID=?", dID);
                return 0;
            }

            Object[] args = new Object[3 * n];
            String sql = "INSERT DEPARTMENT(dID,userID,dName) VALUES " + getRepeatQMark(n, 3) + " ON DUPLICATE KEY UPDATE dName=VALUES(dName)";
            for (int i = 0, j = 0; i < n; i++) {
                args[j++] = dID;
                args[j++] = userids[i];
                args[j++] = dName;
            }
            jdbcTemplate.update(sql, args);

            sql = "DELETE FROM DEPARTMENT WHERE dID=" + String.valueOf(dID) + " AND userID NOT IN " + getRepeatQMark(1, n);
            jdbcTemplate.update(sql, userids);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }
}
