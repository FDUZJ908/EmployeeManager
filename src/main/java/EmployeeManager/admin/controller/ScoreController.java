package EmployeeManager.admin.controller;

import EmployeeManager.admin.service.ScoreService;
import EmployeeManager.admin.model.Score;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 11437 on 2017/10/14.
 */
@Controller
@RequestMapping("/score")
public class ScoreController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ScoreService scoreService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("list", scoreService.list());
        return "score/list";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String toform(@RequestParam(value = "userid", required = false) String userid, Model model) {
        String api = "";
        if (StringUtils.isNotBlank(userid)) {
            model.addAttribute("score", scoreService.get(userid));
            api = "/score/" + userid + "/modify";
        }
        model.addAttribute("api", api);
        return "score/form";
    }

    @RequestMapping(value = "/{userid}/modify", method = RequestMethod.POST)
    public String modify(@PathVariable("userid") String userid, Score score) {
        score.setUserid(userid);
        scoreService.modify(score);
        return "redirect:/score";
    }
}
