$(document).ready(function () {
    $(".shopping-cart-remove-book").submit(function () {
        event.preventDefault();
        removeBook(this);
    })
});

function removeBook (element){
    $.ajax({
        type: "POST",
        url: "/shopping-cart/edit/remove/" + $(element).data("isbn"),
        timeout: 5000,
        success: getShoppingCart
    });
}