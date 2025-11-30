function getShoppingCart(){
    event.preventDefault();
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