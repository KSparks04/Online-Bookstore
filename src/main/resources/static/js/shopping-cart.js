$(document).ready(function () {
    $("#shopping-cart-modal").hide()

    $("#open-shopping-cart").click(function() {
        $("#shopping-cart-modal").show();
    })

    $("#close-shopping-cart").click(function() {
        $("#shopping-cart-modal").hide();
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