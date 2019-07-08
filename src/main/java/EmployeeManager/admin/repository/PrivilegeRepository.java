package EmployeeManager.admin.repository;

import EmployeeManager.admin.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */
@Repository
public class PrivilegeRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void update(Privilege privilege) {
        jdbcTemplate.update("UPDATE privilege set weekday=?,pushTime=?,leaderPostLimit=?,leaderScoreLimit=? where privilege=?"
                , privilege.getWeekday(), privilege.getPushTime(), privilege.getLeaderPostLimit(), privilege.getLeaderScoreLimit(), privilege.getPrivilege());
    }

    public void remove(String privilege) {
        jdbcTemplate.update("DELETE FROM privilege where privilege=?", privilege);
    }

    public Privilege get(String privilege) {
        return jdbcTemplate.queryForObject("select * from privilege where privilege=?", BeanPropertyRowMapper.newInstance(Privilege.class), privilege);
    }

    public List<Privilege> list() {
        return jdbcTemplate.query("select * from privilege", BeanPropertyRowMapper.newInstance(Privilege.class));
    }

    public void add(Privilege privilege) {
        jdbcTemplate.update("INSERT privilege (`privilege`,`weekday`,`pushTime`,`leaderPostLimit`,`leaderScoreLimit`) VALUES (?,?,?,?,?)",
                privilege.getPrivilege(), privilege.getWeekday(), privilege.getPushTime(), privilege.getLeaderPostLimit(), privilege.getLeaderScoreLimit());
    }

}
