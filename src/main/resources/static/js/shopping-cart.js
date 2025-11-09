$(document).ready(function () {
    $("#shopping-cart-modal").hide()

    $("#open-shopping-cart").click(function() {
        $("#shopping-cart-modal").show();
    })

    $("#close-shopping-cart").click(function() {
        $("#shopping-cart-modal").hide();
    })

    $(".purchase-button").submit(function () {
        event.preventDefault();
        purchaseBook(this);
    })
});

function updateShoppingCart(data){
    $("#shopping-cart-modal-body").html(data);
}

function getShoppingCart(){
    $.ajax({
        type: "GET",
        url: "/shopping-cart",
        timeout: 5000,
        success: updateShoppingCart
    });
}

function purchaseBook (element){
    $.ajax({
        type: "POST",
        url: "/shopping-cart/edit/add/" + $(element).data("isbn"),
        timeout: 5000,
        success: getShoppingCart
    });
}