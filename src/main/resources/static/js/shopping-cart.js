let shoppingCartModal;

$(document).ready(function () {
    const modalEl = document.getElementById('shopping-cart-modal');
    shoppingCartModal = new bootstrap.Modal(modalEl);

    $("#open-shopping-cart").click(function() {
        shoppingCartModal.show();
    });
});

function getShoppingCart(){
    event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/shopping-cart",
        timeout: 5000,
        success: function(data){
            $("#shopping-cart-modal-body").html(data);
        }
    });
}