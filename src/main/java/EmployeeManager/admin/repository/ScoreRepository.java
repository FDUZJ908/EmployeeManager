package EmployeeManager.admin.repository;

import EmployeeManager.admin.model.Score;

import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */
public interface ScoreRepository{
    void update(Score score);
    Score get(String userid);
    List<Score> list();
}
