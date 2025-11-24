package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.User;
import org.project.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping("/purchase-history")
    public String getUserPurchaseHistory(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            model.addAttribute("user", new User()); // prevents Thymeleaf binding error
            model.addAttribute("error", "You must be logged in to view your purchase history.");
            return "login";
        }

        model.addAttribute("purchases", purchaseRepository.findByUser(currentUser));
        return "purchase-history";
    }

    @GetMapping("/purchase-success")
    public String getPurchaseSuccess(){
        return "purchase-success";
    }
}
