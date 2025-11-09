package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.ShoppingCart;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;

@Controller
public class ShoppingCartController {

    @Autowired
    private BookRepository bookRepository;


    public static Model addShoppingCartAttributes(Model model, HttpSession session){
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null) {
            cart = new ShoppingCart();
        }
        model.addAttribute("shoppingCart", cart);
        return model;
    }

    //Controller to display the shopping cart stored in session
    //TODO store cart to Database and retrieve it from DB
    @GetMapping("/shopping-cart")
    public String getShoppingCart(Model model, HttpSession session) {
        ShoppingCartController.addShoppingCartAttributes(model, session);
        return "fragments/shopping-cart/shopping-cart-body";
    }

    //Edit
    @PostMapping("/shopping-cart/edit/{function}/{ISBN}")
    public String editShoppingCart(@PathVariable("function") String function, @PathVariable("ISBN") int ISBN, Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null) {
            cart = new ShoppingCart();
        }

        //Adds or removes book from shopping cart
        if (function.equalsIgnoreCase("add")){
            cart.addBook(bookRepository.findByISBN(ISBN));
        } else if (function.equalsIgnoreCase("remove")) {
            cart.removeBook(bookRepository.findByISBN(ISBN));
        }

        session.setAttribute("shoppingCart", cart);
        return "redirect:/shopping-cart";
    }
}
