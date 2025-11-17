package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.Purchase;
import org.project.model.ShoppingCart;
import org.project.model.User;
import org.project.repository.BookRepository;
import org.project.repository.PurchaseRepository;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ShoppingCartController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

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

        addShoppingCartAttributes(model, session);

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

        //Use quantity map instead of looping raw list
        Map<Integer, Integer> quantities = cart.getBookCounts();

        for (var entry : quantities.entrySet()) {

            int isbn = entry.getKey();
            int qty  = entry.getValue();

            Book book = bookRepository.findByISBN(isbn);

            //Inventory decreases by exactly 'qty'
            int newInventory = book.getInventory() - qty;
            if (newInventory < 0) newInventory = 0;
            book.setInventory(newInventory);
            bookRepository.save(book);

            //Save each purchase individually
            for (int i = 0; i < qty; i++) {
                Purchase purchase = new Purchase(currentUser, book);
                purchaseRepository.save(purchase);
            }
        }

        // clear cart after checkout
        cart.clearBooks();
        session.setAttribute("shoppingCart", cart);

        model.addAttribute("success", "Purchase completed successfully!");
        return "purchase-success";
    }


}
