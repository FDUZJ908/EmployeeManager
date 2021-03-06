package EmployeeManager.admin.controller;

import EmployeeManager.admin.service.ScoreService;
import EmployeeManager.admin.model.Score;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by 11437 on 2017/10/14.
 */
@Controller
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    protected ScoreService scoreService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("list", scoreService.list());
        return "score/listScore";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "userid", required = false) String userid, Model model) {
        String api = "";
        if (StringUtils.isNotBlank(userid)) {
            model.addAttribute("score", scoreService.get(userid));
            api = "/score/" + userid + "/modify";
        }
        model.addAttribute("api", api);
        return "score/formEdit";
    }

    @RequestMapping(value = "/{userid}/modify", method = RequestMethod.POST)
    public String modifyEdit(@PathVariable("userid") String userid, Score score) {
        score.setUserid(userid);
        scoreService.modify(score);
        return "redirect:/score";
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public String clear(@RequestParam(value = "password") String password, HttpServletResponse response) {
        if (password.equals(System.getenv("ClearPassword")))
            scoreService.clear();
        else {
            response.setStatus(403);
            return "Fail!";
        }
        return "Succeed!";
    }
}
