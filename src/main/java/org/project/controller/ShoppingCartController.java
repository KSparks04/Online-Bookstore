package org.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import java.util.HashMap;
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
        for(Book entry: cart.getBookList()){
            if(entry.getISBN() == book.getISBN()){

            }
        }
        if (function.equalsIgnoreCase("add")) {
            if(book.getInventory() > 0){
                cart.addBook(book);
            }
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

        double hst = cart.getTotalPrice() * 0.13;
        double totalAfterTax = hst + cart.getTotalPrice();

        model.addAttribute("subtotal", cart.getTotalPrice());
        model.addAttribute("hst", hst);
        model.addAttribute("totalAfterTax", totalAfterTax);

        return "checkout";
    }

    @GetMapping("/shopping-cart/checkout-table")
    public String getCheckoutBody(Model model, HttpSession session){
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");

        addShoppingCartAttributes(model, session);

        double hst = cart.getTotalPrice() * 0.13;
        double totalAfterTax = hst + cart.getTotalPrice();

        model.addAttribute("subtotal", cart.getTotalPrice());
        model.addAttribute("hst", hst);
        model.addAttribute("totalAfterTax", totalAfterTax);

        return "fragments/checkout-table";
    }

    /**
     * Checkout endpoint – processes the purchase and saves to history
     */
    @PostMapping("/shopping-cart/attempt-purchase")
    @Transactional
    @ResponseBody
    public Map<String, Object> checkoutSuccess(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "You must log in first");
            return response;
        }

        if (cart == null || cart.getBookList().isEmpty()) {
            response.put("success", false);
            response.put("message", "Your cart is empty");
            return response;
        }


        // Save each purchased book
        for (Book cartBook : cart.getBookList()) {
            //pull repo version of book
            Book repoBook = bookRepository.findByISBN(cartBook.getISBN());

            int quantityPurchased = cartBook.getInventory();
            //check if book exists
            if(repoBook == null){
                response.put("success", false);
                response.put("message", cartBook.getTitle() + " does not exist in repo");
                return response;
            }
            //check if purchase inventory is larger than actual inventory
            if(quantityPurchased > repoBook.getInventory()){
                response.put("success", false);
                response.put("message", "Cannot order more than whats in stock");
                return response;
            }
            //decrease inventory in book repo by purchase number
            bookRepository.decreaseInventoryByISBN(cartBook.getISBN(), quantityPurchased);
            //Save purchase
            purchaseRepository.save(new Purchase(currentUser, cartBook, quantityPurchased));
        
        }

        // Get book quantities (ISBN → count)
        Map<Long, Integer> quantities = cart.getBookCounts();

        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {

            long isbn = entry.getKey();
            int qty = entry.getValue();

            Book book = bookRepository.findByISBN(isbn);
            if (book == null) continue;

            // Update inventory
            int newInventory = book.getInventory() - qty;
            if (newInventory < 0) newInventory = 0;

            book.setInventory(newInventory);
            bookRepository.save(book);

            // Save purchase entries
            purchaseRepository.save(new Purchase(currentUser, book, qty));
        }

        // Clear cart
        cart.clearBooks();
        session.setAttribute("shoppingCart", cart);

        response.put("success", true);
        response.put("message", "Purchase completed successfully");
        return response;
    }

    @GetMapping("/shopping-cart/count")
    @ResponseBody
    public Map<String, Integer> getCount(HttpSession session){
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        int count = cart != null ? cart.getBookList().size() : 0;
        return Collections.singletonMap("count", count);
    }
}
