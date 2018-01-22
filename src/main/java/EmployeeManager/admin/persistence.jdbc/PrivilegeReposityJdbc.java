package EmployeeManager.admin.persistence.jdbc;

import EmployeeManager.admin.model.Privilege;
import EmployeeManager.admin.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */
@Repository
public class PrivilegeReposityJdbc implements PrivilegeRepository {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Override
    public void update(Privilege privilege){
        jdbcTemplate.update("UPDATE privilege set privilege=?,weekday=?,pushTime=?,leaderPostLimit=?,leaderScoreLimit=? where pid=?"
                ,privilege.getPrivilege(),privilege.getWeekday(),privilege.getPushTime(),privilege.getLeaderPostLimit(),privilege.getLeaderScoreLimit(),privilege.getPid());
    }

    @Override
    public void remove(String pid){
        jdbcTemplate.update("DELETE FROM privilege where pid=?",pid);
    }

    @Override
    public Privilege get(String pid){
        return jdbcTemplate.queryForObject("select * from privilege where pid=?", BeanPropertyRowMapper.newInstance(Privilege.class),pid);
    }

    @Override
    public List<Privilege> list(){
        return jdbcTemplate.query("select * from privilege",BeanPropertyRowMapper.newInstance(Privilege.class));
    }

    @Override
    public void add(Privilege privilege){
        jdbcTemplate.update("INSERT privilege (`pID`,`privilege`,`weekday`,`pushTime`,`leaderPostLimit`,`leaderScoreLimit`) VALUES (?,?,?,?,?,?)",
                privilege.getPid(),privilege.getPrivilege(),privilege.getWeekday(),privilege.getPushTime(),privilege.getLeaderPostLimit(),privilege.getLeaderScoreLimit());
    }

}
