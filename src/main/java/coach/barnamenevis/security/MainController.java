package coach.barnamenevis.security;

import coach.barnamenevis.security.jwt.JwtAuth;
import coach.barnamenevis.security.jwt.JwtUtils;
import coach.barnamenevis.security.users.domain.Users;
import coach.barnamenevis.security.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class MainController {

    private final UsersService usersService;

    private final AuthenticationManager manager;

    private final JwtUtils jwtUtils;

    @Autowired
    public MainController(UsersService usersService, AuthenticationManager manager, JwtUtils jwtUtils) {
        this.usersService = usersService;
        this.manager = manager;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("")
    public String indexPage() {
        return "index";
    }

    @PreAuthorize("hasAuthority('OP_ACCESS_USER')")
    @GetMapping("/user")
    public String userPage() {
        return "user";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('OP_ACCESS_ADMIN')")
    public String adminPage(Model model) {
        model.addAttribute("users", usersService.findAll());
        return "admin";
    }


    @GetMapping("/user/get/{id}")
    @PostAuthorize("returnObject.email == authentication.name")
    public @ResponseBody
    Users getUser(@PathVariable("id") Long id) {
        return usersService.findById(id);
    }


    @GetMapping(value = "/admin/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new Users());
        return "registerUser";
    }

    @GetMapping(value = "/admin/edit/{id}")
    public String registerPage(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", usersService.findById(id));
        return "registerUser";
    }

    @GetMapping(value = "/admin/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        usersService.deleteById(usersService.findById(id));
        return "redirect:/admin";
    }


    @PostMapping(value = "/admin/register")
    public String register(@ModelAttribute(name = "user") Users users) {
        usersService.registerUser(users);
        return "redirect:/admin";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/getCookie")
    public String getCookie(HttpServletRequest request, HttpSession session) {
        for (Cookie cookie : request.getCookies())
            System.out.println(cookie.getName() + " : " + cookie.getValue());
        return "login";
    }

    @GetMapping("/setCookie")
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("user", "hatef");
        cookie.setMaxAge(60);
        response.addCookie(cookie);
        return "login";
    }

    @PostMapping("/jwt/login")
    public @ResponseBody
    ResponseEntity<?> jwtLogin(@RequestBody JwtAuth jwtAuth, HttpServletResponse response) {

        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuth.getUsername(), jwtAuth.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        response.addHeader("Authorization", jwtUtils.generateToken(jwtAuth.getUsername()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/jwt/hello")
    public @ResponseBody
    String jwtHello() {
        return "Hello Jwt";
    }


    @GetMapping("/info")
    public @ResponseBody
    Principal getCookie(Principal principal) {
        return principal;
    }
}
