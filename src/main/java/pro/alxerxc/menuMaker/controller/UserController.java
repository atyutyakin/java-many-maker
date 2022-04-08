package pro.alxerxc.menuMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.alxerxc.menuMaker.entity.User;
import pro.alxerxc.menuMaker.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final String REDIRECT_TO_INDEX_VIEW = "redirect:/users/index";
    private static final String INDEX_VIEW = "/user/index";
    private static final String EDIT_VIEW = "/user/edit";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showUsersRoot() {
        return REDIRECT_TO_INDEX_VIEW;
    }

    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return INDEX_VIEW;
    }

    @GetMapping("/signup")
    public String showSignupForm(User user, Model model) {
        setIsNew(model);
        return EDIT_VIEW;
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return EDIT_VIEW;
    }

    @PostMapping("/add")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setIsNew(model);
            return EDIT_VIEW;
        }
        userService.add(user);
        return REDIRECT_TO_INDEX_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        user.setId(id);
        if (result.hasErrors()) {
            return EDIT_VIEW;
        }
        userService.update(user);
        return REDIRECT_TO_INDEX_VIEW;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        userService.deleteById(id);
        return REDIRECT_TO_INDEX_VIEW;
    }

    private void setIsNew(Model model) {
        model.addAttribute("isNew", Boolean.TRUE);
    }
}
