package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.ShoppingCart;
import org.project.model.User;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WishlistController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/wishlist")
    public String getWishlist(Model model, HttpSession session) {
        ShoppingCartController.addShoppingCartAttributes(model, session);
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("wishlist", user.getWishlist());

        // If user is not logged in → redirect to register
        if (user == null) {
            session.setAttribute("redirectAfterRegister", "/wishlist");
            return "redirect:/register";
        }

        return "wishlist";
    }

    @GetMapping("/wishlist/fragment")
    public String getWishlistBody(Model model, HttpSession session) {
        ShoppingCartController.addShoppingCartAttributes(model, session);
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("wishlist", user.getWishlist());
        return "fragments/wishlist-body";
    }

    @PostMapping("/wishlist/edit/{function}/{ISBN}")
    public String editWishlist(@PathVariable("function") String function,
                                   @PathVariable("ISBN") long ISBN,
                                   Model model, HttpSession session) {

        User user = (User) session.getAttribute("currentUser");
        Book book = bookRepository.findByISBN(ISBN);

        // If user is not logged in → redirect to register
        if (user == null) {
            throw new RuntimeException("User must first login");
//            session.setAttribute("redirectAfterRegister", "/get-browse-view");
//            return "redirect:/register";
        }

        if (function.equalsIgnoreCase("add")) {
            user.addBookToWishlist(book);
        } else if (function.equalsIgnoreCase("remove")) {
            user.removeBookFromWishlist(book);
        }

        session.setAttribute("currentUser", user);
        return "redirect:/wishlist";
    }
}
