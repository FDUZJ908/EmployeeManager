package EmployeeManager.admin.persistence.jdbc;

import EmployeeManager.admin.model.Depart;
import EmployeeManager.admin.model.Employee;
//import EmployeeManager.admin.model.User;
import EmployeeManager.admin.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by 11437 on 2017/10/13.
 */
@Repository
public class EmployeeRepositoryJdbc implements EmployeeRepository {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Override
    public Employee get(String userid){
        return jdbcTemplate.queryForObject("select * from user where userid=?", BeanPropertyRowMapper.newInstance(Employee.class),userid);
    }

    @Override
    public List<Employee> list() {
        return jdbcTemplate.query("select * from user",BeanPropertyRowMapper.newInstance(Employee.class));
    }

    @Override
    public List<Depart> getDepartmentList(){
        return  jdbcTemplate.query("select distinct dName from department",BeanPropertyRowMapper.newInstance(Depart.class));
    }


    @Override
    public List<Depart> list(String department){
        return jdbcTemplate.query("select distinct department.userid,user.username,department.dname,department.isleader from department,user where user.userid=department.userid and dName=?",
                BeanPropertyRowMapper.newInstance(Depart.class),department);
    }

    @Override
    public void add(String username,String department,String isleader){
        List<Depart> temp1=jdbcTemplate.query("select distinct did from department where dname=?", BeanPropertyRowMapper.newInstance(Depart.class),department);
        String did=temp1.get(0).getDid();
        List<Employee> temp2=jdbcTemplate.query("select distinct userid from user where username=?",BeanPropertyRowMapper.newInstance(Employee.class),username);
        String userid=temp2.get(0).getUserID();
        jdbcTemplate.update("insert department (`did`,`userid`,`dname`,`isleader`) values (?,?,?,?)",did,userid,department,isleader);
    }

    @Override
    public void remove(String userid,String department){
        jdbcTemplate.update("delete from department where userid=? and dname=?",userid,department);
    }

    @Override
    public void update1(Employee employee){
        jdbcTemplate.update("UPDATE user SET username=?,duty=?,position=?,title=?,privilege=?,status=? WHERE userID=?",
                employee.getUsername(),employee.getDuty(),employee.getPosition(),employee.getTitle(),employee.getPrivilege(),employee.getStatus(),employee.getUserID());
    }

    @Override
    public void update2(String userid,String department,String isleader){
        jdbcTemplate.update("update department set isleader=? where userid=? and dname=?",isleader,userid,department);
    }


}
