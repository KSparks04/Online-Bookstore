$(document).on("submit", ".shopping-cart-remove-book", function (e) {
    e.preventDefault();

    if ($(this).data("submitted")) return;
    $(this).data("submitted", true);

    removeBook(this);
});

function removeBook (element){
    $.ajax({
        type: "POST",
        url: "/shopping-cart/edit/remove/" + $(element).data("isbn"),
        timeout: 5000,
        success: getShoppingCart
    });
}

function getShoppingCart(){
    $.ajax({
        type: "GET",
        url: "/shopping-cart",
        timeout: 5000,
        success: function(data){
            $("#shopping-cart-modal-body").html(data);

            if($("#checkout-table").length){
                $.ajax({
                    type: "GET",
                    url: "/shopping-cart/checkout-table",
                    timeout: 5000,
                    success: function(data){
                        $("#checkout-table").html(data);
                    }
                })
            }
        }
    });
}