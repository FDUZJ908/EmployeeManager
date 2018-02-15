package EmployeeManager.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lsh on 08/02/2018.
 */
@Controller
public class Administration {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

}
