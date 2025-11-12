package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.Purchase;
import org.project.model.ShoppingCart;
import org.project.model.User;
import org.project.repository.BookRepository;
import org.project.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ShoppingCartController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    /**
     * Utility method to add cart info to model
     */
    public static Model addShoppingCartAttributes(Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null) {
            cart = new ShoppingCart();
        }
        model.addAttribute("shoppingCart", cart);
        return model;
    }

    /**
     * Display current shopping cart
     */
    @GetMapping("/shopping-cart")
    public String getShoppingCart(Model model, HttpSession session) {
        addShoppingCartAttributes(model, session);
        return "fragments/shopping-cart/shopping-cart-body";
    }

    /**
     * Add or remove book from cart
     */
    @PostMapping("/shopping-cart/edit/{function}/{ISBN}")
    public String editShoppingCart(@PathVariable("function") String function,
                                   @PathVariable("ISBN") int ISBN,
                                   Model model, HttpSession session) {

        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null) {
            cart = new ShoppingCart();
        }

        Book book = bookRepository.findByISBN(ISBN);
        if (function.equalsIgnoreCase("add")) {
            cart.addBook(book);
        } else if (function.equalsIgnoreCase("remove")) {
            cart.removeBook(book);
        }

        session.setAttribute("shoppingCart", cart);
        return "redirect:/shopping-cart";
    }

    /**
     * Checkout endpoint – processes the purchase and saves to history
     */
    @PostMapping("/shopping-cart/checkout")
    public String checkout(Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        User currentUser = (User) session.getAttribute("currentUser");

        // If cart is empty
        if (cart == null || cart.getBookList().isEmpty()) {
            model.addAttribute("error", "Your shopping cart is empty.");
            return "fragments/shopping-cart/shopping-cart-body";
        }

        // If user is not logged in → redirect to register
        if (currentUser == null) {
            session.setAttribute("redirectAfterRegister", "/shopping-cart");
            return "redirect:/register";
        }

        // Save each purchased book
        for (Book book : cart.getBookList()) {
            purchaseRepository.save(new Purchase(currentUser, book));
        }

        // Clear cart after purchase
        cart.getBookList().clear();
        session.setAttribute("shoppingCart", cart);

        model.addAttribute("success", "Purchase completed successfully!");
        return "purchase-success";
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
        Map<Integer, Integer> bookCounts = cart.getBookCounts();

        for (Map.Entry<Integer, Integer> entry : bookCounts.entrySet()) {
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
        for (Map.Entry<Integer, Integer> entry : bookCounts.entrySet()) {
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
