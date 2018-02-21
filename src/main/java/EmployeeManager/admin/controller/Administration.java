package EmployeeManager.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by lsh on 08/02/2018.
 */
@Controller
public class Administration {

    @RequestMapping("/")
    public String home(Model model) {
        return "index";
    }
}
