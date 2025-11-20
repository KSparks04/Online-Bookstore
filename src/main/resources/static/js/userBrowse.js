function toggleCart(button) {
    const icon = button.querySelector('i');
    if (icon.classList.contains("bi-cart-plus")) {
        icon.classList.remove("bi-cart-plus");
        icon.classList.add("bi-cart-check-fill");
    }

}