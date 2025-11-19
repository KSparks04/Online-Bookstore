$(document).ready(function () {
    getCartCount();
    $("#shopping-cart-modal").hide()

    $(document).on("click", "#open-shopping-cart", function() {
        $("#shopping-cart-modal").show();
    })

    $(document).on("click", "#close-shopping-cart", function() {
        $("#shopping-cart-modal").hide();
    })

    $(document).on("click", ".purchase-button", function() {
        setTimeout(function() {
            getCartCount();
        }, 300);
    })

    $(document).on("click", ".shopping-cart-remove-book", function(){
        setTimeout(function() {
            getCartCount();
        }, 300);
    })
});

function getCartCount(){
    $.ajax({
        type: "GET",
        url: "/shopping-cart/count",
        timeout: 5000,
        success: function(data){
            updateCartCount(data.count);
        }
    });
}

function updateCartCount(count){
    const badge = $("#cart-count");
    badge.text(count);
    badge.toggleClass("d-none", count === 0);
}