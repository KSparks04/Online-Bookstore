package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.User;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Display login page
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    /**
     * Handle login form submission
     */
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, HttpSession session, Model model) {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            session.setAttribute("currentUser", existingUser);
            return "redirect:/";  // redirect to home page or book list
        }

        model.addAttribute("loginError", "Invalid username or password");
        return "login";
    }

    /**
     * Handle logout
     */
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
