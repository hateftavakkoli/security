package coach.barnamenevis.security;

import coach.barnamenevis.security.users.domain.Users;
import coach.barnamenevis.security.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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


    @GetMapping(value = "/admin/register")
    @PreAuthorize("hasAuthority('OP_NEW_USER')")
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
        usersService.deleteById(id);
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

}
