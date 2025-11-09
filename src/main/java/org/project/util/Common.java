package org.project.util;

import jakarta.servlet.http.HttpSession;
import org.project.model.ShoppingCart;
import org.springframework.ui.Model;

public class Common {
    public static Model addCommonAttributes(Model model, HttpSession session){
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null) {
            cart = new ShoppingCart();
        }
        model.addAttribute("shoppingCart", cart);
        return model;
    }
}
