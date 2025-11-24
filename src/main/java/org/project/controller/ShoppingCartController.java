package org.project.controller;

import java.util.Collections;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;


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
                                   @PathVariable("ISBN") long ISBN,
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
    @PostMapping("/shopping-cart/validate-checkout")
    public String checkOut(Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        User currentUser = (User) session.getAttribute("currentUser");

        addShoppingCartAttributes(model, session);

        // If cart is empty
        if (cart == null || cart.getBookList().isEmpty()) {
            model.addAttribute("error", "Your shopping cart is empty.");
            return "fragments/shopping-cart/shopping-cart-body";
        }

        // If user is not logged in → redirect to register
        if (currentUser == null) {
            session.setAttribute("redirectAfterRegister", "/shopping-cart/checkout");
            return "redirect:/register";
        }

        return "redirect:/shopping-cart/checkout";
    }

    @GetMapping("/shopping-cart/checkout")
    public String getCheckout(Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");

        addShoppingCartAttributes(model, session);

        // If cart is empty
        if (cart == null || cart.getBookList().isEmpty()) {
            model.addAttribute("error", "Your shopping cart is empty.");
            return "fragments/shopping-cart/shopping-cart-body";
        }

        model.addAttribute("total", cart.getTotalPrice());

        return "checkout";
    }

    /**
     * Checkout endpoint – processes the purchase and saves to history
     */
    @PostMapping("/shopping-cart/checkout-success")
    public String checkoutSuccess(Model model, HttpSession session) {

        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        User currentUser = (User) session.getAttribute("currentUser");

        if (cart == null || cart.getBookList().isEmpty()) {
            model.addAttribute("error", "Your shopping cart is empty.");
            return "fragments/shopping-cart/shopping-cart-body";
        }

        // Get book quantities (ISBN → count)
        Map<Long, Integer> quantities = cart.getBookCounts();

        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {

            int isbn = Math.toIntExact(entry.getKey());
            int qty = entry.getValue();

            Book book = bookRepository.findByISBN(isbn);
            if (book == null) continue;

            // Update inventory
            int newInventory = book.getInventory() - qty;
            if (newInventory < 0) newInventory = 0;

            book.setInventory(newInventory);
            bookRepository.save(book);

            // Save purchase entries
            for (int i = 0; i < qty; i++) {
                purchaseRepository.save(new Purchase(currentUser, book));
            }
        }

        // Clear cart
        cart.clearBooks();
        session.setAttribute("shoppingCart", cart);

        model.addAttribute("success", "Purchase completed successfully!");
        return "purchase-success";
    }

    @GetMapping("/shopping-cart/count")
    @ResponseBody
    public Map<String, Integer> getCount(HttpSession session){
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        int count = cart != null ? cart.getBookList().size() : 0;
        return Collections.singletonMap("count", count);
    }
}
