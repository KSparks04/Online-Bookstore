package org.project.controller;

import org.project.model.ShoppingCart;
import org.project.model.Book;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String editShoppingCart(@PathVariable("function") String function, @PathVariable("ISBN") long ISBN, Model model, HttpSession session) {
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

    // Check out with what is currently in the shopping cart
    @PostMapping("/shopping-cart/checkout")
    public String checkout(Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null || cart.getBookList().isEmpty()) {
            cart = new ShoppingCart();
            session.setAttribute("shoppingCart", cart);
        }

        model.addAttribute("shoppingCart", cart);
        model.addAttribute("total", cart.getTotalPrice());
        return "fragments/shopping-cart/checkout";
    }

    @PostMapping("/shopping-cart/checkout-success")
    public String checkoutSuccess(Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null || cart.getBookList().isEmpty()) {
            model.addAttribute("error", "Your shopping cart is empty.");
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("total", cart.getTotalPrice());
            return "fragments/shopping-cart/checkout";
        }

        List<Book> notEnoughStock = new ArrayList<>();
        Map<Long, Integer> bookCounts = cart.getBookCounts();

        for (Map.Entry<Long, Integer> entry : bookCounts.entrySet()) {
            Book storedBook = bookRepository.findByISBN(entry.getKey());
            int quantity = entry.getValue();
            if (storedBook.getInventory() < quantity) {
                notEnoughStock.add(storedBook);
            }
        }

        if (!notEnoughStock.isEmpty()) {
            model.addAttribute("error", "Some books do not have enough stock to complete your purchase: " +
                    notEnoughStock.stream().map(Book::getTitle).collect(Collectors.joining(", ")));
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("total", cart.getTotalPrice());
            return "fragments/shopping-cart/checkout";
        }
        for (Map.Entry<Long, Integer> entry : bookCounts.entrySet()) {
            Book storedBook = bookRepository.findByISBN(entry.getKey());
            storedBook.setInventory(storedBook.getInventory() - entry.getValue());
            bookRepository.save(storedBook);
        }

        model.addAttribute("shoppingCart", cart);
        model.addAttribute("bookCounts", bookCounts);
        model.addAttribute("total", cart.getTotalPrice());

        cart.clearBooks();
        session.setAttribute("shoppingCart", cart);

        return "fragments/shopping-cart/checkout-success";
    }
}
