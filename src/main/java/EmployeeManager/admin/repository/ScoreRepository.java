package EmployeeManager.admin.repository;

import EmployeeManager.admin.model.Score;
import EmployeeManager.admin.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */
@Repository
public class ScoreRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void update(Score score) {
        jdbcTemplate.update("update user set f_score=? where userid=?", score.getF_score(), score.getUserid());
    }

    public Score get(String userid) {
        return jdbcTemplate.queryForObject("select userid,username,s_score,f_score from user where userid=?", BeanPropertyRowMapper.newInstance(Score.class), userid);
    }

    public List<Score> list() {
        return jdbcTemplate.query("select userid,username,s_score,f_score from user", BeanPropertyRowMapper.newInstance(Score.class));
    }
}
