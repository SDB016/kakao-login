package kakaologin.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MemberController {

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }
}
