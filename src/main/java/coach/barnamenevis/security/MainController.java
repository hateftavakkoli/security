package coach.barnamenevis.security;

import coach.barnamenevis.security.users.domain.Users;
import coach.barnamenevis.security.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class MainController {

    private final UsersService usersService;

    @Autowired
    public MainController(UsersService usersService) {
        this.usersService = usersService;
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

    @GetMapping("/info")
    public @ResponseBody
    Principal info(Principal principal) {
        return principal;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


}
