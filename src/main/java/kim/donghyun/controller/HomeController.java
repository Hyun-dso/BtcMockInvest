package kim.donghyun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "main";  // 처음 진입시 보여줄 화면
    }

    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    @RequestMapping("/btc")
    public String home() {
        return "btc";
    }

    @RequestMapping("/signin")
    public String signin() {
        return "signin";
    }

    @RequestMapping("/regist")
    public String regist() {
        return "regist";
    }
}
