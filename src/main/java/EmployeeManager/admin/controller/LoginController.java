package EmployeeManager.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by lsh on 08/02/2018.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fail")
    public String fail(Model model) {
        return "error/403";
    }
}
