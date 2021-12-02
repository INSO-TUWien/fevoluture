package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RoutingErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
