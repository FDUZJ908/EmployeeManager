package EmployeeManager.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by lsh on 08/02/2018.
 */
@Controller
public class LoginController {

    @RequestMapping(method = RequestMethod.GET, value="/login")
    public String loginPage(Model model) {
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fail")
    public String failPage(Model model) {
        return "error/403";
    }
}
