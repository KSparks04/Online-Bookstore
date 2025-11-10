$(document).ready(function () {
    $(".purchase-button").submit(function () {
        event.preventDefault();
        purchaseBook(this);
    })
});

function purchaseBook (element){
    $.ajax({
        type: "POST",
        url: "/shopping-cart/edit/add/" + $(element).data("isbn"),
        timeout: 5000,
        success: getShoppingCart
    });
}