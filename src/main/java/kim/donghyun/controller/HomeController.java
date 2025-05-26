package kim.donghyun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
    @RequestMapping("/")
    public String root() {
    	
        return "home"; // → /WEB-INF/views/home.jsp
    }
    
    @RequestMapping("/home")
    public String home() {
    	
        return "home"; // → /WEB-INF/views/home.jsp
    }
}
