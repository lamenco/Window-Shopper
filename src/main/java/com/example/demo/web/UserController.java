package com.example.demo.web;

import com.example.demo.models.dto.UserRegisterDto;
import com.example.demo.service.UserService;


import javax.validation.Valid;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
        ;
    }

    @ModelAttribute("userModel")
    public UserRegisterDto initUserModel() {
        return new UserRegisterDto();
    }

    @GetMapping("/users/register")
    public String register() {
        return "register";
    }


    @PostMapping("/users/register")
    public String register(@Valid UserRegisterDto userModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (!userModel.getPassword().equals(userModel.getConfirmPassword())) {
            bindingResult.addError(
                    new FieldError(
                            "differentConfirmPassword",
                            "confirmPassword",
                            "Password must be the same."
                    )
            );
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userModel", userModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userModel", bindingResult);
            return "redirect:/users/register";
        }
        ;
        this.userService.register(userModel);
        return "redirect:/";
    }


    @GetMapping("/users/login")
    public String login() {
        return "login";
    }

    @PostMapping("/users/login-error")
    public String onFailedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username
            , RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);
        return "redirect:/users/login";
    }

}
