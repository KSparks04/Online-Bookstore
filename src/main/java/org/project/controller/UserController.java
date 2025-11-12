package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.User;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Display login page
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User()); // ensures Thymeleaf has something to bind to
        }
        return "login";
    }

    /**
     * Handle login form submission
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("user", new User());
            return "login";
        }

        session.setAttribute("currentUser", user);

        // Optional: handle redirect if user tried to access checkout first
        String redirectAfterRegister = (String) session.getAttribute("redirectAfterRegister");
        if (redirectAfterRegister != null) {
            session.removeAttribute("redirectAfterRegister");
            return "redirect:" + redirectAfterRegister;
        }

        return "redirect:/";
    }

    /**
     * Display registration page
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Handle registration form submission
     */
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model,
                           HttpSession session) {

        // Check if username already exists
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists. Please choose another one.");
            model.addAttribute("user", new User());
            return "register";
        }

        // Create new user
        User user = new User(username, password);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        // ✅ Redirect to cart if user came from checkout
        String redirectAfterRegister = (String) session.getAttribute("redirectAfterRegister");
        if (redirectAfterRegister != null) {
            session.removeAttribute("redirectAfterRegister");
            return "redirect:" + redirectAfterRegister;
        }

        return "redirect:/";
    }

    /**
     * Logout the current user
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
