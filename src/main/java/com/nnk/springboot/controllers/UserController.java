package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserForm;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/user/list")
    public String home(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("users", userService.findAll(
                PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"))));
        return "user/list";
    }

    @GetMapping("/admin/user/add")
    public String addUser(Model model) {
        model.addAttribute("user", new UserForm());
        return "user/add";
    }

    @PostMapping("/admin/user/validate")
    public String validate(@ModelAttribute("user") @Valid UserForm user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/add";
        }
        userService.save(user);
        return "redirect:/admin/user/list";
    }

    @GetMapping("/admin/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id);
        UserForm form = new UserForm(user.getId(), user.getUsername(), "", user.getFullname(), user.getRole());
        model.addAttribute("user", form);
        return "user/update";
    }

    @PostMapping("/admin/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @ModelAttribute("user") @Valid UserForm form,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        userService.update(id, form);
        return "redirect:/admin/user/list";
    }

    @PostMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/admin/user/list";
    }
}