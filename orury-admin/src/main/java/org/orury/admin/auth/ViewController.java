package org.orury.admin.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ViewController {
    @GetMapping("/notices")
    public String notices() {
        return "notices";
    }

    @GetMapping("/admins")
    public String admins() {
        return "admins";
    }
}
