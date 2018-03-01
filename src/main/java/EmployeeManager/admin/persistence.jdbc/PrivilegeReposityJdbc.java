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
    public void update(Privilege privilege) {
        jdbcTemplate.update("UPDATE privilege set weekday=?,pushTime=?,leaderPostLimit=?,leaderScoreLimit=? where privilege=?"
                , privilege.getWeekday(), privilege.getPushTime(), privilege.getLeaderPostLimit(), privilege.getLeaderScoreLimit(), privilege.getPrivilege());
    }

    @Override
    public void remove(String privilege) {
        jdbcTemplate.update("DELETE FROM privilege where privilege=?", privilege);
    }

    @Override
    public Privilege get(String privilege) {
        return jdbcTemplate.queryForObject("select * from privilege where privilege=?", BeanPropertyRowMapper.newInstance(Privilege.class), privilege);
    }

    @Override
    public List<Privilege> list() {
        return jdbcTemplate.query("select * from privilege", BeanPropertyRowMapper.newInstance(Privilege.class));
    }

    @Override
    public void add(Privilege privilege) {
        jdbcTemplate.update("INSERT privilege (`privilege`,`weekday`,`pushTime`,`leaderPostLimit`,`leaderScoreLimit`) VALUES (?,?,?,?,?)",
                privilege.getPrivilege(), privilege.getWeekday(), privilege.getPushTime(), privilege.getLeaderPostLimit(), privilege.getLeaderScoreLimit());
    }

}
