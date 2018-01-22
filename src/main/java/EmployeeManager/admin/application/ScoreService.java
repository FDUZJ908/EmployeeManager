package EmployeeManager.admin.application;

import EmployeeManager.admin.model.Score;
import EmployeeManager.admin.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 11437 on 2017/10/14.
 */
@Service
public class ScoreService {
    @Autowired
    protected ScoreRepository scoreRepository;

    public void modify(Score score){
        scoreRepository.update(score);
    }

    public Score get(String userid){
       return scoreRepository.get(userid);
    }

    public List<Score> list(){
        return scoreRepository.list();
    }
}
